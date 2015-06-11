package no.unified.soak.webapp.json;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.MailUtil;

import org.springframework.mail.MailSender;
import org.springframework.orm.ObjectRetrievalFailureException;

@Path("/paamelding")
public class PameldingJsonController {
	
	@Context UriInfo uriInfo;

	private RegistrationManager registrationManager;
	private CourseManager courseManager;
	private NotificationManager notificationManager;
	private ConfigurationManager configurationManager;

	private MailEngine mailEngine;
	private MailSender mailSender;

	private UserManager userManager;
	
    private static boolean ongoingBooking = false;

	public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }
	
	public void setCourseManager(CourseManager courseManager) {
		this.courseManager = courseManager;
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}
	
	public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@POST
	@Consumes( MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response registrerPamelding(Registration registration) {
		
		
		if (!isValid(registration)) {
			throw new WebApplicationException(Response.status(com.sun.jersey.api.client.ClientResponse.Status.PRECONDITION_FAILED).build());
		}

		
		registration.setLocale("no");
		registration.setStatus(Status.RESERVED);
		registration.setEmail(registration.getEmail().toLowerCase());
		registration.setUsername(registration.getEmail());
		
		ENTER__Critical__Section(registration.getEmail());
		
		Course course;
		try {
			course = courseManager.getCourse(""+registration.getCourseid());
		} catch (ObjectRetrievalFailureException e) {
			LEAVE__Critical__Section();
			throw new NotFoundException(e.getMessage());
		}
		
		//Check if course is published
        if (!course.getStatus().equals(CourseStatus.COURSE_PUBLISHED)) {
			LEAVE__Critical__Section();
        	throw new CourseCancelledException();
        }
		
		//Check if user is already registered - send error
		User user = userManager.findUserByEmail(registration.getEmail());
        if(user == null){
            user = userManager.addUser(registration);
        }
        
        //Check if already registered
        List<Registration> registrations = registrationManager.getCourseRegistrations(course.getId());
        for (Registration reg : registrations) {
			if (reg.getEmail() != null && reg.getEmail().equals(registration.getEmail())) {
				LEAVE__Critical__Section();
				throw new AlreadyRegisteredException();
			}
		}
        
        //Check if course is full
		updateAvailability(course);
		if (course.getAvailableAttendants() < registration.getParticipants()) {
			LEAVE__Critical__Section();
			throw new CourseFullException(); 
		}

		registration.setCourse(course);
		registration.setUser(user);
		try {
			registrationManager.saveRegistration(registration);
	
			saveNotification(registration, registration.getCourse());
			LEAVE__Critical__Section();
			
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			URI createdUri  = ub.path(""+registration.getId()).path(registration.getHash()).build();
	        try {
				sendMail(new Locale("no"), registration.getCourse(), registration, Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED);
			} catch (Exception e) {
				//Returning HTTP code 201 Created - response body contains "EMAIL_NOT_SENT"-message
	        	return Response.created(createdUri).entity("EMAIL_NOT_SENT").build();
			}
			
	        return Response.created(createdUri).build();
	        
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		} finally {
			LEAVE__Critical__Section();
		}
	}
	
	private void updateAvailability(Course course) {
		Integer available = registrationManager.getAvailability(true, course);
		course.setAvailableAttendants(available);
	}
	
	private void saveNotification(Registration registration, Course course) {
		Notification notification = new Notification();
        notification.setRegistrationid(registration.getId());
        notification.setReminderSent(false);
        notification.setIsFollowup(false);
        notificationManager.saveNotification(notification);

        if (course != null && course.hasFollowup()) {
            notification = new Notification();
            notification.setRegistrationid(registration.getId());
            notification.setReminderSent(false);
            notification.setIsFollowup(true);
            notificationManager.saveNotification(notification);
        }
	}
	
	private void sendMail(Locale locale, Course course, Registration registration, int event) throws Exception {
    	StringBuffer msg = null;
    	switch(event) {
	    	case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
	    		msg = MailUtil.create_EMAIL_EVENT_REGISTRATION_CONFIRMED_body(course, registration, null, configurationManager.getConfigurationsMap()); // bør endre navn
	    		break;
			case Constants.EMAIL_EVENT_REGISTRATION_CANCELLED:
				boolean chargeOverdue = false;
	        	if(new Date().after(course.getRegisterBy())) {
	        		if(course.getChargeoverdue()) {
	        			chargeOverdue = true;
	        		}
	        	}
				msg = MailUtil.create_EMAIL_EVENT_REGISTRATION_CANCELLED_body(course, registration, chargeOverdue, configurationManager.getConfigurationsMap());
				break;
    	}
    	boolean ccToResponsible = configurationManager.isActive("mail.registration.notifyResponsible", false);
        ArrayList<MimeMessage> theEmails = MailUtil.getMailMessages(registration, event, course, msg, mailSender, ccToResponsible);
        
        if (event == Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED) {
        	mailEngine.sendAndExceptionOnFail(theEmails.get(0));
        } else {
        	MailUtil.sendMimeMails(theEmails, mailEngine);
        }
    }

	private boolean isValid(Registration registration) {
		return (isSet(registration.getFirstName()) &&
				isSet(registration.getLastName()) &&
				isSet(registration.getBirthdate()) &&
				isSet(registration.getMobilePhone()) &&
				isSet(registration.getCourseid()) &&
				isSet(registration.getParticipants()) &&
				isSet(registration.getEmail()) &&
				isSet(registration.getPostnr()));
	}

	private boolean isSet(Object value) {
		return value != null && !"".equals(""+value);
	}

	@GET
	@Path("/{id}/{hash}")
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Registration hentPamelding(@PathParam("id") String registrationId, @PathParam("hash") String userhash) {
		try {
			Registration registration = registrationManager.getRegistration(registrationId);

			if (userhash == null || !userhash.equals(registration.getHash())) {
				throw new NotFoundException();
			}
			return registration;
		} catch (ObjectRetrievalFailureException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
	}
	
	@GET
	@Path("/slett/{id}/{hash}")
	public Response avbrytPamelding(@PathParam("id") String registrationId, @PathParam("hash") String userhash) {
		try {
			Registration registration = registrationManager.getRegistration(registrationId);
			if (userhash == null || !userhash.equals(registration.getHash())) {
				throw new NotFoundException();
			}
			registrationManager.removeRegistration(registrationId);

			try {
				sendMail(new Locale("no"), registration.getCourse(), registration, Constants.EMAIL_EVENT_REGISTRATION_CANCELLED);
				return Response.noContent().build();
			} catch (Exception e) {
				return Response.noContent().entity("EMAIL_NOT_SENT").type("text/plain").build();
			}
			
		} catch (ObjectRetrievalFailureException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
	}
	

	/**
	 * Innført pga dobbeltbooking problem hos SVV (som ikke benytter ventelister)
	 * @param email som identifikator i loggmeldinger
	 */
	private void ENTER__Critical__Section(String email){
		int venteTid = 0;
		final int MAX_VENTETID = 500; // ms
		
		if(!ongoingBooking){
			// Starter sekvens for sjekk av tilgjengelighet og evnt. lagring 
			ongoingBooking = true;
			return;
		}

		while(true){
			// Sekvens for å vente på en egen tidsluke
			if(!ongoingBooking && venteTid > 0) {
				//log.info(email + " ventet " + venteTid + "ms i registreringssekvens");
				return;
			}
			if(venteTid == MAX_VENTETID){
				//log.warn("Registreringssekvens TIMEOUT for " + email);
				return;
			}

			try {
				Thread.sleep(20);
				venteTid += 20;
			}catch(Exception e){ /* do nothing */ }
		}
	}
	
	private void LEAVE__Critical__Section(){
		ongoingBooking = false;
	}
}

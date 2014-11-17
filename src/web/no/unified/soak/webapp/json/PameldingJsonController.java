package no.unified.soak.webapp.json;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.mail.MailSender;

import sun.util.logging.resources.logging;
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
import no.unified.soak.util.MailUtil;

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
		try {
			Course course = courseManager.getCourse(""+registration.getCourseid());
			
			if (course == null) {
				throw new WebApplicationException(Response.status(com.sun.jersey.api.client.ClientResponse.Status.PRECONDITION_FAILED).build());
			}
			registration.setCourse(course);
		} catch (RuntimeException e) {
			throw new WebApplicationException(Response.status(com.sun.jersey.api.client.ClientResponse.Status.PRECONDITION_FAILED).build());
		}
//TODO: Sjekk om det er ledig plass
		registration.setLocale("no");
		registration.setStatus(Status.RESERVED);
		registration.setUsername(registration.getEmail());
		
		User user = userManager.findUserByEmail(registration.getEmail());
		
        if(user == null){
            user = userManager.addUser(registration);
        }

		registration.setUser(user);
		try {
			registrationManager.saveRegistration(registration);
	
			saveNotification(registration, registration.getCourse());
			
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			URI createdUri  = ub.path(""+registration.getId()).path(registration.getHash()).build();
			boolean emailSent = false;
	        try {
				sendMail(new Locale("no"), registration.getCourse(), registration, Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED);
				emailSent = true;
			} catch (Exception e) {
	        	return Response.created(createdUri).entity("Email not sent").build();
			}
			
	        if (emailSent) {
	        	return Response.created(createdUri).build();
	        } else {
	        	return Response.created(createdUri).entity("Email not sent").build();
	        }
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
	}
	
	private void saveNotification(Registration registration, Course course) {
		Notification notification = new Notification();
        notification.setRegistrationid(registration.getId());
        notification.setReminderSent(false);
        notificationManager.saveNotification(notification);
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
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/{id}/{hash}")
	public Registration hentPamelding(@PathParam("id") String registrationId, @PathParam("hash") String userhash) {
		try {
			Registration registration = registrationManager.getRegistration(registrationId);

			if (userhash == null || !userhash.equals(registration.getHash())) {
				throw new NotFoundException();
			}
			return registration;
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
	}
	
	@DELETE
	@Path("/{id}/{hash}")
	public Response avbrytPamelding(@PathParam("id") String registrationId, @PathParam("hash") String userhash) {
		try {
			Registration registration = registrationManager.getRegistration(registrationId);
			if (userhash == null || !userhash.equals(registration.getHash())) {
				throw new NotFoundException();
			}
			registrationManager.removeRegistration(registrationId);

			try {
				sendMail(new Locale("no"), registration.getCourse(), registration, Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED);
				return Response.noContent().build();
			} catch (Exception e) {
				return Response.noContent().entity("Email not sent").build();
			}
			
		} catch (Throwable e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
	}
}

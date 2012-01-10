/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created 21. dec 2005
 */
package no.unified.soak.webapp.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Registration;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.model.User;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.MailUtil;
import no.unified.soak.util.SMSUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementation of SimpleFormController that interacts with the
 * RegistrationManager to retrieve/persist values to the database.
 * 
 * @author hrj
 */
public class RegistrationFormController extends BaseFormController {
    private RegistrationManager registrationManager = null;
    private CourseManager courseManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private OrganizationManager organizationManager = null;
    private NotificationManager notificationManager = null;
    private UserManager userManager = null;
    @SuppressWarnings("unused")
    private MessageSource messageSource = null;
    protected MailEngine mailEngine = null;
    protected MailSender mailSender = null;

    private static boolean ongoingBooking = false;
    
    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }
    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }
    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map<String,Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String,Object> model = new HashMap<String,Object>();
        Locale locale = request.getLocale();

    	model.put("linkExpired", new Boolean(false));
        
        String courseId = request.getParameter("courseId");
        if ((courseId == null) || !StringUtils.isNumeric(courseId)) {
            // Redirect to error page - should never happen
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);
        User altUser = (User) session.getAttribute(Constants.ALT_USER_KEY);

        Boolean isAdmin = false;
        List<String> roles = null;
        if (user != null) {
            roles = user.getRoleNameList();
            isAdmin = (Boolean) roles.contains(Constants.ADMIN_ROLE);
        }

        String registrationId = request.getParameter("id");
        if ((registrationId != null) && StringUtils.isNumeric(registrationId) && StringUtils.isNotEmpty(registrationId)) {
        	Registration registration = registrationManager.getRegistration(registrationId);
        	model.put("registration", registration);

            if(!courseId.equals(registration.getCourseid().toString())){
				/**
				 * Dersom bruker har byttet møte/kurs og klikker på link i en
				 * påmeldingsepost hvor det senere har blitt foretatt et møtebytte
				 */
            	saveMessage(request, getText("registration.email.linkExpired", locale));
            	model.put("linkExpired", new Boolean(true));
            	return model;
            }
            
            // This is an existing registration, so get courses in case the user want to switch course.
            // Retrieve the all published courses and add them to the list
            Course courseForSearch = new Course();
            courseForSearch.setStatus(CourseStatus.COURSE_PUBLISHED);
            List<Course> courses = courseManager.searchCourses(courseForSearch, null, null, null);
            courses = updateAvailableAttendants(courses, request);
            List<Course> filtered = filterByRole(isAdmin, roles, courses);
            filtered = removeCoursesWithNoAvailableSeats(courses);
            
            if (courses != null) {
                model.put("courseList", filtered);
            }
        }
        else {
        	// tilleggsinformasjon vises ved førstegangsvisning av påmeldingsskjema dersom propery er definert
        	String additionalInfo = getText("registration.additionalInfo", locale);
        	if(StringUtils.isNotBlank(additionalInfo)) saveMessage(request, additionalInfo);
        }

        // Retrieve all serviceareas into an array
        List<ServiceArea> serviceAreas = serviceAreaManager.getAllIncludingDummy(getText("misc.none", locale));
        if (serviceAreas != null) {
            model.put("serviceareas", serviceAreas);
        }

        // Get all organizations
        List<Organization> organizations = new ArrayList<Organization>(); 
        String typeDBvalue = ApplicationResourcesUtil.getText("show.organization.pulldown.typeDBvalue");
        if (typeDBvalue != null && !StringUtils.isBlank(typeDBvalue)) {
        	Integer value = Integer.valueOf(typeDBvalue);
        	Type type = Organization.Type.getTypeFromDBValue(value);
            organizations = organizationManager.getByTypeIncludingDummy(type, getText("misc.all", locale));
        } else {
            organizations = organizationManager.getAllIncludingDummy(getText("misc.all", locale));
        }
        
        if(!ApplicationResourcesUtil.isSVV()){
	        // If user not admin, show only registered org
	        if (user != null && user.getOrganization() != null && !isAdmin) {
	            organizations.clear();
	            organizations.add(user.getOrganization());
	        }
	        
	        // If cached user, show only users org
	        if (altUser != null &&  altUser.getOrganization() != null && !isAdmin) {
	            organizations.clear();
	            organizations.add(altUser.getOrganization());
	        }
	        else {
	        	// Vi ønsker ikke begrensninger for hvem en innlogget bruker kan utføre påmeldinger for
	        }
        }
        model.put("organizations", organizations);

        // Retrieve the course the user wants to attend
        Course course = courseManager.getCourse(courseId);
        if (course != null) {
            model.put("course", course);
            if (course.getFeeExternal().equals(new Double("0")) && (course.getFeeInternal() != null && course.getFeeInternal().equals(new Double("0")))) {
                model.put("freeCourse", new Boolean(true));
            }
        }

        if (!legalRegistrationDate(request, request.getLocale(), course)) {
            model.put("illegalRegistration", new Boolean(true));
        }
        
        // sett startYear 16år tilbake fra i dag - benyttes til CalendarPopup
        Calendar start = new GregorianCalendar();
        start.setTime(new Date());
//        start.add(Calendar.YEAR, -16); // 16år var tilpasset SVV. Denne verdien bør property-styres for FKP
        model.put("startYear", start.getTime());

        // evnt. deaktivering av ventelistefunksjonalitet
        Integer availability = registrationManager.getAvailability(true, courseManager.getCourse(courseId));
        if (availability.intValue() > 0)
        	model.put("isCourseFull", new Boolean(false));
        else { 
        	if(configurationManager.isActive("access.registration.useWaitlists", true)){
        		// ventelistefunksjonalitet er aktivert, påmelding tillatt uten ledige plasser
	        	model.put("isCourseFull", new Boolean(false));
        		saveMessage(request, getText("errors.courseFull.waitlistwarning", locale));
        	}
        	else {
        		if(StringUtils.isBlank(registrationId)){
        			// meldingen skal kun gis dersom bruker ikke allerede er påmeldt dette møtet
        			model.put("isCourseFull", new Boolean(true));
	        		saveMessage(request, getText("errors.courseFull.warning", locale));
        		}
        	}
        }

        return model;
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        String id = request.getParameter("id");
        String courseId = request.getParameter("courseId");

        Registration registration = null;

        if (!StringUtils.isEmpty(id)) {
            registration = registrationManager.getRegistration(id);
            registration.setEmailRepeat(registration.getEmail());
        } else {
            registration = new Registration();
            registration.setCourseid(new Long(courseId));
            User user = (User) session.getAttribute(Constants.USER_KEY);
            
            if(user != null && user.getOrganization() != null){
                registration.setOrganization(user.getOrganization());
                registration.setOrganizationid(user.getOrganization().getId());
            }
            User regUser = (User) session.getAttribute(Constants.ALT_USER_KEY);
            
            boolean aktivBrukerErInnlogget = false;
            if(user != null){
            	aktivBrukerErInnlogget = !user.getUsername().equals(Constants.ANONYMOUS_ROLE);
            	// true  = Innlogget via eZ
            	// false = temporær hashbruker
//            	System.out.println("aktivBrukerErInnlogget: " + aktivBrukerErInnlogget);
            }
            
            if (configurationManager.isActive("access.registration.userdefaults",false) 
            		&& configurationManager.isActive("access.registration.anonymous",true)) {
            	// potensiell sammenblanding av forhåndsutfylt data
            	if(user != null && regUser != null && aktivBrukerErInnlogget && !user.getUsername().equalsIgnoreCase(regUser.getUsername())){
            		// innlogget bruker melder på flere (og andre enn seg selv) -- ikke forhåndsutfylle basert på "regUser"
            		regUser = null;
//                	System.out.println("SINDRE - debug 1");
            	}
            	else if(user != null && regUser == null && aktivBrukerErInnlogget){
            		// forhåndsutfylle med informasjon fra innlogget bruker
            		regUser = user;
//                	System.out.println("SINDRE - debug 2");
            	}
            	else if(user != null && regUser != null && !aktivBrukerErInnlogget){
            		// forhåndsutfylle med informasjon fra temporær bruker som allerede ligger i regUser-objektet
//                	System.out.println("SINDRE - debug 3");
            	}
            	else {
            		// user == null - ingen informasjon om bruker(e)
            		regUser = null;
//                	System.out.println("SINDRE - debug 4");
            	}
            }
            else if(configurationManager.isActive("access.registration.anonymous",true) && !aktivBrukerErInnlogget){
            	// kun forhåndsutfylle data for temporære brukere 
            	if(user != null && regUser != null){
            		// forhåndsutfylle med informasjon fra temporær bruker som allerede ligger i regUser-objektet
//                	System.out.println("SINDRE - debug 5");
            	}
            }
            else if(configurationManager.isActive("access.registration.userdefaults",false) && aktivBrukerErInnlogget){
            	// forhåndsutfylle data for innloggede "ordentlige" brukere 
        		regUser = user;
//            	System.out.println("SINDRE - debug 6");
            }
            else{
            	// ikke forhåndsutfylle data i skjema
            	regUser = null;
//            	System.out.println("SINDRE - debug 7");
            }
            
            if (regUser != null && !ApplicationResourcesUtil.isSVV()) {
                registration.setFirstName(regUser.getFirstName());
                registration.setLastName(regUser.getLastName());
                registration.setEmail(regUser.getEmail());
                registration.setBirthdate(regUser.getBirthdate());
                registration.setPhone(regUser.getPhoneNumber());
                registration.setMobilePhone(regUser.getMobilePhone());
                registration.setJobTitle(regUser.getJobTitle());
                registration.setWorkplace(regUser.getWorkplace());
                registration.setEmployeeNumber(regUser.getEmployeeNumber());
                if ((regUser.getOrganizationid() != null) && (regUser.getOrganizationid() != 0)) {
                    // Sett alltid organisasjon dersom denne finnes
                    registration.setOrganizationid(regUser.getOrganizationid());
                }
                if ((regUser.getServiceAreaid() != null) && (regUser.getServiceAreaid() != 0)) {
                    registration.setServiceAreaid(regUser.getServiceAreaid());
                }
                registration.setInvoiceName(regUser.getInvoiceName());
                registration.setInvoiceAddress(regUser.getInvoiceAddressCopy());
                registration.setClosestLeader(regUser.getClosestLeader());
            }
        }
        
        String birthdateFromRequest = request.getParameter("birthdate");
        if (StringUtils.isNotBlank(birthdateFromRequest)) {
        	request.setAttribute("birthdateFromRequest", birthdateFromRequest);
        }

        return registration;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        HttpSession session = request.getSession();

        String key = null;
        Map<String,Object> model = new HashMap<String,Object>();
        Boolean changedCourse = false;

        // Fetch the object from the form
        Registration registration = (Registration) command;

        if(registration.getId() != null){
        	// for å hindre "sammenblanding" av registration fra "command" og originalRegistration
        	registrationManager.evict(registration);  
        }
        
        Course course = courseManager.getCourse(registration.getCourseid().toString());
        Course originalCourse = null;
        Registration originalRegistration = null;
        //Check if course has been changed
        String courseId = request.getParameter("courseId");
        if (!courseId.equals(course.getId().toString())){
            changedCourse=true;
            originalCourse = courseManager.getCourse(courseId);
        }
        if ((registration.getId() != null) && (registration.getId().longValue() != 0)){
        	originalRegistration = registrationManager.getRegistration(registration.getId().toString());
            registrationManager.evict(originalRegistration); // for å hindre "sammenblanding" av registration fra "command" og originalRegistration  
        }
        
        // Fetch the locale for resource message
        Locale locale = request.getLocale();
        registration.setLocale(locale.getLanguage());

        // Are we to cancel?
        if (request.getParameter("docancel") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'cancel' from jsp");
            }

            return new ModelAndView(getCancelView(), "courseid", registration.getCourseid());
        } // or to delete?
        else if (request.getParameter("unregister") != null) {
            registrationManager.cancelRegistration(registration.getId().toString());
            saveMessage(request, getText("registration.canceled", locale) + " (" + registration.getFirstName() + " " + registration.getLastName() + " / " + registration.getCourse().getName() + " - " + DateUtil.getDateTime("dd.MM.yyyy", registration.getCourse().getStartTime()) + ")");
            sendMail(locale, course, registration, Constants.EMAIL_EVENT_REGISTRATION_CANCELLED);
            return new ModelAndView("redirect:listRegistrations.html");
        } else if (request.getParameter("delete") != null) {
            registrationManager.removeRegistration(registration.getId().toString());
            saveMessage(request, getText("registration.deleted", locale));
            return new ModelAndView("redirect:listRegistrations.html");
        } else {
            // Perform a new registration / update personal information

        	//force to loverCase because of bug in displaytag's sort-functionality
        	if(registration.getEmail() != null){ registration.setEmail(registration.getEmail().toLowerCase()); }
        	
        	// Check email
        	if(configurationManager.isActive("access.registration.emailrepeat",false) &&  !registration.getEmail().equalsIgnoreCase(registration.getEmailRepeat())){
                //                String error = "errors.email.notSame";
                errors.rejectValue("email", "errors.email.notSame",  new Object[] { registration.getEmail(), registration.getEmailRepeat() }, "Email addresses not equal.");
				registrationManager.evict(registration);
                return showForm(request,response, errors);
            }
            
            
            // -- validering på server pga problemer på klient
			Object[] args = null;

			if(configurationManager.isActive("access.registration.useBirthdate",false)){
				try {
					String birthdate_str = request.getParameter("birthdate");
					if(StringUtils.isNotEmpty(birthdate_str)){ 
						// utfylt, men ukjent format
						String format = getText("date.format", request.getLocale());
						Date birthdate = DateUtil.convertStringToDate(birthdate_str, format);
						if (birthdate != null) {
							// utfylt riktig format
							registration.setBirthdate(birthdate);
						} else {
							// utfylt feil format
							throw new BindException(registration, "birthdate");
						}
					}
				} catch (Exception e) {
					args = new Object[] { getText("registration.birthdate", request.getLocale()),
							getText("date.format.localized", request.getLocale()), ""};
					errors.rejectValue("birthdate", "errors.dateformat", args, "Invalid date");
				}
			}

			boolean freeCourse = false;
			if (course.getFeeExternal().equals(new Double("0")) && (course.getFeeInternal() != null && course.getFeeInternal().equals(new Double("0")))) {
				freeCourse = true;
            }
			if(!freeCourse){
				String sted = request.getParameter("invoiceAddress.city");
				if(!StringUtils.isNotEmpty(sted)){
					args = new Object[] { getText("registration.invoiceAddress.city", request.getLocale()), "", ""};
					errors.rejectValue("invoiceAddress.city", "errors.required", args, "");
				}
	
				String postnr = request.getParameter("invoiceAddress.postalCode");
				if(!StringUtils.isNotEmpty(postnr)){
					args = new Object[] { getText("registration.invoiceAddress.postalCode", request.getLocale()), "", ""};
					errors.rejectValue("invoiceAddress.postalCode", "errors.required", args, "");
				}
			}

			if(configurationManager.isActive("access.course.useParticipants",false)){
				String participants = request.getParameter("participants");
				if(!StringUtils.isNumeric(participants)){
					args = new Object[] { getText("registration.participants", request.getLocale()), "", ""};
					errors.rejectValue("participants", "errors.positivNumber", args, "");
				}
			}
			
            // Lets find out if the course has room for this one
            Boolean localAttendant = new Boolean(false);

            if(ApplicationResourcesUtil.isSVV()){
            	localAttendant = new Boolean(true);
            }
            else{
            	if(registration.getOrganizationid() != null) {
            		if (registration.getOrganizationid().longValue() == course.getOrganizationid().longValue()) {
            			localAttendant = new Boolean(true);
            		}
            	}
            }

            boolean useWaitLists = configurationManager.isActive("access.registration.useWaitlists", true);
            
            if (validateAnnotations(registration, errors, null) > 0) {
            	args = new Object[] {};
            }
            
            model.put("course", course);
            registration.setCourse(course);
            
            // Get registrations on this course for this user
            List<Registration> userRegistraionsForCourse = registrationManager.getUserRegistrationsForCourse(
            		registration.getEmail(), registration.getFirstName(), registration.getLastName(), registration
            		.getCourseid());
            
            // Is this a valid date to register? Or has this user already been registered to this course?
            // (It is always allowed to edit the data)
            if ((registration.getId() == null) || (registration.getId().longValue() == 0)) {
            	if (!legalRegistrationDate(request, locale, course)) {
            		return new ModelAndView(getCancelView(), "courseId", registration.getCourseid());
            	}
            	if (userRegistraionsForCourse.size() > 0) {
            		String start = DateUtil.convertDateToString(course.getStartTime());
            		saveMessage(request, getText("courseList.alreadyRegistered", new Object[]{course.getName(), start, course.getLocation().getName()}, locale) + "");
            		return new ModelAndView(getCancelView(), model);
            	}
            }
            
            // sjekk om det er nok plasser til alle deltakere
        	ENTER__Critical__Section(registration.getEmail());
        	// *****************************************************************************************************************************
        	
            Integer availability = registrationManager.getAvailability(localAttendant, course);
//            log.info("Sjekket tilgjengelighet for " + registration.getFirstName() + " " + registration.getLastName() + " - " + new Date().getTime() + " : " + availability);
            if(!changedCourse && originalRegistration != null){
				/*
				 * Siden dette kun er en oppdatering av en registrering må registeringens 
				 * orginalt besatte plasser frigjøres i forb. med beregning av tilgjengelighet
				 */
                if(!configurationManager.isActive("access.registration.useParticipants", false)){
                	originalRegistration.setParticipants(1); // setter antall deltakere til 1 når feltet ikke er i bruk
                }

            	availability += originalRegistration.getParticipants();
            }
            
        	if(!useWaitLists){
            	if (registration.getParticipants() != null && availability.intValue() < registration.getParticipants()) {
            		// det er ikke plass til alle deltakere i registreringen
            		args = new Object[] {availability, getText("courseList.theitem", request.getLocale()).toLowerCase(), ""};
    				errors.rejectValue("participants", "errors.limitedNumberOfSeats", args, "");
            	}
            }
            
			if (args != null) {
				registrationManager.evict(registration);

            	// skummel kode - veldig viktig å låse opp sekvens også ved feil!
            	LEAVE__Critical__Section();
            	// *****************************************************************************************************************************

				return showForm(request, response, errors);
			}
			
            //If course has been changed, send deleted mail for original course.  
            if (changedCourse && (originalCourse != null) && (originalRegistration != null)){
                sendMail(locale, originalCourse, originalRegistration, Constants.EMAIL_EVENT_REGISTRATION_CANCELLED);
            }

            // Set user object for registration
            User user = userManager.findUserByEmail(registration.getEmail());
            if(user == null){
                user = userManager.addUser(registration);
            }

			// For å unngå sammenblanding av innlogget bruker og temporære brukere (som følge av påmelding),
            // legges ikke brukeren fra påmeldingen inn i sesjonen når den utføres av innlogget bruker (men kun for "Anonymous User")
            User innloggetBruker = (User) session.getAttribute(Constants.USER_KEY);
            if(innloggetBruker != null && !innloggetBruker.getUsername().equals(Constants.ANONYMOUS_ROLE)){
            	// fjerner innhold i ALT_USER_KEY
            	session.setAttribute(Constants.ALT_USER_KEY, null);
            }
            else {
            	// legger bruker fra påmelding inn i ALT_USER_KEY
            	session.setAttribute(Constants.ALT_USER_KEY, user);
            }
            // ----------------------------------------------------------------------------------------------------------------------
            
            registration.setUser(user);
            registration.setUsername(user.getUsername());

            // update only ?? 
            boolean update = false;
            if(!changedCourse && (registration.getId() != null && registration.getId().longValue() != 0)){
            	update = true; // no change of course / not a new registration -- no email needed
            }
            
            if(!configurationManager.isActive("access.registration.useParticipants", false)){
            	registration.setParticipants(1); // setter antall deltakere til 1 når feltet ikke er i bruk
            }
            
            if (availability.intValue() >= registration.getParticipants()) {
                // There's room - save the registration
                registration.setStatus(Registration.Status.RESERVED);

                try {
	                // for å hindre "sammenblanding" av registration fra "command" og originalRegistration
	                if(registration.getId() != null){
		                Registration cleanUpObj = registrationManager.getRegistration(registration.getId().toString());
		                if(cleanUpObj != null){
		                	registrationManager.evict(cleanUpObj);
		                }
	                }
	                // -----------------------------------------------------------------------------------
	                
	                registrationManager.saveRegistration(registration);
//	                log.info("Lagret registrering for " + registration.getFirstName() + " " + registration.getLastName() + " - " + new Date().getTime());
                }
                finally {
                	// skummel kode - veldig viktig å låse opp sekvens også ved feil!
                	LEAVE__Critical__Section();
                	// *****************************************************************************************************************************
                }
                
                Notification notification = new Notification();
                notification.setRegistrationid(registration.getId());
                notification.setReminderSent(false);
                notificationManager.saveNotification(notification);
                if(update){
                	key = "registrationComplete.updated";
                	saveMessage(request, getText(key, locale));
                }else{
                	try {
						sendMail(locale, course, registration, Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED);
						saveMessage(request, getText("registrationComplete.completed", locale));
					} catch (Exception e) {
						saveMessage(request, getText("registrationComplete.completed.saved", locale));
						saveErrorMessage(request, getText("registrationComplete.completed.mailsending.failed", locale));
					}
                	if(configurationManager.isActive("sms.confirmedRegistrationChangedCourse", false)){
                		SMSUtil.sendRegistrationConfirmedMessage(registration, course);
                	}
                }

            } else {
                // The course is fully booked, put the applicant on the waiting list
                registration.setStatus(Registration.Status.WAITING);

                try {
	                // for å hindre "sammenblanding" av registration fra "command" og originalRegistration
	                if(registration.getId() != null){
		                Registration cleanUpObj = registrationManager.getRegistration(registration.getId().toString());
		                if(cleanUpObj != null){
		                	registrationManager.evict(cleanUpObj);
		                }
	                }
	                // -----------------------------------------------------------------------------------
	                
	                registrationManager.saveRegistration(registration);
                }
                finally {
                	// skummel kode - veldig viktig å låse opp sekvens også ved feil!
                	LEAVE__Critical__Section();
                	// *****************************************************************************************************************************
                }

                Notification notification = new Notification();
                notification.setRegistrationid(registration.getId());
                notification.setReminderSent(false);
                notification.setRegistration(registration);
                notificationManager.saveNotification(notification);
                key = "registrationComplete.waitinglist";
                saveMessage(request, getText(key, locale));
                sendMail(locale, course, registration, Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION);
            }

            // Let the next page know what registration we were editing here
            model.put("registrationid", registration.getId().toString());
        }

        return new ModelAndView(getSuccessView(), model);
    }

    /**
     * Sends mail to the user
     * 
     * @param locale
     *            The locale to use
     * @param course
     *            The course the applicant has registered for
     * @param registration
     * @param event
     * @throws Exception 
     */
    private void sendMail(Locale locale, Course course, Registration registration, int event) throws Exception {
    	StringBuffer msg = null;
    	switch(event) {
	    	case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
	    		msg = MailUtil.create_EMAIL_EVENT_REGISTRATION_CONFIRMED_body(course, registration, null, configurationManager.getConfigurationsMap()); // bør endre navn
	    		break;
	    	case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
	    		msg = MailUtil.create_EMAIL_EVENT_WAITINGLIST_NOTIFICATION_body(course, registration, null, false, configurationManager.getConfigurationsMap());
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

    /**
     * Checks if the user is allowed to register to the course at this time
     * 
     * @param request
     *            The HTTP request object
     * @param locale
     *            The current locale
     * @param course
     *            The course in question
     */
    private boolean legalRegistrationDate(HttpServletRequest request, Locale locale, Course course) {
        boolean result = true;
        Date now = new Date();

        // Too early?
        if ((course.getRegisterStart() != null) && course.getRegisterStart().after(now)) {
            // Not allowed to register yet
            saveMessage(request, getText("errors.tooearlytoregister", DateUtil.getDate(course.getRegisterStart()),
                    locale));
            result = false;
        }

        // Too late?
        if ((course.getRegisterBy() != null) && course.getRegisterBy().before(now)) {
            // Not allowed to register yet
            saveMessage(request, getText("errors.toolatetoregister", DateUtil.getDate(course.getRegisterBy()), locale));
            result = false;
        }

        return result;
    }

    private List<Course> filterByRole(Boolean admin, List<String> roles, List<Course> courses) {
        List<Course> filtered = new ArrayList<Course>();
        // Filter all courses not visible for the user.
        if (roles != null) {
            for (Iterator<Course> iterator = courses.iterator(); iterator.hasNext();) {
                Course roleCourse = iterator.next();
                roleCourse.setAvailableAttendants(0);
                if (roles.contains(roleCourse.getRole()) || admin.booleanValue()) {
                    if (roleCourse.getStopTime().after(new Date())) {
                        roleCourse.setAvailableAttendants(registrationManager.getAvailability(true, roleCourse));
                    }
                    filtered.add(roleCourse);
                }
            }
        } else {
            filtered = courses;
        }
        return filtered;
    }
    
	private List<Course> updateAvailableAttendants(List courses, HttpServletRequest request) {
		List<Course> updated = new ArrayList<Course>();
		Integer availableSum = NumberUtils.INTEGER_ZERO;
		for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
			Course course = (Course) iterator.next();
			course.setAvailableAttendants(NumberUtils.INTEGER_ZERO);
			if (course.getStopTime().after(new Date())) {
				Integer available = registrationManager.getAvailability(true, course);
				course.setAvailableAttendants(available);
				availableSum += available;
			}
			updated.add(course);
		}

		request.setAttribute("sumTotal", availableSum);
		return updated;
	}
	
	private List<Course> removeCoursesWithNoAvailableSeats(List courses){
		List<Course> notFull = new ArrayList<Course>();
		for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
			Course course = (Course) iterator.next();
			if(course.getAvailableAttendants() > 0){
				notFull.add(course);
			}
		}
		return notFull;
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
				log.info(email + " ventet " + venteTid + "ms i registreringssekvens");
				return;
			}
			if(venteTid == MAX_VENTETID){
				log.warn("Registreringssekvens TIMEOUT for " + email);
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

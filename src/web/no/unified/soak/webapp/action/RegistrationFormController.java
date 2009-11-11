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
import java.util.Date;
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
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.MailUtil;

import org.apache.commons.lang.StringUtils;
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
    private ConfigurationManager configurationManager = null;
    private UserManager userManager = null;
    private MessageSource messageSource = null;
    protected MailEngine mailEngine = null;
    protected MailSender mailSender = null;

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

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map<String,Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String,Object> model = new HashMap<String,Object>();
        Locale locale = request.getLocale();

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
            model.put("registration", registrationManager.getRegistration(registrationId));

            // This is an existing registration, so get courses in case the user want to switch course.
            // Retrieve the all published courses
            // and add them to the list
            Course courseForSearch = new Course();
            courseForSearch.setStatus(CourseStatus.COURSE_PUBLISHED);
            List<Course> courses = courseManager.searchCourses(courseForSearch, null, null);
            List<Course> filtered = filterByRole(isAdmin, roles, courses);

            if (courses != null) {
                model.put("courseList", filtered);
            }

        }

        // Retrieve all serviceareas into an array
        List<ServiceArea> serviceAreas = serviceAreaManager.getAllIncludingDummy(getText("misc.none", locale));
        if (serviceAreas != null) {
            model.put("serviceareas", serviceAreas);
        }

        // Get all organizations
        List<Organization> organizations = organizationManager.getAllIncludingDummy(getText("misc.none", locale));
        if (organizations == null) {
            organizations = new ArrayList<Organization>();
        }
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
        model.put("organizations", organizations);

        // Retrieve the course the user wants to attend
        Course course = courseManager.getCourse(courseId);
        if (course != null) {
            model.put("course", course);
            if (course.getFeeExternal().equals(new Double("0"))) {
                model.put("freeCourse", new Boolean(true));
            }
        }

        if (!legalRegistrationDate(request, request.getLocale(), course)) {
            model.put("illegalRegistration", new Boolean(true));
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
            User regUser = null;
            //            Locale locale = request.getLocale();
            String userdefaults = configurationManager.getValue("access.registration.userdefaults","false");
            if (userdefaults != null && userdefaults.equals("true")) {
                regUser = user;
                if (user != null && user.getUsername().equals(Constants.ANONYMOUS_ROLE)) {
                    regUser = (User) session.getAttribute(Constants.ALT_USER_KEY);
                }
            } else {
                regUser = (User) session.getAttribute(Constants.ALT_USER_KEY);
            }

            if (regUser != null) {
                registration.setFirstName(regUser.getFirstName());
                registration.setLastName(regUser.getLastName());
                registration.setEmail(regUser.getEmail());
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
        Boolean courseFull = null;
        Boolean alreadyRegistered = false;
        Boolean changedCourse = false;

        // Fetch the object from the form
        Registration registration = (Registration) command;
        Course course = courseManager.getCourse(registration.getCourseid().toString());
        Course originalCourse = null;
        Registration originalRegistration = null;
        //Check if course has been changed
        String courseId = request.getParameter("courseId");
        if (!courseId.equals(course.getId().toString())){
            changedCourse=true;
            originalCourse = courseManager.getCourse(courseId);
            if ((registration.getId() != null) && (registration.getId().longValue() != 0)){
                originalRegistration = registrationManager.getRegistration(registration.getId().toString());
            }
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
            registrationManager.removeRegistration(registration.getId().toString());
            saveMessage(request, getText("registration.unregistered", locale));
            sendMail(locale, course, registration, Constants.EMAIL_EVENT_REGISTRATION_DELETED);
            return new ModelAndView(getCancelView(), "courseid", registration.getCourseid());
        } else if (request.getParameter("delete") != null) {
            registrationManager.removeRegistration(registration.getId().toString());
            saveMessage(request, getText("registration.deleted", locale));

            return new ModelAndView(getCancelView(), "courseid", registration.getCourseid());
        } else {
            // Perform a new registration

            // Check email
            if(configurationManager.getValue("access.registration.emailrepeat","false").equals("true") &&  !registration.getEmail().equals(registration.getEmailRepeat())){
                //                String error = "errors.email.notSame";
                errors.rejectValue("email", "errors.email.notSame",  new Object[] { registration.getEmail(), registration.getEmailRepeat() }, "Email addresses not equal.");

                return showForm(request,response, errors);
            }

            // We need the course as reference data
            // Course course =
            // courseManager.getCourse(registration.getCourseid().toString());
            model.put("course", course);
            registration.setCourse(course);

            // Get registrations on this course for this user
            List<Registration> userRegistraionsForCourse = registrationManager.getUserRegistrationsForCourse(
                    registration.getEmail(), registration.getFirstName(), registration.getLastName(), registration
                    .getCourseid());

            // Is this a valid date to register? Or has this user already been
            // registered to this course?
            // (It is always allowed to edit the data)
            if ((registration.getId() == null) || (registration.getId().longValue() == 0)) {
                if (!legalRegistrationDate(request, locale, course)) {
                    return new ModelAndView(getCancelView(), "courseId", registration.getCourseid());
                }
                if (userRegistraionsForCourse.size() > 0) {
                    alreadyRegistered = true;
                    model.put("alreadyRegistered", alreadyRegistered);
                    return new ModelAndView(getCancelView(), "alreadyRegistered", true);
                }
            }

            //If course has been changed, send deleted mail for original course.  
            if (changedCourse && (originalCourse != null) && (originalRegistration != null)){
                sendMail(locale, originalCourse, originalRegistration, Constants.EMAIL_EVENT_REGISTRATION_DELETED);
            }

            // Set user object for registration
            User user = userManager.findUser(registration.getEmail());
            if(user == null){
                user = userManager.addUser(registration);
            }
            session.setAttribute(Constants.ALT_USER_KEY, user);

            registration.setUser(user);
            registration.setUsername(user.getUsername());

            // Lets find out if the course has room for this one
            Boolean localAttendant = new Boolean(false);

            if (registration.getOrganizationid() != null) {
                if (registration.getOrganizationid().longValue() == course.getOrganizationid().longValue()) {
                    localAttendant = new Boolean(true);
                }
            }

            Integer availability = registrationManager.getAvailability(localAttendant, course);

            if (availability.intValue() > 0) {
                // There's room - save the registration
                courseFull = new Boolean(false);
                registration.setReserved(new Boolean(true));
                registrationManager.saveRegistration(registration);
                Notification notification = new Notification();
                notification.setRegistrationid(registration.getId());
                notification.setReminderSent(false);
                notificationManager.saveNotification(notification);
                key = "registrationComplete.completed";
                saveMessage(request, getText(key, locale));
                sendMail(locale, course, registration, Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED);

            } else {
                // The course is fully booked, put the applicant on the waiting list
                courseFull = new Boolean(true);
                registration.setReserved(new Boolean(false));
                registrationManager.saveRegistration(registration);
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

            // Let us also put whether the applicant is put on the waiting list
            model.put("courseFull", courseFull);
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
     */
    private void sendMail(Locale locale, Course course, Registration registration, int event) {
    	StringBuffer msg = null;
    	switch(event) {
	    	case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
	    		msg = MailUtil.create_EMAIL_EVENT_REGISTRATION_CONFIRMED_body(course, registration, null); // bør endre navn
	    		break;
	    	case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
	    		msg = MailUtil.create_EMAIL_EVENT_WAITINGLIST_NOTIFICATION_body(course, registration, null, false);
	    		break;
			case Constants.EMAIL_EVENT_REGISTRATION_DELETED:
				boolean chargeOverdue = false;
	        	if(new Date().after(course.getRegisterBy())) {
	        		if(course.getChargeoverdue()) {
	        			chargeOverdue = true;
	        		}
	        	}
				msg = MailUtil.create_EMAIL_EVENT_REGISTRATION_DELETED_body(course, chargeOverdue);
				break;
    	}
        ArrayList<MimeMessage> theEmails = MailUtil.getMailMessages(registration, event, course, msg, mailSender);
        MailUtil.sendMimeMails(theEmails, mailEngine);
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
}

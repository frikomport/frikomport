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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.mail.internet.MimeMessage;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.MailUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.orm.ObjectRetrievalFailureException;

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

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map model = new HashMap();
		Locale locale = request.getLocale();

		String courseId = request.getParameter("courseid");
		if ((courseId == null) || !StringUtils.isNumeric(courseId)) {
			// Redirect to error page - should never happen
		}

		String registrationId = request.getParameter("id");
		if ((registrationId != null) && StringUtils.isNumeric(registrationId) && StringUtils.isNotEmpty(registrationId)) {
			model.put("registration", registrationManager.getRegistration(registrationId));
		}

		// Retrieve all serviceareas into an array
		List serviceAreas = serviceAreaManager.getAllIncludingDummy(getText("misc.none", locale));
		if (serviceAreas != null) {
			model.put("serviceareas", serviceAreas);
		}

		// Retrieve all organizations into an array
		List organizations = organizationManager.getAllIncludingDummy(getText("misc.none", locale));
		if (organizations != null) {
			model.put("organizations", organizations);
		}

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
		Registration registration = null;

		if (!StringUtils.isEmpty(id)) {
			registration = registrationManager.getRegistration(id);
		} else {
			registration = new Registration();
			User user = null;
			Locale locale = request.getLocale();
			String userdefaults = getText("access.registration.userdefaults", locale);
			String anonymous = getText("access.registration.anonymous", locale);
			if (userdefaults != null && userdefaults.equals("true")) {
				user = (User) session.getAttribute(Constants.USER_KEY);
				if (anonymous != null && anonymous.equals("true")
						&& user.getUsername().equals(Constants.ANONYMOUS_ROLE)) {
					user = (User) session.getAttribute(Constants.ALT_USER_KEY);
				}
			} else {
				user = (User) session.getAttribute(Constants.ALT_USER_KEY);
			}

			if (user != null) {
				registration.setFirstName(user.getFirstName());
				registration.setLastName(user.getLastName());
				registration.setEmail(user.getEmail());
				registration.setPhone(user.getPhoneNumber());
				registration.setMobilePhone(user.getMobilePhone());
				registration.setJobTitle(user.getJobTitle());
				registration.setWorkplace(user.getWorkplace());
				registration.setEmployeeNumber(user.getEmployeeNumber());
				if ((user.getOrganizationid() != null) && (user.getOrganizationid() != 0)) {
					registration.setOrganizationid(user.getOrganizationid());
				}
				if ((user.getServiceareaid() != null) && (user.getServiceareaid() != 0)) {
					registration.setServiceareaid(user.getServiceareaid());
				}
				registration.setInvoiceName(user.getInvoiceName());
				registration.setInvoiceAddress(user.getInvoiceAddressCopy());
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

		String key = null;
		Map model = new HashMap();
		Boolean courseFull = null;
		Boolean alreaddyRegistered = false;

		// Fetch the object from the form
		Registration registration = (Registration) command;
		Course course = courseManager.getCourse(registration.getCourseid().toString());

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

			// We need the course as reference data
			// Course course =
			// courseManager.getCourse(registration.getCourseid().toString());
			model.put("course", course);
			registration.setCourse(course);

			// Is this a valid date to register?
			// (It is always allowed to edit the data)
			if ((registration.getId() == null) || (registration.getId().longValue() == 0)) {
				if (!legalRegistrationDate(request, locale, course)) {
					return new ModelAndView(getCancelView(), "courseid", registration.getCourseid());
				}
			}

			// Set user object for registration
			User user = null;
			try {
				user = userManager.findUser(registration.getEmail());
			} catch (ObjectRetrievalFailureException orfe) {
				user = userManager.addUser(registration);
			}
			registration.setUser(user);
			registration.setUsername(user.getUsername());

			// Lets see if this user has alreaddy been registered to this course
			if (registrationManager.isUserRegisteredOnCourse(registration.getEmail(), registration.getFirstName(),
					registration.getLastName(), registration.getCourseid())) {
				alreaddyRegistered = true;
				model.put("alreaddyRegistered", alreaddyRegistered);
				return new ModelAndView(getCancelView(), "alreaddyRegistered", true);
			}

			// Lets find out if the course has room for this one
			Boolean localAttendant = new Boolean(true);

			if (registration.getOrganizationid() != null) {
				if (registration.getOrganizationid().longValue() != course.getOrganizationid().longValue()) {
					localAttendant = new Boolean(false);
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
				// The course is fully booked, put the applicant on the
				// waiting
				// list
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

			notificationManager.sendReminders();

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
		StringBuffer msg = MailUtil.createStandardBody(course, event, locale, messageSource, null, true);
		ArrayList<MimeMessage> theEmails = MailUtil.getMailMessages(registration, event, course, msg, messageSource,
				locale, mailSender);
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
}

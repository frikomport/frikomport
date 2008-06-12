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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
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

	private MessageSource messageSource = null;

	protected MailEngine mailEngine = null;

	protected SimpleMailMessage message = null;

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
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

	public void setOrganizationManager(
			OrganizationManager organizationManager) {
		this.organizationManager = organizationManager;
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
		if ((registrationId != null) && StringUtils.isNumeric(registrationId)
				&& StringUtils.isNotEmpty(registrationId)) {
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
		}

		if (!legalRegistrationDate(request, request.getLocale(), course)) {
			model.put("illegalRegistration", new Boolean(true));
		}

		return model;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession(true);
		String id = request.getParameter("id");
		Registration registration = null;

		if (!StringUtils.isEmpty(id)) {
			registration = registrationManager.getRegistration(id);
		} else {
			registration = new Registration();
	        // Check if a default organization should be applied
            User user = (User)session.getAttribute(Constants.USER_KEY);
            if((user.getOrganizationid() != null) && (user.getOrganizationid() != 0)){
                registration.setOrganizationid(user.getOrganizationid());
            }

            if((user.getServiceareaid() != null) && (user.getServiceareaid() != 0)){
                registration.setServiceareaid(user.getServiceareaid());
            }
            
            // set default values
            Locale locale = request.getLocale();
            String userdefaults = getText("access.registration.userdefaults",locale);
            if(userdefaults != null && userdefaults.equals("true")){
                registration.setFirstName(user.getFirstName());
                registration.setLastName(user.getLastName());
                registration.setEmail(user.getEmail());
                registration.setPhone(user.getPhoneNumber());
                registration.setMobilePhone(user.getMobilePhone());
                registration.setJobTitle(user.getJobTitle());
                registration.setWorkplace(user.getWorkplace());
                registration.setEmployeeNumber(user.getEmployeeNumber());
            }
        }

		return registration;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'onSubmit' method...");
		}

		String key = null;
		Map model = new HashMap();
		Boolean courseFull = null;

		// Fetch the object from the form
		Registration registration = (Registration) command;

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

			return new ModelAndView(getCancelView(), "courseid", registration.getCourseid());
		} else if (request.getParameter("delete") != null) {
            registrationManager.removeRegistration(registration.getId().toString());
            saveMessage(request, getText("registration.deleted", locale));

            return new ModelAndView(getCancelView(), "courseid", registration.getCourseid());
        } else {
			// Perform a new registration

			// We need the course as reference data
			Course course = courseManager.getCourse(registration.getCourseid()
					.toString());

			// Is this a valid date to register?
			// (It is always allowed to edit the data)
			if ((registration.getId() == null)
					|| (registration.getId().longValue() == 0)) {
				if (!legalRegistrationDate(request, locale, course)) {
					return new ModelAndView(getCancelView(), "courseid",
							registration.getCourseid());
				}
			}

			// Lets find out if the course has room for this one
			Boolean localAttendant = new Boolean(true);

			if (registration.getOrganizationid() != null){
				if (registration.getOrganizationid().longValue() != course
						.getOrganizationid().longValue()) {
					localAttendant = new Boolean(false);
				}
			}

			Integer availability = registrationManager.getAvailability(
					localAttendant, course);

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

				// Send an e-mail
				// MessageSourceAccessor text =
				// new MessageSourceAccessor(messageSource,
				// request.getLocale());
				sendMail(locale, course, false, registration);
			} else {
				// The course is fully booked, put the applicant on the waiting
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
				sendMail(locale, course, true, registration);
			}
		}
		
		notificationManager.sendReminders();

		// Let the next page know what registration we were editing here
		model.put("registrationid", registration.getId().toString());

		// Let us also put whether the applicant is put on the waiting list
		model.put("courseFull", courseFull);

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
	 *            TODO
	 */
	private void sendMail(Locale locale, Course course, boolean waitinglist,
			Registration registration) {
		StringBuffer msg = new StringBuffer();

		msg.append(getText("misc.hello", locale) + " "
				+ registration.getFirstName() + " "
				+ registration.getLastName());

		if (registration.getEmployeeNumber() != null)
		{
			String employeeNumberText = messageSource.getMessage("registration.employeeNumber",
					null, locale);
			if (!StringUtils.isEmpty(employeeNumberText))
				employeeNumberText = employeeNumberText.toLowerCase();

			msg.append(StringUtil.ifEmpty(registration.getEmployeeNumber(), " ("
					+ StringEscapeUtils.unescapeHtml(employeeNumberText
							) + " "
					+ registration.getEmployeeNumber().intValue() + ")"));
		}
		
		msg.append("\n\n");
		// Build mail
		if (waitinglist) {
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"waitinglist.mail.body", " " + course.getName(), locale))
					+ "\n\n");
		} else {
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"registrationComplete.mail.body", " " + course.getName(),
					locale))
					+ "\n\n");
		}

		// Include course details
		msg.append(StringEscapeUtils
				.unescapeHtml(getText("course.name", locale))
				+ ": " + course.getName() + "\n");
		msg.append(StringEscapeUtils
				.unescapeHtml(getText("course.type", locale))
				+ ": " + course.getType() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.startTime",
				locale))
				+ ": "
				+ DateUtil
						.getDateTime(getText("date.format", locale) + " "
								+ getText("time.format", locale), course
								.getStartTime()) + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.stopTime",
				locale))
				+ ": "
				+ DateUtil.getDateTime(getText("date.format", locale) + " "
						+ getText("time.format", locale), course.getStopTime())
				+ "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.duration",
				locale))
				+ ": " + course.getDuration() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText(
				"course.organization", locale))
				+ ": " + course.getOrganization().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea",
				locale))
				+ ": " + course.getServiceArea().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.location",
				locale))
				+ ": " + course.getLocation().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.responsible",
				locale))
				+ ": " + course.getResponsible().getFullName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor",
				locale))
				+ ": " + course.getInstructor().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.description",
				locale))
				+ ": " + course.getDescription() + "\n");

		msg.append('\n');
		if (waitinglist) {
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"waitinglist.mail.footer", locale)));
		} else {
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"registrationComplete.mail.footer", locale)));
		}

		List<String> emails = new LinkedList<String>();
		if (registration.getEmail() != null
				&& registration.getEmail().trim().length() > 0) {
			emails.add(registration.getEmail());
		}
		if (emails.size() == 0 && course.getInstructor().getEmail() != null) {
			emails.add(course.getInstructor().getEmail());
		}

		String[] stringEmails = StringUtil.list2Array(emails);
		message.setTo(stringEmails);

		if (waitinglist) {
			message.setSubject(StringEscapeUtils.unescapeHtml(getText(
					"waitinglist.mail.subject", course.getName(), locale)));
		} else {
			message.setSubject(StringEscapeUtils.unescapeHtml(getText(
					"registrationComplete.mail.subject", course.getName(),
					locale)));
		}

		msg.append("\n\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.contactinfo",
				locale))
				+ "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.donotreply",
				getText("mail.default.from", locale), locale))
				+ "\n");

		message.setText(msg.toString());
		mailEngine.send(message);
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
	private boolean legalRegistrationDate(HttpServletRequest request,
			Locale locale, Course course) {
		boolean result = true;
		Date now = new Date();

		// Too early?
		if ((course.getRegisterStart() != null)
				&& course.getRegisterStart().after(now)) {
			// Not allowed to register yet
			saveMessage(request, getText("errors.tooearlytoregister", DateUtil
					.getDate(course.getRegisterStart()), locale));
			result = false;
		}

		// Too late?
		if ((course.getRegisterBy() != null)
				&& course.getRegisterBy().before(now)) {
			// Not allowed to register yet
			saveMessage(request, getText("errors.toolatetoregister", DateUtil
					.getDate(course.getRegisterBy()), locale));
			result = false;
		}

		return result;
	}
}

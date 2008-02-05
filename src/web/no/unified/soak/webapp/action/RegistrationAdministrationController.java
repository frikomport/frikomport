/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created January 14th, 2005
 */
package no.unified.soak.webapp.action;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.MunicipalitiesManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.MailUtil;
import no.unified.soak.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.context.MessageSource;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.validation.BindException;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This controller handles the management of a course - where an authorized user can manage the waiting list, which
 * invoices has been sent and so on
 * 
 * @author Henrik RJ
 * 
 */
public class RegistrationAdministrationController extends BaseFormController {
	private RegistrationManager registrationManager = null;

	private CourseManager courseManager = null;

	private ServiceAreaManager serviceAreaManager = null;

	private MunicipalitiesManager municipalitiesManager = null;

	private WaitingListManager waitingListManager = null;

	private MessageSource messageSource = null;

	protected MailEngine mailEngine = null;

	protected SimpleMailMessage message = null;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}

	public void setWaitingListManager(WaitingListManager waitingListManager) {
		this.waitingListManager = waitingListManager;
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

	public void setMunicipalitiesManager(MunicipalitiesManager municipalitiesManager) {
		this.municipalitiesManager = municipalitiesManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'referenceData' method in Administration controller...");
		}

		Map model = new HashMap();

		String courseId = request.getParameter("courseid");

		// Retrieve all serviceareas into an array
		List serviceAreas = serviceAreaManager.getServiceAreas();

		if (serviceAreas != null) {
			model.put("serviceareas", serviceAreas);
		}

		// Retrieve all municipalities into an array
		List municipalities = municipalitiesManager.getAll();

		if (municipalities != null) {
			model.put("municipalities", municipalities);
		}

		// Retrieve the course the user wants to attend
		Course course = courseManager.getCourse(courseId);

		if (course != null) {
			model.put("course", course);
			
            Date today = new Date();
            Boolean allowRegistration = new Boolean(false);

            if (today.before(course.getRegisterBy())
                    && (course.getRegisterStart() == null || today
                            .after(course.getRegisterStart()))) {
                allowRegistration = new Boolean(true);
            }

            model.put("allowRegistration", allowRegistration);
            
            Integer registrations = registrationManager.getNumberOfAttendants(false, course);
            model.put("isCourseFull", new Boolean(registrations.intValue() >= course.getMaxAttendants()));
		}

		/*
		 * // Retrive the registrations attached to the course List registrations =
		 * registrationManager.getSpecificRegistrations(new Long(courseId), null, null, null, null); if (registrations !=
		 * null) model.put("registrations", registrations);
		 */
		return model;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'formBackingObject' method in Administration controller...");
		}

		RegistrationsBackingObject registrationsBackingObject = new RegistrationsBackingObject();

		// Get the courseId to administrer registrations for
		String courseId = request.getParameter("courseid");

		if ((courseId == null) || !StringUtils.isNumeric(courseId)) {
			// TODO:Redirect to error page - should never happen
		}

		List registrations = registrationManager.getSpecificRegistrations(new Long(courseId), null, null, null, null,
				null, null);
		registrationsBackingObject.setRegistrations(registrations);

		return registrationsBackingObject;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'onSubmit' method in Administration controller...");
		}

		Locale locale = request.getLocale();

		RegistrationsBackingObject registrationsBackingObject = (RegistrationsBackingObject) command;

		// Remember which course we are working with
		Map model = new HashMap();
		String courseId = request.getParameter("courseid");
		model.put("courseid", new Long(courseId));

		// Are we to cancel?
		if (request.getParameter("docancel") != null) {
			if (log.isDebugEnabled()) {
				log.debug("recieved 'cancel' from jsp");
			}

			return new ModelAndView(getCancelView(), "id", courseId);
		} // or to delete?
		else if (request.getParameter("unregister") != null) {
			// We are deleting a registration
			unregisterRegistration(request, locale);

			// Ready the WaitingListManagerImpl
			waitingListManager.processIfNeeded(new Long(courseId), locale);

			String key = "waitinglist.updated";
			saveMessage(request, getText(key, locale));
		}
		else if (request.getParameter("delete") != null) {
            // We are deleting a registration
            deleteRegistration(request, locale);

            // Ready the WaitingListManagerImpl
            waitingListManager.processIfNeeded(new Long(courseId), locale);

            String key = "waitinglist.updated";
            saveMessage(request, getText(key, locale));
        } else {
			// We are saving all changes made to the list

			// Loop over the rows and check for changes and the presist what
			// changes there are
			if (registrationsBackingObject != null) {
				if (persistChanges(request, registrationsBackingObject)) {
					// Run the waiting list process
					waitingListManager.processIfNeeded(new Long(courseId), locale);

					String key = "waitinglist.updated";
					saveMessage(request, getText(key, locale));
				}

				String key = "registrationList.updated";
				saveMessage(request, getText(key, locale));
			}
		}

		return new ModelAndView(getSuccessView(), model);
	}

	/**
	 * Scans through the list of objects for changes, and persists the changes that have been made
	 * 
	 * @param request
	 *            The HTTP Request object
	 * @param registrationsBackingObject
	 *            The objects from the form
	 * @return true if there were changes that have been persisted
	 */
	private boolean persistChanges(HttpServletRequest request, RegistrationsBackingObject registrationsBackingObject) {
		boolean runWaitingList = false;

		// Loop over the rows and check for changes and the presist what
		// changes there are
		for (int i = 0; i < registrationsBackingObject.getRegistrations().size(); i++) {
			boolean changed = false;

			// Get current row
			Registration thisRegistration = registrationsBackingObject.getRegistrations().get(i);

			// Get reserved and invoiced as seen in the table at the
			// time of submit
			String reservedCheckbox = request.getParameter("reserved_" + thisRegistration.getId().toString());
			String hiddenReservedCheckbox = request.getParameter("_reserved" + thisRegistration.getId().toString()); 
			String invoicedCheckbox = request.getParameter("invoiced_" + thisRegistration.getId().toString());
			String hiddenInvoicedCheckbox = request.getParameter("_invoiced" + thisRegistration.getId().toString()); 
			String attendedCheckbox = request.getParameter("attended_" + thisRegistration.getId().toString());
			String hiddenAttendedCheckbox = request.getParameter("_attended" + thisRegistration.getId().toString()); 
			boolean reserved = checkboxToBoolean(reservedCheckbox);
			boolean hiddenReservedPresent = checkboxToBoolean(hiddenReservedCheckbox);
			boolean invoiced = checkboxToBoolean(invoicedCheckbox);
			boolean hiddenInvoicedPresent = checkboxToBoolean(hiddenInvoicedCheckbox);
			boolean attended = checkboxToBoolean(attendedCheckbox);
			boolean hiddenAttendedPresent = checkboxToBoolean(hiddenAttendedCheckbox);

			// Check if reserved is changed
			if (thisRegistration.getReserved().booleanValue() != reserved && hiddenReservedPresent) {
				changed = true;
				thisRegistration.setReserved(new Boolean(reserved));

				// Send mail about the status change of the reservation
				if (reserved) {
					sendMail(request.getLocale(), thisRegistration.getCourse(), Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED,
							thisRegistration);
				} else {
					sendMail(request.getLocale(), thisRegistration.getCourse(), Constants.EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST,
							thisRegistration);
				}
			}

			// Check if invoiced is changed
			if (thisRegistration.getInvoiced().booleanValue() != invoiced && hiddenInvoicedPresent) {
				changed = true;
				thisRegistration.setInvoiced(new Boolean(invoiced));
			}
			
	         // Check if attended is changed
            if (thisRegistration.getAttended().booleanValue() != attended && hiddenAttendedPresent) {
                changed = true;
                thisRegistration.setAttended(new Boolean(attended));
            }

			if (changed) {
				runWaitingList = true;
				registrationManager.saveRegistration(thisRegistration);
			}
		}

		return runWaitingList;
	}

	/**
	 * Unregister a registration from the database and sends confirmation email
	 * 
	 * @param request
	 *            The HTTP request object
	 * @param locale
	 *            The current locale
	 */
	private void unregisterRegistration(HttpServletRequest request, Locale locale) {
		String regid = request.getParameter("regid");

		// Send mail to the person in question
		Registration registration = registrationManager.getRegistration(regid);
		Course course = registration.getCourse();
		sendMail(locale, course, Constants.EMAIL_EVENT_REGISTRATION_DELETED, registration);
		registrationManager.removeRegistration(regid);

		String key = "registration.unregistered";
		saveMessage(request, getText(key, locale));
	}
	
	/**
     * Deletes a registration from the database
     * 
     * @param request
     *            The HTTP request object
     * @param locale
     *            The current locale
     */
    private void deleteRegistration(HttpServletRequest request, Locale locale) {
        String regid = request.getParameter("regid");

        // Send mail to the person in question
        Registration registration = registrationManager.getRegistration(regid);
        Course course = registration.getCourse();
        registrationManager.removeRegistration(regid);

        String key = "registration.deleted";
        saveMessage(request, getText(key, locale));
    }

	/**
	 * Converts a forms textual boolean to a boolean Expects input "null" or (null) or "false" for false - all others
	 * are read as true.
	 * 
	 * @param request
	 *            String returned from form.
	 * @return request converted to boolean value
	 */
	private boolean checkboxToBoolean(String request) {
		boolean result = true;

		if ((request == null) || (request.compareTo("null") == 0) || (request.compareTo("false") == 0)) {
			result = false;
		}

		return result;
	}

	/**
	 * Sends mail to the user
	 * 
	 * @param locale
	 *            The locale to use
	 * @param course
	 *            The course the applicant has registered for
	 */
	private void sendMail(Locale locale, Course course, int event, Registration registration) {
		StringBuffer msg = MailUtil.createStandardBody(course, event, locale, messageSource);
		ArrayList<SimpleMailMessage> emails = MailUtil.setMailInfo(registration, event, course, msg, messageSource,
				locale);
		MailUtil.sendMails(emails, mailEngine);

//		msg
//				.append(getText("misc.hello", locale) + " " + registration.getFirstName() + " "
//						+ registration.getLastName());
//
//		// Only display this line if the employeenumber is given!
//		if (registration.getEmployeeNumber() != null) {
//			msg.append(StringUtil.ifEmpty(registration.getEmployeeNumber(), " ("
//					+ StringEscapeUtils.unescapeHtml(
//							messageSource.getMessage("registration.employeeNumber", null, locale)).toLowerCase() + " "
//					+ registration.getEmployeeNumber().intValue() + ")"));
//		}
//		msg.append("\n\n");
//
//		// Build mail
//		switch (event) {
//		case REGISTRATION_DELETED:
//			msg.append(StringEscapeUtils
//					.unescapeHtml(getText("registrationDeleted.mail.body", course.getName(), locale))
//					+ "\n\n");
//
//			break;
//
//		case REGISTRATION_MOVEDTOWAITINGLIST:
//			msg.append(StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.body", course.getName(),
//					locale))
//					+ "\n\n");
//
//			break;
//
//		case REGISTRATION_CONFIRMED:
//			msg.append(StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.body", course.getName(),
//					locale))
//					+ "\n\n");
//
//			break;
//		}
//
//		// Include course details
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.name", locale)) + ": " + course.getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.type", locale)) + ": " + course.getType() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.startTime", locale))
//				+ ": "
//				+ DateUtil.getDateTime(getText("date.format", locale) + " " + getText("time.format", locale), course
//						.getStartTime()) + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.stopTime", locale))
//				+ ": "
//				+ DateUtil.getDateTime(getText("date.format", locale) + " " + getText("time.format", locale), course
//						.getStopTime()) + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.duration", locale)) + ": " + course.getDuration()
//				+ "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.municipality", locale)) + ": "
//				+ course.getMunicipality().getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea", locale)) + ": "
//				+ course.getServiceArea().getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.location", locale)) + ": "
//				+ course.getLocation().getName() + "\n");
//
//		if (course.getResponsible() != null) {
//			msg.append(StringEscapeUtils.unescapeHtml(getText("course.responsible", locale)) + ": "
//					+ course.getResponsible().getName() + "\n");
//		}
//
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor", locale)) + ": "
//				+ course.getInstructor().getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.description", locale)) + ": "
//				+ course.getDescription() + "\n");
//
//		switch (event) {
//		case REGISTRATION_DELETED:
//			msg.append("\n" + StringEscapeUtils.unescapeHtml(getText("registrationDeleted.mail.footer", locale)));
//
//			break;
//
//		case REGISTRATION_MOVEDTOWAITINGLIST:
//			msg.append("\n"
//					+ StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.body", " "
//							+ course.getName(), locale)) + "\n\n");
//
//			break;
//
//		case REGISTRATION_CONFIRMED:
//			msg.append("\n"
//					+ StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.body", " " + course.getName(),
//							locale)) + "\n\n");
//
//			break;
//		}
//
//		ArrayList<String> recipients = new ArrayList<String>();
//
//		if (!StringUtils.isEmpty(registration.getEmail())) {
//			recipients.add(registration.getEmail());
//		}
//
//		if (recipients.size() > 0) {
//			String[] recipientArray = new String[recipients.size()];
//
//			for (int i = 0; i < recipients.size(); i++) {
//				recipientArray[i] = recipients.get(i);
//			}
//
//			message.setTo(recipientArray);
//		} else {
//			message.setTo(registration.getCourse().getInstructor().getEmail());
//		}
//
//		switch (event) {
//		case REGISTRATION_DELETED:
//			message.setSubject(StringEscapeUtils.unescapeHtml(getText("registrationDeleted.mail.subject", course
//					.getName(), locale)));
//
//			break;
//
//		case REGISTRATION_MOVEDTOWAITINGLIST:
//			message.setSubject(StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.subject", course
//					.getName(), locale)));
//
//			break;
//
//		case REGISTRATION_CONFIRMED:
//			message.setSubject(StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.subject", course
//					.getName(), locale)));
//
//			break;
//		}
//		msg.append("\n\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.contactinfo", locale)) + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.donotreply", getText("mail.default.from", locale),
//				locale))
//				+ "\n");
//
//		message.setText(msg.toString());
//		mailEngine.send(message);
	}
}

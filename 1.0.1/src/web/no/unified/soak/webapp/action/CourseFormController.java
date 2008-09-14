/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created 20. dec 2005
 */
package no.unified.soak.webapp.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.Constants;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.Attachment;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.AttachmentManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.MunicipalitiesManager;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.StringUtil;
import no.unified.soak.webapp.util.FileUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementation of SimpleFormController that interacts with the CourseManager
 * to retrieve/persist values to the database.
 * 
 * @author hrj
 */
public class CourseFormController extends BaseFormController {
	private final int COURSEDELETED = 1;

	private final int COURSECHANGED = 2;

	private CourseManager courseManager = null;

	private ServiceAreaManager serviceAreaManager = null;

	private PersonManager personManager = null;

	private LocationManager locationManager = null;

	private MunicipalitiesManager municipalitiesManager = null;

	private AttachmentManager attachmentManager = null;

	private RegistrationManager registrationManager = null;

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

	public void setCourseManager(CourseManager courseManager) {
		this.courseManager = courseManager;
	}

	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}

	public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
		this.serviceAreaManager = serviceAreaManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public void setMunicipalitiesManager(
			MunicipalitiesManager municipalitiesManager) {
		this.municipalitiesManager = municipalitiesManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String courseid = request.getParameter("id");

		// Retrieve all serviceareas into an array
		List serviceAreas = serviceAreaManager.getServiceAreas();
		if (serviceAreas != null) {
			model.put("serviceareas", serviceAreas);
		}

		// Retrieve all people into an array
		List people = personManager.getPersons(null, new Boolean(false));
		if (people != null) {
			model.put("instructors", people);
		}

		List responsibles = personManager.getEZResponsibles((EzUser) null);
		if (responsibles != null) {
			model.put("responsible", responsibles);
		}

		// Retrieve all locations into an array
		List locations = locationManager.getLocations(null, new Boolean(false));
		if (locations != null) {
			model.put("locations", locations);
		}

		// Retrieve all municipalities into an array
		List municipalities = municipalitiesManager.getAll();
		if (municipalities != null) {
			model.put("municipalities", municipalities);
		}

		// Current time
		List<Date> time = new ArrayList<Date>();
		time.add(new Date());
		model.put("time", time);
		
		// Are we to enable mail comment field and buttons?
		Boolean enableMail = new Boolean(false);
		String mailParam = request.getParameter("enableMail"); 
		if (mailParam != null && mailParam.compareToIgnoreCase("true") == 0) {
			enableMail = new Boolean(true);
		}
		model.put("enableMail", enableMail);

        // Check whether or not we allow registrations
		if ((courseid != null) && StringUtils.isNumeric(courseid)) {
			Course course = courseManager.getCourse(courseid);

			if (course != null) {
				Date today = new Date();
				Boolean allowRegistration = new Boolean(false);

				if (today.before(course.getRegisterBy())
						&& (course.getRegisterStart() == null || today
								.after(course.getRegisterStart()))) {
					allowRegistration = new Boolean(true);
				}

				model.put("allowRegistration", allowRegistration);

				// Retrieve all attachments belonging to the course into an
				// array
				if ((courseid != null) && StringUtils.isNumeric(courseid)
						&& StringUtils.isNotBlank(courseid)
						&& (Long.parseLong(courseid) != 0)) {
					List attachments = attachmentManager
							.getCourseAttachments(new Long(courseid));

					if (attachments != null) {
						model.put("attachments", attachments);
					}
				}
			}
		}

		return model;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		String copyid = request.getParameter("copyid");
		Course course = null;

		if (!StringUtils.isEmpty(id)) {
			course = courseManager.getCourse(id);
		} else if (!StringUtils.isEmpty(copyid)) {
			course = courseManager.getCourse(copyid);
			course.setId(null);
		} else {
			course = new Course();
	        // Check if a default municipality should be applied
	        Object omid = request.getAttribute(Constants.EZ_MUNICIPALITY);
	        if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
	            course.setMunicipalityid(new Long(omid.toString()));
	        }
		}

		return course;
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

		Map model = new HashMap();
		Course course = (Course) command;
		Long courseId = course.getId();

		boolean isNew = (course.getId() == null);
		Locale locale = request.getLocale();

		String mailComment = request.getParameter("mailcomment");

		// Are we to return to the course list?
		if (request.getParameter("return") != null) {
			if (log.isDebugEnabled()) {
				log.debug("recieved 'return' from jsp");
			}
			return new ModelAndView(getCancelView());
		} // or to delete the course?
		else if (request.getParameter("delete") != null) {
			// First (try to) delete the course
			courseManager.removeCourse(course.getId().toString());
			// Then send mail (in case the deletion was rejected)
			sendMail(locale, course, COURSEDELETED, mailComment);
			saveMessage(request, getText("course.deleted", locale));
			// Finally go to the course list
			return new ModelAndView(getCancelView());
		} // or to download files? 
		else if (request.getParameter("download") != null) {
			try {
				String attachmentId = request.getParameter("attachmentid");

				if ((attachmentId != null)
						&& StringUtils.isNumeric(attachmentId)
						&& (new Integer(attachmentId).intValue() != 0)) {
					Attachment attachment = attachmentManager
							.getAttachment(new Long(attachmentId));
					String filename = getServletContext().getRealPath(
							"/resources")
							+ "/" + attachment.getStoredname();

					FileUtil.downloadFile(request, response, attachment
							.getContentType(), filename, attachment
							.getFilename());
				}
			} catch (FileNotFoundException fnfe) {
				log.error(fnfe);

				String key = "attachment.sendError";
				errors.reject(getText(key, request.getLocale()));

				return showForm(request, response, errors);
			} catch (IOException ioe) {
				log.error(ioe);

				String key = "errors.ioerror";
				errors.reject(getText(key, request.getLocale()));

				return showForm(request, response, errors);
			}
		} // or to send out notification email?
		else if (request.getParameter("send") != null) {
			sendMail(locale, course, COURSECHANGED, mailComment);

		} // or to save/update?
		else {
			// Parse date and time fields together
			String format = getText("date.format", request.getLocale()) + " "
					+ getText("time.format", request.getLocale());
			String formatLocalized = getText("date.format.localized", request
					.getLocale())
					+ " "
					+ getText("time.format.localized", request.getLocale());
			Object[] args = null;

			try {
				Date time = parseDateAndTime(request, "startTime", format);

				if (time != null) {
					course.setStartTime(time);
				} else {
					throw new BindException(course, "startTime");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.startTime", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("startTime", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				Date time = parseDateAndTime(request, "stopTime", format);

				if (time != null) {
					course.setStopTime(time);
				} else {
					throw new BindException(course, "stopTime");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.stopTime", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("stopTime", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				course.setRegisterStart(parseDateAndTime(request,
						"registerStart", format));
			} catch (Exception e) {
				args = new Object[] {
						getText("course.registerStart", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("registerStart", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				course
						.setReminder(parseDateAndTime(request, "reminder",
								format));
			} catch (Exception e) {
				args = new Object[] {
						getText("course.reminder", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("reminder", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				Date time = parseDateAndTime(request, "registerBy", format);

				if (time != null) {
					course.setRegisterBy(time);
				} else {
					throw new BindException(course, "registerBy");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.registerBy", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("registerBy", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				Date time = parseDateAndTime(request, "freezeAttendance",
						format);

				if (time != null) {
					course.setFreezeAttendance(time);
				} else {
					throw new BindException(course, "freezeAttendance");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.freezeAttendance", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("freezeAttendance", "errors.dateformat",
						args, "Invalid date or time");
			}

			if (args != null) {
				return showForm(request, response, errors);
			}

			courseManager.saveCourse(course);

			String key = (isNew) ? "course.added" : "course.updated";
			saveMessage(request, getText(key, locale));

			// If not new, we need to send out a message to everyone registered
			// to the course that things have changed
			if (isNew) {
				model.put("enablemail", "false");
				courseId = course.getId();
			} else {
				List<Registration> registrations = registrationManager
					.getSpecificRegistrations(course.getId(), null, null, null, null, null);
				if (registrations.isEmpty()) {
					model.put("enablemail", "false");
				} else {
					model.put("enablemail", "true");
				}
			}
		}

		// Let the next page know what course we were editing here
		model.put("id", courseId.toString());

		return new ModelAndView(getSuccessView(), model);
	}

	private Date parseDateAndTime(HttpServletRequest request, String fieldName,
			String format) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(format);
		String date = request.getParameter(fieldName + "Date");
		String time = request.getParameter(fieldName + "Time");

		if (StringUtils.isEmpty(date)) {
			return null;
		} else if (StringUtils.isEmpty(time)) {
			return (Date) formatter.parse(date + " 00:00");
		} else {
			return (Date) formatter.parse(date + " " + time + ":00");
		}
	}

	/**
	 * Sends mail to the user
	 * 
	 * @param locale
	 *            The locale to use
	 * @param course
	 *            The course the applicant has registered for
	 */
	private void sendMail(Locale locale, Course course, int event, String mailComment) {

		List<Registration> registrations = registrationManager
				.getSpecificRegistrations(course.getId(), null, null, null,
						null, null);

		String registeredMsg = StringEscapeUtils.unescapeHtml(getText(
				"courseNotification.phrase.registered", locale));
		String waitingMsg = StringEscapeUtils.unescapeHtml(getText(
				"courseNotification.phrase.waitinglist", locale));
		
		StringBuffer msg = new StringBuffer();

		// Build mail
		switch (event) {
		case COURSECHANGED:
			msg
					.append(StringEscapeUtils.unescapeHtml(getText(
							"courseChanged.mail.body", " " + course.getName(),
							locale)));

			break;

		case COURSEDELETED:
			msg
					.append(StringEscapeUtils.unescapeHtml(getText(
							"courseDeleted.mail.body", " " + course.getName(),
							locale)));

			break;
		}

		msg.append("\n\n");

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
				"course.municipality", locale))
				+ ": " + course.getMunicipality().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea",
				locale))
				+ ": " + course.getServiceArea().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.location",
				locale))
				+ ": " + course.getLocation().getName() + "\n");

		if (course.getResponsible() != null) {
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"course.responsible", locale))
					+ ": " + course.getResponsible().getName() + "\n");
		}

		msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor",
				locale))
				+ ": " + course.getInstructor().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.description",
				locale))
				+ ": " + course.getDescription() + "\n");

		// Include user defined comment if specified
		if (mailComment != null && StringUtils.isNotBlank(mailComment)) {
			msg.append("\n");
			msg.append(mailComment);
			msg.append("\n");
		}

		// We cannot link to a deleted course, so the link is only displayed if
		// the course still exists
		if (event == COURSECHANGED) {
			String baseurl = StringEscapeUtils.unescapeHtml(getText(
					"javaapp.baseurl", locale));
			String coursedetailurl = StringEscapeUtils.unescapeHtml(getText(
					"javaapp.coursedetailurl", String.valueOf(course.getId()),
					locale));
			msg.append("\n\n");
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"javaapp.findurlhere", locale))
					+ " " + baseurl + coursedetailurl);
		}

		msg.append("\n\n");

		switch (event) {
		case COURSECHANGED:
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"courseChanged.mail.footer", locale)));

			break;

		case COURSEDELETED:
			msg.append(StringEscapeUtils.unescapeHtml(getText(
					"courseDeleted.mail.body", " " + course.getName() + "\n\n",
					locale)));

			break;
		}

		msg.append("\n\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.contactinfo",
				locale))
				+ "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.donotreply",
				getText("mail.default.from", locale), locale))
				+ "\n");

		for (Registration registration : registrations) {
			switch (event) {
			case COURSECHANGED:
				if (registration.getReserved()) {
					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
						"courseChanged.mail.subject", course.getName(), 
						locale)).replaceAll("<registeredfor/>", registeredMsg));
				} else {
					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
							"courseChanged.mail.subject", course.getName(), 
							locale)).replaceAll("<registeredfor/>", waitingMsg));
				}
				break;

			case COURSEDELETED:
				if (registration.getReserved()) {
					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
						"courseDeleted.mail.subject", course.getName(), 
						locale)).replaceAll("<registeredfor/>", registeredMsg));
				} else {
					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
							"courseDeleted.mail.subject", course.getName(), 
							locale)).replaceAll("<registeredfor/>", waitingMsg));
				}
				
				break;
			}

			String custom = msg.toString();
			if (registration.getReserved()) {
				custom.replaceAll("<registeredfor/>", registeredMsg);
			} else {
				custom.replaceAll("<registeredfor/>", waitingMsg);
			}
			
			StringBuffer msgIndivid = new StringBuffer(custom);
			msgIndivid.insert(0, "\n\n");
			
			String employeeNoText = messageSource.getMessage("registration.employeeNumber",
					null, locale);
			if (!StringUtils.isEmpty(employeeNoText))
				employeeNoText = employeeNoText.toLowerCase();
					
			if (registration.getEmployeeNumber() != null)
			{
				String ansattParentes = " ("
						+ StringEscapeUtils.unescapeHtml(
								employeeNoText) + " "
						+ registration.getEmployeeNumber().intValue() + ")";
				
	
				msgIndivid.insert(0, StringUtil.ifEmpty(registration
						.getEmployeeNumber(), ansattParentes));
			}
			
			msgIndivid.insert(0, getText("misc.hello", locale) + " "
					+ registration.getFirstName() + " "
					+ registration.getLastName());

			message.setText(msgIndivid.toString());

			List<String> emails = new LinkedList<String>();
			if (registration.getEmail() != null
					&& registration.getEmail().trim().length() > 0) {
				emails.add(registration.getEmail());
			}
			if (emails.size() == 0 && course.getInstructor().getEmail() != null) {
				emails.add(course.getInstructor().getEmail());
			}

			message.setTo(StringUtil.list2Array(emails));
			mailEngine.send(message);
		}
	}

	/**
	 * @param registrationManager
	 *            The registrationManager to set.
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}
}
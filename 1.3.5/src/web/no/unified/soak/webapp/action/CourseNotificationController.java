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
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.MailUtil;
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
 * @author ceg
 */
public class CourseNotificationController extends BaseFormController {
	
	private CourseManager courseManager = null;

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

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String courseid = request.getParameter("id");
		model.put("id", courseid);

		// Are we to enable mail comment field and buttons?
		Boolean enableMail = new Boolean(false);
		String mailParam = request.getParameter("enablemail"); 
		if ((mailParam != null) && (mailParam.compareToIgnoreCase("true") == 0)) {
			enableMail = new Boolean(true);
		}
		model.put("enableMail", enableMail);

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
	        // Check if a default organization should be applied
	        Object omid = request.getAttribute(Constants.EZ_ORGANIZATION);
	        if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
	            course.setOrganizationid(new Long(omid.toString()));
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

		Course course = (Course) command;

		Locale locale = request.getLocale();

		String mailComment = request.getParameter("mailcomment");

		// Are we to return to the course list?
		if (request.getParameter("return") != null) {
			if (log.isDebugEnabled()) {
				log.debug("recieved 'return' from jsp");
			}
			return new ModelAndView(getCancelView(), "id", course.getId().toString());
		} // or to send out notification email?
		else if (request.getParameter("send") != null) {
			sendMail(locale, course, Constants.EMAIL_EVENT_COURSECHANGED, mailComment);
		}

		return new ModelAndView(getSuccessView());
	}

	/**
	 * Sends mail to the user
	 * 
	 * @param locale
	 *            The locale to use
	 * @param course
	 *            The course the applicant has registered for
	 */
	protected void sendMail(Locale locale, Course course, int event, String mailComment) {
		log.debug("Sending mail from CourseNotificationController");
		List<Registration> registrations = registrationManager
				.getSpecificRegistrations(course.getId(), null, null, null,
						null, null, null);
		
		StringBuffer msg = MailUtil.createStandardBody(course, event, locale, messageSource, mailComment);
		ArrayList<SimpleMailMessage> emails = MailUtil.setMailInfo(registrations, event, course, msg, messageSource, locale);
		MailUtil.sendMails(emails, mailEngine);
		
//		String registeredMsg = StringEscapeUtils.unescapeHtml(getText(
//				"courseNotification.phrase.registered", locale));
//		String waitingMsg = StringEscapeUtils.unescapeHtml(getText(
//				"courseNotification.phrase.waitinglist", locale));
//		
//		StringBuffer msg = new StringBuffer();
//
//		// Build mail
//		switch (event) {
//		case COURSECHANGED:
//			msg
//					.append(StringEscapeUtils.unescapeHtml(getText(
//							"courseChanged.mail.body", " " + course.getName(),
//							locale)));
//
//			break;
//
//		case COURSEDELETED:
//			msg
//					.append(StringEscapeUtils.unescapeHtml(getText(
//							"courseDeleted.mail.body", " " + course.getName(),
//							locale)));
//
//			break;
//		}
//
//		msg.append("\n\n");
//
//		// Include course details
//		msg.append(StringEscapeUtils
//				.unescapeHtml(getText("course.name", locale))
//				+ ": " + course.getName() + "\n");
//		msg.append(StringEscapeUtils
//				.unescapeHtml(getText("course.type", locale))
//				+ ": " + course.getType() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.startTime",
//				locale))
//				+ ": "
//				+ DateUtil
//						.getDateTime(getText("date.format", locale) + " "
//								+ getText("time.format", locale), course
//								.getStartTime()) + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.stopTime",
//				locale))
//				+ ": "
//				+ DateUtil.getDateTime(getText("date.format", locale) + " "
//						+ getText("time.format", locale), course.getStopTime())
//				+ "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.duration",
//				locale))
//				+ ": " + course.getDuration() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText(
//				"course.organization", locale))
//				+ ": " + course.getOrganization().getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea",
//				locale))
//				+ ": " + course.getServiceArea().getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.location",
//				locale))
//				+ ": " + course.getLocation().getName() + "\n");
//
//		if (course.getResponsible() != null) {
//			msg.append(StringEscapeUtils.unescapeHtml(getText(
//					"course.responsible", locale))
//					+ ": " + course.getResponsible().getName() + "\n");
//		}
//
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor",
//				locale))
//				+ ": " + course.getInstructor().getName() + "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("course.description",
//				locale))
//				+ ": " + course.getDescription() + "\n");
//
//		// Include user defined comment if specified
//		if (mailComment != null && StringUtils.isNotBlank(mailComment)) {
//			msg.append("\n");
//			msg.append(mailComment);
//			msg.append("\n");
//		}
//
//		// We cannot link to a deleted course, so the link is only displayed if
//		// the course still exists
//		if (event == COURSECHANGED) {
//			String baseurl = StringEscapeUtils.unescapeHtml(getText(
//					"javaapp.baseurl", locale));
//			String coursedetailurl = StringEscapeUtils.unescapeHtml(getText(
//					"javaapp.coursedetailurl", String.valueOf(course.getId()),
//					locale));
//			msg.append("\n\n");
//			msg.append(StringEscapeUtils.unescapeHtml(getText(
//					"javaapp.findurlhere", locale))
//					+ " " + baseurl + coursedetailurl);
//		}
//
//		msg.append("\n\n");
//
//		switch (event) {
//		case COURSECHANGED:
//			msg.append(StringEscapeUtils.unescapeHtml(getText(
//					"courseChanged.mail.footer", locale)));
//
//			break;
//
//		case COURSEDELETED:
//			msg.append(StringEscapeUtils.unescapeHtml(getText(
//					"courseDeleted.mail.body", " " + course.getName() + "\n\n",
//					locale)));
//
//			break;
//		}
//
//		msg.append("\n\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.contactinfo",
//				locale))
//				+ "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.donotreply",
//				getText("mail.default.from", locale), locale))
//				+ "\n");
//
//		for (Registration registration : registrations) {
//			switch (event) {
//			case COURSECHANGED:
//				if (registration.getReserved()) {
//					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
//						"courseChanged.mail.subject", course.getName(), 
//						locale)).replaceAll("<registeredfor/>", registeredMsg));
//				} else {
//					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
//							"courseChanged.mail.subject", course.getName(), 
//							locale)).replaceAll("<registeredfor/>", waitingMsg));
//				}
//				break;
//
//			case COURSEDELETED:
//				if (registration.getReserved()) {
//					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
//						"courseDeleted.mail.subject", course.getName(), 
//						locale)).replaceAll("<registeredfor/>", registeredMsg));
//				} else {
//					message.setSubject(StringEscapeUtils.unescapeHtml(getText(
//							"courseDeleted.mail.subject", course.getName(), 
//							locale)).replaceAll("<registeredfor/>", waitingMsg));
//				}
//				
//				break;
//			}
//
//			String custom = msg.toString();
//			if (registration.getReserved()) {
//				custom.replaceAll("<registeredfor/>", registeredMsg);
//			} else {
//				custom.replaceAll("<registeredfor/>", waitingMsg);
//			}
//			
//			StringBuffer msgIndivid = new StringBuffer(custom);
//			msgIndivid.insert(0, "\n\n");
//			
//			String employeeNoText = messageSource.getMessage("registration.employeeNumber",
//					null, locale);
//			if (!StringUtils.isEmpty(employeeNoText))
//				employeeNoText = employeeNoText.toLowerCase();
//					
//			if (registration.getEmployeeNumber() != null)
//			{
//				String ansattParentes = " ("
//						+ StringEscapeUtils.unescapeHtml(
//								employeeNoText) + " "
//						+ registration.getEmployeeNumber().intValue() + ")";
//				
//	
//				msgIndivid.insert(0, StringUtil.ifEmpty(registration
//						.getEmployeeNumber(), ansattParentes));
//			}
//			
//			msgIndivid.insert(0, getText("misc.hello", locale) + " "
//					+ registration.getFirstName() + " "
//					+ registration.getLastName());
//
//			message.setText(msgIndivid.toString());
//
//			List<String> emails = new LinkedList<String>();
//			if (registration.getEmail() != null
//					&& registration.getEmail().trim().length() > 0) {
//				emails.add(registration.getEmail());
//			}
//			if (emails.size() == 0 && course.getInstructor().getEmail() != null) {
//				emails.add(course.getInstructor().getEmail());
//			}
//
//			message.setTo(StringUtil.list2Array(emails));
//			mailEngine.send(message);
//		}
	}

	/**
	 * @param registrationManager
	 *            The registrationManager to set.
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}
}

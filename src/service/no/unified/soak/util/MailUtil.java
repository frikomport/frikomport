package no.unified.soak.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.MailEngine;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;

public class MailUtil {
	protected static final Log log = LogFactory.getLog(MailUtil.class);

	public static void sendMails(ArrayList<SimpleMailMessage> Emails, MailEngine engine) {
		if (Emails != null) {
			for (int i = 0; i < Emails.size(); i++) {
				SimpleMailMessage theEmail = Emails.get(i);
				log.debug("Sent mail to: " + theEmail.getTo());
				engine.send(theEmail);
			}
		}
	}

	/**
	 * Creates a standard mail body containing all the details of a course and some information on the event that
	 * triggered the mail to be sent.
	 * 
	 * @param course
	 *            The course in question
	 * @param event
	 *            The event that triggered the sending of this mail
	 * @param locale
	 *            The language to fetch messages in
	 * @param messageSource
	 *            The source for our messages.
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createStandardBody(Course course, int event, Locale locale, MessageSource messageSource) {
		return createStandardBody(course, event, locale, messageSource, null, false);
	}

	/**
	 * Creates a standard mail body containing all the details of a course and some information on the event that
	 * triggered the mail to be sent.
	 * 
	 * @param course
	 *            The course in question
	 * @param event
	 *            The event that triggered the sending of this mail
	 * @param locale
	 *            The language to fetch messages in
	 * @param messageSource
	 *            The source for our messages.
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createStandardBody(Course course, int event, Locale locale, MessageSource messageSource,
			String mailComment) {
		return createStandardBody(course, event, locale, messageSource, mailComment, false);
	}

	/**
	 * Creates a standard mail body containing all the details of a course and some information on the event that
	 * triggered the mail to be sent.
	 * 
	 * @param course
	 *            The course in question
	 * @param event
	 *            The event that triggered the sending of this mail
	 * @param locale
	 *            The language to fetch messages in
	 * @param messageSource
	 *            The source for our messages.
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createStandardBody(Course course, int event, Locale locale, MessageSource messageSource,
			boolean reservationConfirmed) {
		return createStandardBody(course, event, locale, messageSource, null, reservationConfirmed);
	}

	/**
	 * Creates a standard mail body containing all the details of a course and some information on the event that
	 * triggered the mail to be sent.
	 * 
	 * @param course
	 *            The course in question
	 * @param event
	 *            The event that triggered the sending of this mail
	 * @param locale
	 *            The language to fetch messages in
	 * @param messageSource
	 *            The source for our messages.
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createStandardBody(Course course, int event, Locale locale, MessageSource messageSource,
			String mailComment, boolean reservationConfirmed) {
		StringBuffer msg = new StringBuffer();
		
        // Include user defined comment if specified
        if (mailComment != null && StringUtils.isNotBlank(mailComment)) {
            msg.append("\n");
            msg.append(mailComment);
            msg.append("\n\n");
        }

		// Build mail
		switch (event) {
		case Constants.EMAIL_EVENT_COURSECHANGED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseChanged.mail.body", " " + course.getName(),
					locale, messageSource)));
			break;
		case Constants.EMAIL_EVENT_COURSEDELETED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseDeleted.mail.body", " " + course.getName(),
					locale, messageSource)));
		case Constants.EMAIL_EVENT_NOTIFICATION:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseNotification.mail.body", " " + course.getName(),
					locale, messageSource)));

			break;
		case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
			// TODO: Test - Is this right? Previously: getText("registrationComplete.mail.body", null, locale,
			// messageSource)));
			msg
					.append(StringEscapeUtils.unescapeHtml(getText("registrationComplete.mail.body", locale,
							messageSource)));
			break;
		case Constants.EMAIL_EVENT_REGISTRATION_DELETED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("registrationDeleted.mail.body", course.getName(),
					locale, messageSource))
					+ "\n\n");

			break;

		case Constants.EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST:
			msg.append(StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.body", course.getName(),
					locale, messageSource))
					+ "\n\n");

			break;

		case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.body", course.getName(),
					locale, messageSource))
					+ "\n\n");

			break;
		}
		msg.append("\n\n");
		
		// Include all the course details
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.name", locale, messageSource)) + ": "
				+ course.getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.type", locale, messageSource)) + ": "
				+ course.getType() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.startTime", locale, messageSource))
				+ ": "
				+ DateUtil.getDateTime(getText("date.format", locale, messageSource) + " "
						+ getText("time.format", locale, messageSource), course.getStartTime()) + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.stopTime", locale, messageSource))
				+ ": "
				+ DateUtil.getDateTime(getText("date.format", locale, messageSource) + " "
						+ getText("time.format", locale, messageSource), course.getStopTime()) + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.duration", locale, messageSource)) + ": "
				+ course.getDuration() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.municipality", locale, messageSource)) + ": "
				+ course.getMunicipality().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea", locale, messageSource)) + ": "
				+ course.getServiceArea().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.location", locale, messageSource)) + ": "
				+ course.getLocation().getName() + "\n");

		if (course.getResponsible() != null) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.responsible", locale, messageSource)) + ": "
					+ course.getResponsible().getName() + "\n");
		}

		msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor", locale, messageSource)) + ": "
				+ course.getInstructor().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.description", locale, messageSource)) + ": "
				+ course.getDescription() + "\n");

		// We cannot link to a deleted course, so the link is only displayed if
		// the course still exists
		if (event != Constants.EMAIL_EVENT_COURSEDELETED) {
			String baseurl = StringEscapeUtils.unescapeHtml(getText("javaapp.baseurl", locale, messageSource));
			String coursedetailurl = StringEscapeUtils.unescapeHtml(getText("javaapp.coursedetailurl", String
					.valueOf(course.getId()), locale, messageSource));
			msg.append("\n\n");
			msg.append(StringEscapeUtils.unescapeHtml(getText("javaapp.findurlhere", locale, messageSource)) + " "
					+ baseurl + coursedetailurl);
		}

		msg.append("\n\n");

		switch (event) {
		case Constants.EMAIL_EVENT_COURSECHANGED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseChanged.mail.footer", locale, messageSource)));

			break;

		case Constants.EMAIL_EVENT_COURSEDELETED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseDeleted.mail.body", " " + course.getName()
					+ "\n\n", locale, messageSource)));

			break;
		case Constants.EMAIL_EVENT_NOTIFICATION:
			msg
					.append(StringEscapeUtils.unescapeHtml(getText("courseNotification.mail.footer", locale,
							messageSource)));
			break;
		case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
			// TODO: Test if right. Previously: "registrationComplete.mail.footer", null,locale)
			if (reservationConfirmed) {
				msg.append(StringEscapeUtils.unescapeHtml(getText("registrationComplete.mail.footer", locale,
						messageSource)));
			} else {
				msg.append(StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.footer", " "
						+ course.getName() + "\n\n", locale, messageSource)));
			}
			break;
		case Constants.EMAIL_EVENT_REGISTRATION_DELETED:
			msg
					.append("\n"
							+ StringEscapeUtils.unescapeHtml(getText("registrationDeleted.mail.footer", locale,
									messageSource)));

			break;

		case Constants.EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST:
			msg.append("\n"
					+ StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.body", " "
							+ course.getName(), locale, messageSource)) + "\n\n");

			break;

		case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
			msg.append("\n"
					+ StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.body", " " + course.getName(),
							locale, messageSource)) + "\n\n");

			break;
		}

		msg.append("\n\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.contactinfo", locale, messageSource)) + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.donotreply", getText("mail.default.from", locale,
				messageSource), locale, messageSource))
				+ "\n");

		return msg;
	}

	/**
	 * Sets the subject, recipient, sender etc. to a message object
	 * 
	 * @param registration
	 *            The registration to be handled
	 * @param event
	 *            The event that triggered the sending of this mail: Is a course deleted, changed, is this a
	 *            notification mail?
	 * @param course
	 *            the course in question
	 * @param msg
	 *            the body message
	 * @param messageSource
	 *            the source for our message
	 * @param message
	 *            the mail object
	 * @param locale
	 *            The language to fetch messages in
	 */
	public static ArrayList<SimpleMailMessage> setMailInfo(Registration registration, int event, Course course,
			StringBuffer msg, MessageSource messageSource, Locale locale) {
		List<Registration> theRegistration = new ArrayList<Registration>();
		theRegistration.add(registration);
		return setMailInfo(theRegistration, event, course, msg, messageSource, locale);
	}

	/**
	 * Sets the subject, recipient, sender etc. to a message object
	 * 
	 * @param registrations
	 *            All the registrations that are to be handled
	 * @param event
	 *            The event that triggered the sending of this mail: Is a course deleted, changed, is this a
	 *            notification mail?
	 * @param course
	 *            the course in question
	 * @param msg
	 *            the body message
	 * @param messageSource
	 *            the source for our message
	 * @param message
	 *            the mail object
	 * @param locale
	 *            The language to fetch messages in
	 */
	public static ArrayList<SimpleMailMessage> setMailInfo(List<Registration> registrations, int event, Course course,
			StringBuffer msg, MessageSource messageSource, Locale locale) {
		ArrayList<SimpleMailMessage> allEMails = new ArrayList<SimpleMailMessage>();
		String registeredMsg = StringEscapeUtils.unescapeHtml(getText("courseNotification.phrase.registered", locale,
				messageSource));
		String waitingMsg = StringEscapeUtils.unescapeHtml(getText("courseNotification.phrase.waitinglist", locale,
				messageSource));
		for (Registration registration : registrations) {
			SimpleMailMessage message = new SimpleMailMessage();
			switch (event) {
			case Constants.EMAIL_EVENT_COURSECHANGED:
				if (registration.getReserved()) {
					message.setSubject(StringEscapeUtils.unescapeHtml(
							getText("courseChanged.mail.subject", course.getName(), locale, messageSource)).replaceAll(
							"<registeredfor/>", registeredMsg).replaceAll("<coursename/>", course.getName()));
				} else {
					message.setSubject(StringEscapeUtils.unescapeHtml(
							getText("courseChanged.mail.subject", course.getName(), locale, messageSource)).replaceAll(
							"<registeredfor/>", waitingMsg).replaceAll("<coursename/>", course.getName()));
				}
				break;

			case Constants.EMAIL_EVENT_COURSEDELETED:
				if (registration.getReserved()) {
					message.setSubject(StringEscapeUtils.unescapeHtml(
							getText("courseDeleted.mail.subject", course.getName(), locale, messageSource)).replaceAll(
							"<registeredfor/>", registeredMsg).replaceAll("<coursename/>", course.getName()));
				} else {
					message.setSubject(StringEscapeUtils.unescapeHtml(
							getText("courseDeleted.mail.subject", course.getName(), locale, messageSource)).replaceAll(
							"<registeredfor/>", waitingMsg).replaceAll("<coursename/>", course.getName()));
				}

				break;
			case Constants.EMAIL_EVENT_NOTIFICATION:
				if (registration.getReserved()) {
					message.setSubject(StringEscapeUtils.unescapeHtml(
							getText("courseNotification.mail.subject", course.getName(), locale, messageSource))
							.replaceAll("<registeredfor/>", registeredMsg).replaceAll("<coursename/>", course.getName()));
				} else {
					message.setSubject(StringEscapeUtils.unescapeHtml(
							getText("courseNotification.mail.subject", course.getName(), locale, messageSource))
							.replaceAll("<registeredfor/>", waitingMsg).replaceAll("<coursename/>", course.getName()));
				}
				break;
			case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
				if (registration.getReserved()) {
					message.setSubject(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
							"registrationComplete.mail.subject", new String[] { registration.getCourse().getName() },
							locale)));
				} else {
					message.setSubject(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
							"registrationToWaitinglist.mail.subject",
							new String[] { registration.getCourse().getName() }, locale)));
				}
				break;
			case Constants.EMAIL_EVENT_REGISTRATION_DELETED:
				message.setSubject(StringEscapeUtils.unescapeHtml(getText("registrationDeleted.mail.subject", course
						.getName(), locale, messageSource)));

				break;

			case Constants.EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST:
				message.setSubject(StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.subject",
						course.getName(), locale, messageSource)));

				break;

			case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
				message.setSubject(StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.subject", course
						.getName(), locale, messageSource)));

				break;
			}

			String custom = msg.toString();
			if (registration.getReserved()) {
				custom.replaceAll("<registeredfor/>", registeredMsg).replaceAll("<coursename/>", course.getName());
			} else {
				custom.replaceAll("<registeredfor/>", waitingMsg).replaceAll("<coursename/>", course.getName());
			}

			StringBuffer msgIndivid = new StringBuffer(custom);
			msgIndivid.insert(0, "\n\n");

			String employeeNoText = messageSource.getMessage("registration.employeeNumber", null, locale);
			if (!StringUtils.isEmpty(employeeNoText))
				employeeNoText = employeeNoText.toLowerCase();

			if (registration.getEmployeeNumber() != null) {
				String ansattParentes = " (" + StringEscapeUtils.unescapeHtml(employeeNoText) + " "
						+ registration.getEmployeeNumber().intValue() + ")";

				msgIndivid.insert(0, StringUtil.ifEmpty(registration.getEmployeeNumber(), ansattParentes));
			}

			msgIndivid.insert(0, getText("misc.hello", locale, messageSource) + " " + registration.getFirstName() + " "
					+ registration.getLastName());

			message.setText(msgIndivid.toString());
			List<String> emails = new LinkedList<String>();

			if (registration.getEmail() != null && registration.getEmail().trim().length() > 0) {
				emails.add(registration.getEmail());
			}

			if (emails.size() == 0 && course.getInstructor().getEmail() != null) {
				emails.add(course.getInstructor().getEmail());
			}

			// SimpleMailMessage copy = new SimpleMailMessage();
			// message.copyTo(copy);
			message.setFrom(StringEscapeUtils.unescapeHtml(getText("mail.default.from", locale, messageSource)));
			message.setTo(StringUtil.list2Array(emails));
			log.debug("The mail is to: " + emails);
			allEMails.add(message);
		}
		return allEMails;
	}

	/**
	 * Method for getting a key's value (with i18n support). Calling getMessageSourceAccessor() is used because the
	 * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 *            The key to the message
	 * @param locale
	 *            the current locale
	 * @param messageSource
	 *            The source of our messages
	 */
	public static String getText(String msgKey, Locale locale, MessageSource messageSource) {
		String result = "";
		try {
			result = messageSource.getMessage(msgKey, new String[] {}, locale);
		} catch (Exception e) {
			// TODO Handle exception
		}
		return result;
	}

	/**
	 * Method for getting a key's value (with i18n support). Calling getMessageSourceAccessor() is used because the
	 * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 *            The key to the message
	 * @param arg0
	 *            Text to insert into the message
	 * @param locale
	 *            the current locale
	 * @param messageSource
	 *            The source of our messages
	 */
	public static String getText(String msgKey, String arg0, Locale locale, MessageSource messageSource) {
		String result = "";
		try {
			result = messageSource.getMessage(msgKey, new String[] { arg0 }, locale);
		} catch (Exception e) {
			// TODO Handle exception
		}
		return result;
	}

	/**
	 * Method for getting a key's value (with i18n support). Calling getMessageSourceAccessor() is used because the
	 * RequestContext variable is not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 *            The key to the message
	 * @param args
	 *            An arbitrary number of arguments to be inserted into the retrieved message
	 * @param locale
	 *            the current locale
	 * @param messageSource
	 *            The source of our messages
	 */
	public String getText(String msgKey, Object[] args, Locale locale, MessageSource messageSource) {
		String result = "";
		try {
			result = messageSource.getMessage(msgKey, args, locale);
		} catch (Exception e) {
			// TODO Handle exception
		}
		return result;
	}

}

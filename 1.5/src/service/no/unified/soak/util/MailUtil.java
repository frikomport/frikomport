package no.unified.soak.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.net.URI;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.MailEngine;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.ByteArrayResource;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.util.Uris;

public class MailUtil {
	// public static final Log log = LogFactory.getLog(MailUtil.class);

	// public static void sendMails(ArrayList<SimpleMailMessage> Emails,
	// MailEngine
	// engine) {
	// Log log = LogFactory.getLog(MailUtil.class.toString());
	// if (Emails != null) {
	// for (int i = 0; i < Emails.size(); i++) {
	// SimpleMailMessage theEmail = Emails.get(i);
	// log.debug("Sent mail to: " + theEmail.getTo());
	// engine.send(theEmail);
	// }
	// }
	// }

	public static void sendMimeMails(ArrayList<MimeMessage> Emails, MailEngine engine) {
		if (Emails != null) {
			for (int i = 0; i < Emails.size(); i++) {
				MimeMessage theEmail = Emails.get(i);
				engine.send(theEmail);
			}
		}
	}

	/**
	 * Creates a standard mail body containing all the details of a course and
	 * some information on the event that triggered the mail to be sent.
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
	 * Creates a standard mail body containing all the details of a course and
	 * some information on the event that triggered the mail to be sent.
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
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createStandardBody(Course course, int event, Locale locale, MessageSource messageSource,
			String mailComment) {
		return createStandardBody(course, event, locale, messageSource, mailComment, false);
	}

	/**
	 * Creates a changed mail body containing all the details of a course and
	 * some information on the event that triggered the mail to be sent.
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
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createChangedBody(Course course, int event, Locale locale, MessageSource messageSource,
			String mailComment, List<String> changedList) {
		return createChangedBody(course, event, locale, messageSource, mailComment, changedList, false);
	}

	/**
	 * Creates a standard mail body containing all the details of a course and
	 * some information on the event that triggered the mail to be sent.
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
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createStandardBody(Course course, int event, Locale locale, MessageSource messageSource,
			boolean reservationConfirmed) {
		return createStandardBody(course, event, locale, messageSource, null, reservationConfirmed);
	}

	/**
	 * Creates a standard mail body containing all the details of a course and
	 * some information on the event that triggered the mail to be sent.
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
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
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
		case Constants.EMAIL_EVENT_COURSECANCELLED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseCancelled.mail.body", " " + course.getName(),
					locale, messageSource)));
			break;
		case Constants.EMAIL_EVENT_COURSEDELETED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseDeleted.mail.body", " " + course.getName(),
					locale, messageSource)));
			break;
		case Constants.EMAIL_EVENT_NOTIFICATION:
            if(reservationConfirmed){
                msg.append(StringEscapeUtils.unescapeHtml(getText("courseNotification.mail.body.reserved", locale, messageSource)));
            }
            else{
                msg.append(StringEscapeUtils.unescapeHtml(getText("courseNotification.mail.body.waitinglist", locale, messageSource)));
            }
            break;
		case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
			// TODO: Test - Is this right? Previously:
			// getText("registrationComplete.mail.body", null, locale,
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
		case Constants.EMAIL_EVENT_NEW_COURSE_NOTIFICATION:
			msg.append(StringEscapeUtils.unescapeHtml(getText("registrationNewCourse.mail.body", locale, messageSource))
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
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.organization", locale, messageSource)) + ": "
				+ course.getOrganization().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea", locale, messageSource)) + ": "
				+ course.getServiceArea().getName() + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("course.location", locale, messageSource)) + ": "
				+ course.getLocation().getName() + "\n");

		if (course.getResponsible() != null) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.responsible", locale, messageSource)) + ": "
					+ course.getResponsible().getFullName() + ", mailto:" + course.getResponsible().getEmail() + "\n");
		}

		msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor", locale, messageSource)) + ": "
				+ course.getInstructor().getName() + ", mailto:" + course.getInstructor().getEmail() + "\n");
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
		case Constants.EMAIL_EVENT_COURSECANCELLED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseCancelled.mail.body", " " + course.getName()
					+ "\n\n", locale, messageSource)));

			break;

		case Constants.EMAIL_EVENT_COURSEDELETED:
			msg.append(StringEscapeUtils.unescapeHtml(getText("courseDeleted.mail.body", " " + course.getName()
					+ "\n\n", locale, messageSource)));

			break;
		case Constants.EMAIL_EVENT_NOTIFICATION:
            if(reservationConfirmed){
                msg.append(StringEscapeUtils.unescapeHtml(getText("courseNotification.mail.footer.registered", locale,
							messageSource)));
            } else {
                msg.append(StringEscapeUtils.unescapeHtml(getText("courseNotification.mail.footer.waitinglist", locale,
							messageSource)));
            }
            break;
		case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
			// TODO: Test if right. Previously:
			// "registrationComplete.mail.footer", null,locale)
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
					+ StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.footer", locale, messageSource)) + "\n\n");

			break;

		case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
			msg.append("\n"
					+ StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.footer", locale, messageSource)) + "\n\n");

			break;

		case Constants.EMAIL_EVENT_NEW_COURSE_NOTIFICATION:
			msg.append("\n"
					+ StringEscapeUtils.unescapeHtml(getText("registrationNewCourse.mail.footer", locale, messageSource)) + "\n\n");

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
	 * Creates a changed mail body containing all the details of a course and
	 * some information on the event that triggered the mail to be sent.
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
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
	public static StringBuffer createChangedBody(Course course, int event, Locale locale, MessageSource messageSource,
			String mailComment, List<String> changedList, boolean reservationConfirmed) {
		StringBuffer msg = new StringBuffer();

		// Include user defined comment if specified
		if (mailComment != null && StringUtils.isNotBlank(mailComment)) {
			msg.append("\n");
			msg.append(mailComment);
			msg.append("\n\n");
		}

		// Build mail
		msg.append(StringEscapeUtils.unescapeHtml(getText("courseChanged.mail.body", " " + course.getName(), locale,
				messageSource)));
		msg.append("\n\n");

		// Include all the course details
		if (changedList.contains("name")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.name", locale, messageSource)) + ": "
					+ course.getName() + "\n");
		}
		if (changedList.contains("type")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.type", locale, messageSource)) + ": "
					+ course.getType() + "\n");
		}
		if (changedList.contains("startTime")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.startTime", locale, messageSource))
					+ ": "
					+ DateUtil.getDateTime(getText("date.format", locale, messageSource) + " "
							+ getText("time.format", locale, messageSource), course.getStartTime()) + "\n");
		}
		if (changedList.contains("stopTime")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.stopTime", locale, messageSource))
					+ ": "
					+ DateUtil.getDateTime(getText("date.format", locale, messageSource) + " "
							+ getText("time.format", locale, messageSource), course.getStopTime()) + "\n");
		}
		if (changedList.contains("duration")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.duration", locale, messageSource)) + ": "
					+ course.getDuration() + "\n");
		}
		if (changedList.contains("organization")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.organization", locale, messageSource)) + ": "
					+ course.getOrganization().getName() + "\n");
		}
		if (changedList.contains("serviceArea")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.serviceArea", locale, messageSource)) + ": "
					+ course.getServiceArea().getName() + "\n");
		}
		if (changedList.contains("location")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.location", locale, messageSource)) + ": "
					+ course.getLocation().getName() + "\n");
		}
		if (changedList.contains("responsible")) {
			if (course.getResponsible() != null) {
				msg.append(StringEscapeUtils.unescapeHtml(getText("course.responsible", locale, messageSource)) + ": "
						+ course.getResponsible().getFullName() + ", mailto:" + course.getResponsible().getEmail()
						+ "\n");
			}
		}
		if (changedList.contains("instructor")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.instructor", locale, messageSource)) + ": "
					+ course.getInstructor().getName() + ", mailto:" + course.getInstructor().getEmail() + "\n");
		}
		if (changedList.contains("description")) {
			msg.append(StringEscapeUtils.unescapeHtml(getText("course.description", locale, messageSource)) + ": "
					+ course.getDescription() + "\n");
		}

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

		msg.append(StringEscapeUtils.unescapeHtml(getText("courseChanged.mail.footer", locale, messageSource)));

		msg.append("\n\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.contactinfo", locale, messageSource)) + "\n");
		msg.append(StringEscapeUtils.unescapeHtml(getText("mail.donotreply", getText("mail.default.from", locale,
				messageSource), locale, messageSource))
				+ "\n");

		return msg;
	}

	/**
	 * Sets subject, text, recipients and senders to a mail object
	 * 
	 * @param registration
	 *            The registrations to recieve mail
	 * @param event
	 *            If this is a confirmation, cancellation e.g.
	 * @param course
	 *            The course in question
	 * @param msg
	 *            Content of the message
	 * @param messageSource
	 * @param locale
	 * @param sender
	 * @return A list of MimeMessage object
	 * @throws MessagingException
	 */
	public static ArrayList<MimeMessage> getMailMessages(Registration registration, int event, Course course,
			StringBuffer msg, MessageSource messageSource, Locale locale, MailSender sender) {
		List<Registration> theRegistration = new ArrayList<Registration>();
		theRegistration.add(registration);
		return getMailMessages(theRegistration, event, course, msg, messageSource, locale, null, sender);
	}

	/**
	 * Sets subject, text, recipients and senders to a mail object
	 * 
	 * @param registrations
	 *            The registrations to recieve mail
	 * @param event
	 *            If this is a confirmation, cancellation e.g.
	 * @param course
	 *            The course in question
	 * @param msg
	 *            Content of the message
	 * @param messageSource
	 * @param locale
	 * @param from
	 *            The sender of the mail
	 * @param sender
	 * @return A list of MimeMessage object
	 * @throws MessagingException
	 */
	public static ArrayList<MimeMessage> getMailMessages(List<Registration> registrations, int event, Course course,
			StringBuffer msg, MessageSource messageSource, Locale locale, String from, MailSender sender) {
		Log log = LogFactory.getLog(MailUtil.class.toString());
		ArrayList<MimeMessage> allEMails = new ArrayList<MimeMessage>();

		String registered = StringEscapeUtils.unescapeHtml(getText("courseNotification.phrase.registered", locale,
				messageSource));
		String waiting = StringEscapeUtils.unescapeHtml(getText("courseNotification.phrase.waitinglist", locale,
				messageSource));

		for (Registration registration : registrations) {
			MimeMessage message = ((JavaMailSenderImpl) sender).createMimeMessage();
			MimeMessageHelper helper = null;
			try {
				helper = new MimeMessageHelper(message, true, (getText("mail.encoding", locale,messageSource)));
				helper.setSubject(getSubject(registration, event, registered, waiting, messageSource, locale, course));
				helper.setText(getBody(registration, msg, registered, waiting, messageSource, locale));
				Calendar cal = getICalendar(course, registration);
				ByteArrayResource bar = new ByteArrayResource(cal.toString().getBytes());
				helper.addAttachment("calendar.ics", bar, "text/calendar; method=REQUEST");
				List recipients = getRecipients(registration, course.getResponsible());
				log.debug("The mail is to: " + recipients);
				helper.setTo(StringUtil.list2Array(recipients));

				if (from != null && !from.equals(""))
					helper.setFrom(from);
				else
					helper.setFrom(StringEscapeUtils.unescapeHtml(getText("mail.default.from", locale, messageSource)));
			} catch (MessagingException e) {
				log.error("Could not create MimeMessage", e);
			}
			allEMails.add(message);
		}
		return allEMails;
	}

	public static Calendar getICalendar(Course course, Registration registration) {
		Log log = LogFactory.getLog(MailUtil.class.toString());
		Calendar cal = null;

		try {
			// Create an event
			VEvent event = new VEvent(new DateTime(course.getStartTime()), new DateTime(course.getStopTime()), course
					.getName());

			UidGenerator ug = new UidGenerator("1");
			Uid uid = ug.generateUid();
			event.getProperties().add(uid);
			event.getProperties().add(Method.PUBLISH);

			Description description = new Description(course.getDescription());
			event.getProperties().add(description);

			Location location = new Location(course.getLocation().getName());
			event.getProperties().add(location);
			StreetAddress streetAddress = new StreetAddress(course.getLocation().getAddress());
			event.getProperties().add(streetAddress);

			if (course.getStatus() == CourseStatus.COURSE_CANCELLED) {
				event.getProperties().add(Status.VEVENT_CANCELLED);
			} else {
				event.getProperties().add(Status.VEVENT_CONFIRMED);
			}

			if (course.getResponsible() != null) {
				try {
					URI mailto = new URI("MAILTO", course.getResponsible().getEmail(), null);
					Organizer organizer = new Organizer(mailto);
					event.getProperties().add(organizer);
				} catch (Exception ex) {
					log.error("Could not create Organizer object");
				}
			}

			try {
				URI mailto = new URI("MAILTO", registration.getUser().getEmail(), null);
				Attendee attendee = new Attendee(mailto);
				event.getProperties().add(attendee);
			} catch (Exception ex) {
				log.error("Could not create Attendee object");
			}

			if (course.getDetailURL() != null && course.getDetailURL().length() > 0) {
				try {
					Url url = new Url(Uris.create(course.getDetailURL()));
					event.getProperties().add(url);
				} catch (Exception ex) {
					log.error("Could not create Url object");
				}
			}

			// // Set timezone
			// VTimeZone tz = new VTimeZone();
			// TzId tzParam = new
			// TzId(tz.getProperties().getProperty(Property.TZID).getValue());
			// TzId tzParam = new TzId(tz.getProperties().add(Property.TZID));
			// event.getProperties().getProperty(Property.DTSTART).getParameters().add(tzParam);

			// Create calendar and add event
			cal = new Calendar();
			cal.getProperties().add(new ProdId("-//Know IT Objectnet AS//FriKomPort//NO"));
			cal.getProperties().add(Version.VERSION_2_0);
			cal.getProperties().add(CalScale.GREGORIAN);
			cal.getComponents().add(event);

		} catch (Exception e) {
			log.error("Could not create calendar", e);
		}

		return cal;
	}

	public static List<String> getRecipients(Registration registration, User instructor) {
		List<String> emails = new LinkedList<String>();

		if (registration.getEmail() != null && registration.getEmail().trim().length() > 0
				&& EmailValidator.getInstance().isValid(registration.getEmail())) {
			emails.add(registration.getEmail());
		}

		if (emails.size() == 0 && instructor != null && instructor.getEmail() != null
				&& EmailValidator.getInstance().isValid(instructor.getEmail())) {
			emails.add(instructor.getEmail());
		}
		return emails;
	}

	public static String getSubject(Registration registration, int event, String registered, String waiting,
			MessageSource messageSource, Locale locale, Course course) {
		String subject = null;
		String coursename = course.getName();
		switch (event) {
		case Constants.EMAIL_EVENT_COURSECHANGED:
			if (registration.getReserved()) {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseChanged.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
			} else {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseChanged.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
			}
			break;
		case Constants.EMAIL_EVENT_COURSECANCELLED:
			if (registration.getReserved()) {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseCancelled.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
			} else {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseCancelled.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
			}
			break;
		case Constants.EMAIL_EVENT_COURSEDELETED:
			if (registration.getReserved()) {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseDeleted.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
			} else {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseDeleted.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
			}
			break;
		case Constants.EMAIL_EVENT_NOTIFICATION:
			if (registration.getReserved()) {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseNotification.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
			} else {
				subject = StringEscapeUtils.unescapeHtml(
						getText("courseNotification.mail.subject", coursename, locale, messageSource)).replaceAll(
						"<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
			}
			break;
		case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
			if (registration.getReserved()) {
				subject = StringEscapeUtils.unescapeHtml(getText("registrationComplete.mail.subject", coursename,
						locale, messageSource));
			} else {
				subject = StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.subject", coursename,
						locale, messageSource));
			}
			break;
		case Constants.EMAIL_EVENT_REGISTRATION_DELETED:
			subject = StringEscapeUtils.unescapeHtml(getText("registrationDeleted.mail.subject", coursename, locale,
					messageSource));
			break;
		case Constants.EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST:
			subject = StringEscapeUtils.unescapeHtml(getText("registrationToWaitinglist.mail.subject", coursename,
					locale, messageSource));
			break;
		case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
			subject = StringEscapeUtils.unescapeHtml(getText("registrationConfirmed.mail.subject", coursename, locale,
					messageSource));
			break;
		case Constants.EMAIL_EVENT_NEW_COURSE_NOTIFICATION:
				subject = StringEscapeUtils.unescapeHtml(getText("registrationNewCourse.mail.subject", coursename, locale,
						messageSource));
			}
		return subject;
	}

	public static String getBody(Registration registration, StringBuffer msg, String registeredMsg, String waitingMsg,
			MessageSource messageSource, Locale locale) {
		String custom = msg.toString();
		if (registration.getReserved()) {
			custom = custom.replaceAll("<registeredfor/>", registeredMsg);
		} else {
			custom = custom.replaceAll("<registeredfor/>", waitingMsg);
		}
		custom = custom.replaceAll("<coursename/>", registration.getCourse().getName()).replaceAll("<userhash/>",
				registration.getUser().getHash());

		StringBuffer msgIndivid = new StringBuffer(custom);
		msgIndivid.insert(0, "\n\n");

		// Employeenumber
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
		return msgIndivid.toString();
	}

	/**
	 * Method for getting a key's value (with i18n support). Calling
	 * getMessageSourceAccessor() is used because the RequestContext variable is
	 * not set in unit tests b/c there's no DispatchServlet Request.
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
	 * Method for getting a key's value (with i18n support). Calling
	 * getMessageSourceAccessor() is used because the RequestContext variable is
	 * not set in unit tests b/c there's no DispatchServlet Request.
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
	 * Method for getting a key's value (with i18n support). Calling
	 * getMessageSourceAccessor() is used because the RequestContext variable is
	 * not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 *            The key to the message
	 * @param args
	 *            An arbitrary number of arguments to be inserted into the
	 *            retrieved message
	 * @param locale
	 *            the current locale
	 * @param messageSource
	 *            The source of our messages
	 */
	public static String getText(String msgKey, Object[] args, Locale locale, MessageSource messageSource) {
		String result = "";
		try {
			result = messageSource.getMessage(msgKey, args, locale);
		} catch (Exception e) {
			// TODO Handle exception
		}
		return result;
	}

}

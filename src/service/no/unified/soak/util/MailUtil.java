package no.unified.soak.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.io.File;
import java.net.URI;
import java.net.SocketException;
import java.net.URISyntaxException;

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

    public static void sendMimeMails(ArrayList<MimeMessage> Emails, MailEngine engine) {
        if (Emails != null) {
            for (int i = 0; i < Emails.size(); i++) {
                MimeMessage theEmail = Emails.get(i);
                engine.send(theEmail);
            }
        }
    }

	/**
	 * Creates a course cancelled mail body containing all the details of a course
	 * 
	 * @param course
	 *            The course in question
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_COURSECANCELLED_body(Course course, String mailComment) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")) + "\n");

        msg.append("\n"); // empty line

        addMailComment(mailComment, msg);

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseCancelled.mail.body", " " + course.getName())));

        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n"); // empty lines
        
        addDetailsLink(course, msg);

        msg.append("\n\n"); // empty lines

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseCancelled.mail.body", " " + course.getName() + "\n")));

        msg.append("\n\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        return msg;
    }

	/**
	 * Creates a course deleted mail body
	 * 
	 * @param course
	 *            The course in question
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_COURSEDELETED_body(Course course, String mailComment) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")));
        msg.append("\n");
        
        addMailComment(mailComment, msg);

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseDeleted.mail.body", " " + course.getName())) + "\n");

        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n");

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseDeleted.mail.body", " " + course.getName() + "\n")));

        msg.append("\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from"))) + "\n");
        return msg;
    }

	/**
	 * Creates a notification mail body containing info if your registration is
	 * confirmed or still on waitlist, link to detailed course information and
	 * direct cancellation
	 * 
	 * @param course
	 *            The course in question
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_NOTIFICATION_body(Course course, String mailComment, boolean reservationConfirmed) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")));
        msg.append("\n");
        
        addMailComment(mailComment, msg);

        if(reservationConfirmed)
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.mail.body.reserved")));
        else
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.mail.body.waitinglist")));

        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n"); // empty lines

        addDetailsLink(course, msg);
        
        msg.append("\n\n");

        if(reservationConfirmed)
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.mail.footer.registered")));
        else
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.mail.footer.waitinglist")));

        msg.append("\n\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from"))) + "\n");
        return msg;
    }

    
	/**
	 * Creates a notification summary for instructor / responsible
	 * 
	 * @param course
	 *            The course in question
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_NOTIFICATION_SUMMARY_body(Course course, String summary) {
        StringBuffer msg = new StringBuffer();

        String instructor = " " + course.getInstructor().getName() + " ";
        String responsible = " " + course.getResponsible().getFullName();
        
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("misc.hello")) + instructor + StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("misc.and")) + responsible + "\n");
        msg.append("\n");
        
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.summaryheader")) + ":\n");
        
        addMailComment(summary, msg);

        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n"); // empty lines

        addDetailsLink(course, msg);
        
        msg.append("\n\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from"))) + "\n");
        return msg;
    }
    
	/**
	 * Creates a waitinglist mail body containing all the details of a
	 * course, link to detailed course information and direct cancellation
	 * 
	 * @param course
	 *            The course in question
	 * @param registration
	 *            The registration that triggered the sending of this mail
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_WAITINGLIST_NOTIFICATION_body(Course course, Registration registration, String mailComment, boolean reservationConfirmed) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")) + "\n");
        msg.append("\n");
        
        addMailComment(mailComment, msg);
        if (reservationConfirmed)
        	msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationConfirmed.mail.body", course.getName())) + "\n");
        else
        	msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationToWaitinglist.mail.body", course.getName())) + "\n");
        	
        
        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n"); // emptyt lines

        addDetailsLink(course, msg);

        msg.append("\n"); // empty line

        addCancelLink(course, registration, msg);

        msg.append("\n\n");

        // TODO: Test if right. Previously:
        // "registrationComplete.mail.footer", null,locale)
        if (reservationConfirmed)
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationComplete.mail.footer")) + "\n");
        else
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationToWaitinglist.mail.footer", " " + course.getName() + "\n")));

        msg.append("\n\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        return msg;
    }

    
	/**
	 * Creates a confirm cancellation mail body containing all the details of a
	 * course
	 * 
	 * @param course
	 *            The course in question
	 * @param chargeOverdue
	 *            If true unit has to pay for cancelled course
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_REGISTRATION_DELETED_body(Course course, boolean chargeOverdue) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")) + "\n");
        msg.append("\n");
        
        if(chargeOverdue)
        	msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationDeleted.mail.body.chargeoverdue", course.getName())) + "\n");
        else 
        	msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationDeleted.mail.body", course.getName())) + "\n");
        	
        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n");

        addDetailsLink(course, msg);
        
        msg.append("\n");
        
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationDeleted.mail.footer")) + "\n");

        msg.append("\n");
        
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        
        return msg;
    }
    
	/**
	 * Creates a registrationlist mail body containing 
	 * 
	 * @param course
	 *            The course in question
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_REGISTRATIONLIST_body(Course course) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")) + "\n");
        msg.append("\n");
        
        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n");

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationsSent.mail.footer", course.getName())) + "\n");

        msg.append("\n");
        
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        
        return msg;
    }

    
	/**
	 * Creates a moved-to-waitinglist mail body containing all the details of a
	 * course, link to detailed course information and direct cancellation
	 * 
	 * @param course
	 *            The course in question
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST_body(Course course, String mailComment) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")));
        msg.append("\n");
        
        addMailComment(mailComment, msg);

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationToWaitinglist.mail.body", course.getName())) + "\n");

        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n");

        addDetailsLink(course, msg);
        
        msg.append("\n\n");

        msg.append("\n"	+ StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationToWaitinglist.mail.footer")) + "\n");

        msg.append("\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        return msg;
    }
    
    
	/**
	 * Creates a confirm registration mail body containing all the details of a
	 * course, link to detailed course information and direct cancellation
	 * 
	 * @param course
	 *            The course in question
	 * @param registration
	 *            The registration that triggered the sending of this mail
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @param reservationConfirmed
	 *            Set to true if the reservation is confirmed, and to false if
	 *            the attendee is on the waiting list
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_REGISTRATION_CONFIRMED_body(Course course, Registration registration, String mailComment) {
        StringBuffer msg = new StringBuffer();
        
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")));
        msg.append("\n");
        
        // Include user defined comment if specified
        if (mailComment != null && StringUtils.isNotBlank(mailComment)) {
            msg.append("\n"); // empty line
            msg.append(mailComment + "\n");
        }
        
        msg.append("\n"); // empty line

        appendCourseDetails(course, msg);

        msg.append("\n\n"); // empty lines
        
        addDetailsLink(course, msg);

        msg.append("\n"); // empty line

        addCancelLink(course, registration, msg);

        msg.append("\n"); // empty lines

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        return msg;
    }

    
	/**
	 * Creates a new course mail body containing all the details of a
	 * course, link to detailed course information and direct cancellation
	 * 
	 * @param course
	 *            The course in question
	 * @param mailComment
	 *            Optional comment from the admin initiating the sending of this
	 *            mail
	 * @return A complete mail body ready to be inserted into an e-mail object
	 */
    public static StringBuffer create_EMAIL_EVENT_NEW_COURSE_NOTIFICATION_body(Course course, String mailComment) {
        StringBuffer msg = new StringBuffer();

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")));
        msg.append("\n");
        
        addMailComment(mailComment, msg);

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationNewCourse.mail.body")) + "\n");

        msg.append("\n");

        // coursedetails are appended in separate method
        appendCourseDetails(course, msg);

        msg.append("\n\n");

        addDetailsLink(course, msg);

        msg.append("\n\n");

        msg.append("\n" + StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationNewCourse.mail.footer")) + "\n");

        msg.append("\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from")))	+ "\n");
        return msg;
    }
    
    /**
     * Adds mailComment to current msg
     * @param mailComment
     * @param msg
     */
	private static void addMailComment(String mailComment, StringBuffer msg) {
		// Include user defined comment if specified
        if (mailComment != null && StringUtils.isNotBlank(mailComment)) {
            msg.append("\n");
            msg.append(mailComment);
            msg.append("\n\n");
        }
	}

	/**
	 * Adds link to details of current course
	 * @param course
	 * @param locale
	 * @param messageSource
	 * @param msg
	 */
    private static void addDetailsLink(Course course, StringBuffer msg) {
    	// link til detaljer om registrering
    	String baseurl = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("javaapp.baseurl"));
    	
    	msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("javaapp.findurlhere")) + "\n");
    	String coursedetailurl = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("javaapp.coursedetailurl", ""+course.getId()));
    	msg.append(baseurl + coursedetailurl + "\n");
    }

    /**
     * Adds link to direct cancellation of registration, including info about chargeoverdue if present
     * @param course
     * @param registration
     * @param locale
     * @param messageSource
     * @param msg
     */
    private static void addCancelLink(Course course, Registration registration, StringBuffer msg) {
		// link for direkte avmelding
    	String baseurl = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("javaapp.baseurl"));

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("javaapp.cancelcourse")) + "\n");
        String coursecancelurl = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("javaapp.coursecancelurl", ""+registration.getId()));
        msg.append(baseurl + coursecancelurl + "\n");
        
        if(course.getChargeoverdue()) msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationConfirmed.mail.footer.overdue")) + "\n");
	}

    
    /**
     * Adds course details to msg
     * @param course
     * @param locale
     * @param messageSource
     * @param msg
     */
	private static void appendCourseDetails(Course course, StringBuffer msg) {
		// Include all the course details
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.name")) + ": "
                + course.getName() + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.type")) + ": "
                + course.getType() + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.startTime"))
                + ": "
                + DateUtil.getDateTime(ApplicationResourcesUtil.getText("date.format") + " "
                        + ApplicationResourcesUtil.getText("time.format"), course.getStartTime()) + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.stopTime"))
                + ": "
                + DateUtil.getDateTime(ApplicationResourcesUtil.getText("date.format") + " "
                        + ApplicationResourcesUtil.getText("time.format"), course.getStopTime()) + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.duration")) + ": "
                + course.getDuration() + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.organization")) + ": "
                + course.getOrganization().getName() + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.serviceArea")) + ": "
                + course.getServiceArea().getName() + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.location")) + ": "
                + course.getLocation().getName() + "\n");

        if (course.getResponsible() != null) {
            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.responsible")) + ": "
                    + course.getResponsible().getFullName() + ", mailto:" + course.getResponsible().getEmail() + "\n");
        }

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.instructor")) + ": "
                + course.getInstructor().getName() + ", mailto:" + course.getInstructor().getEmail() + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.description")) + ": "
                + course.getDescription() + "\n");
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
    public static StringBuffer createChangedBody(Course course, Locale locale, MessageSource messageSource,
            String mailComment, List<String> changedList) {
        StringBuffer msg = new StringBuffer();

        addMailComment(mailComment, msg);

        // Build mail
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseChanged.mail.body", " " + course.getName())));
        msg.append("\n\n");

        if(changedList != null) {
	        // Include all the course details
	        if (changedList.contains("name")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.name")) + ": "
	                    + course.getName() + "\n");
	        }
	        if (changedList.contains("type")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.type")) + ": "
	                    + course.getType() + "\n");
	        }
	        if (changedList.contains("startTime")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.startTime"))
	                    + ": "
	                    + DateUtil.getDateTime(ApplicationResourcesUtil.getText("date.format") + " "
	                            + ApplicationResourcesUtil.getText("time.format"), course.getStartTime()) + "\n");
	        }
	        if (changedList.contains("stopTime")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.stopTime"))
	                    + ": "
	                    + DateUtil.getDateTime(ApplicationResourcesUtil.getText("date.format") + " "
	                            + ApplicationResourcesUtil.getText("time.format"), course.getStopTime()) + "\n");
	        }
	        if (changedList.contains("duration")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.duration")) + ": "
	                    + course.getDuration() + "\n");
	        }
	        if (changedList.contains("organization")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.organization")) + ": "
	                    + course.getOrganization().getName() + "\n");
	        }
	        if (changedList.contains("serviceArea")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.serviceArea")) + ": "
	                    + course.getServiceArea().getName() + "\n");
	        }
	        if (changedList.contains("location")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.location")) + ": "
	                    + course.getLocation().getName() + "\n");
	        }
	        if (changedList.contains("responsible")) {
	            if (course.getResponsible() != null) {
	                msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.responsible")) + ": "
	                        + course.getResponsible().getFullName() + ", mailto:" + course.getResponsible().getEmail()
	                        + "\n");
	            }
	        }
	        if (changedList.contains("instructor")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.instructor")) + ": "
	                    + course.getInstructor().getName() + ", mailto:" + course.getInstructor().getEmail() + "\n");
	        }

	        if (changedList.contains("maxAttendants")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.maxAttendants")) + ": "
	                    + course.getMaxAttendants() + "\n");
	        }
	        /*
	         * Internal /external fee are (most likely) not to be broadcasted to all attendants
			*/
	        if (changedList.contains("description")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.description")) + ": "
	                    + course.getDescription() + "\n");
	        }
	        if (changedList.contains("chargeoverdue")) {
	            msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.chargeoverdue")) + ": "
	                    + (course.getChargeoverdue()?"Ja":"Nei") + "\n");
	        }
	    }

/*
		// why change a deleted course -- case should never occur
	
        // We cannot link to a deleted course, so the link is only displayed if
        // the course still exists
        if (event != Constants.EMAIL_EVENT_COURSEDELETED) {
            String baseurl = StringEscapeUtils.unescapeHtml(getText("javaapp.baseurl", locale, messageSource));
            String coursedetailurl = StringEscapeUtils.unescapeHtml(getText("javaapp.coursedetailurl", String
                    .valueOf(course.getId()), locale, messageSource));
            msg.append("\n\n");
            msg.append(StringEscapeUtils.unescapeHtml(getText("javaapp.findurlhere", locale, messageSource)) + " "
                    + baseurl + coursedetailurl);
            msg.append("\n");
        }
*/
        msg.append("\n");

        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseChanged.mail.footer")));

        msg.append("\n\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.contactinfo")) + "\n");
        msg.append(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.donotreply", ApplicationResourcesUtil.getText("mail.default.from"))) + "\n");

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
    public static ArrayList<MimeMessage> getMailMessages(Registration registration, int event, Course course, StringBuffer msg, MailSender sender) {
        List<Registration> theRegistration = new ArrayList<Registration>();
        theRegistration.add(registration);
        return getMailMessages(theRegistration, event, course, msg, null, sender);
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
     * @param from
     *            The sender of the mail
     * @param sender
     * @return A list of MimeMessage object
     * @throws MessagingException
     */
    public static ArrayList<MimeMessage> getMailMessages(List<Registration> registrations, int event, Course course,
            StringBuffer msg, String from, MailSender sender) {
        Log log = LogFactory.getLog(MailUtil.class.toString());
        ArrayList<MimeMessage> allEMails = new ArrayList<MimeMessage>();

        String registered = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.phrase.registered"));
        String waiting = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.phrase.waitinglist"));

        for (Registration registration : registrations) {
            MimeMessage message = ((JavaMailSenderImpl) sender).createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true, (ApplicationResourcesUtil.getText("mail.encoding")));
                helper.setSubject(getSubject(registration, event, registered, waiting, course));
                helper.setText(getBody(registration, msg, registered, waiting));
                addCalendar(helper,event,course, registration);                
                helper.setTo(registration.getEmail());
                helper.setCc(course.getResponsible().getEmail());
                
                if (from != null && !from.equals(""))
                    helper.setFrom(from);
                else
                    helper.setFrom(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.default.from")));
            } catch (MessagingException e) {
                log.error("Could not create MimeMessage", e);
            }
            allEMails.add(message);
        }
        return allEMails;
    }

    public static MimeMessage getMailMessage(String[] to, String[] cc, String[] bcc, String from, String subject, StringBuffer msg, String attachmentName, File attachementFileOnDisk, MailSender sender) {
        Log log = LogFactory.getLog(MailUtil.class.toString());

        MimeMessage message = null;
        
        if(subject == null) subject = "(No Subject)";
        try {

	        message = ((JavaMailSenderImpl) sender).createMimeMessage();
	        MimeMessageHelper helper = null;
	        helper = new MimeMessageHelper(message, true, (ApplicationResourcesUtil.getText("mail.encoding")));
	
	        helper.setSubject(subject);
	        helper.setText(msg.toString());
	        helper.setTo(to);
	        
	        if(cc != null) helper.setCc(cc);
	        if(bcc != null) helper.setBcc(bcc);
	        
	        if (from != null && !from.equals(""))
	            helper.setFrom(from);
	        else
	            helper.setFrom(StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("mail.default.from")));
	
	        if(attachmentName != null && attachementFileOnDisk != null) helper.addAttachment(attachmentName, attachementFileOnDisk);
        }
        catch(MessagingException e) {
        	log.error("Error creating mimemessage", e);
        }
        return message;
    }
    
    /**
     * Adds ics file if registration OK, course changed or course cancelled.
     * @param helper
     * @param event
     * @param course
     * @param registration
     * @throws MessagingException
     */
    private static void addCalendar(MimeMessageHelper helper, int event, Course course, Registration registration) throws MessagingException {
        switch (event) {
        case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
        case Constants.EMAIL_EVENT_COURSECHANGED:
        case Constants.EMAIL_EVENT_COURSECANCELLED:
            Calendar cal = getICalendar(course, registration);
            ByteArrayResource bar = new ByteArrayResource(cal.toString().getBytes());
            helper.addAttachment("calendar.ics", bar, "text/calendar; method=REQUEST");  
            break;
        }
    }

    public static Calendar getICalendar(Course course, Registration registration) {
        Log log = LogFactory.getLog(MailUtil.class.toString());
        Calendar cal = null;

        try {
            // Create an event
            VEvent event = getVEvent(course);

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

            if (course.getStatus().equals(CourseStatus.COURSE_CANCELLED)) {
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
            cal = createCalendar();
            cal.getComponents().add(event);

        } catch (Exception e) {
            log.error("Could not create calendar", e);
        }

        return cal;
    }

    public static Calendar createCalendar() {
        Calendar cal;
        cal = new Calendar();
        cal.getProperties().add(new ProdId("-//Know IT Objectnet AS//FriKomPort//NO"));
        cal.getProperties().add(Version.VERSION_2_0);
        cal.getProperties().add(CalScale.GREGORIAN);
        return cal;
    }

    public static VEvent getVEvent(Course course) throws SocketException {
        Log log = LogFactory.getLog(MailUtil.class.toString());
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

        try {
            Url url = new Url(new URI(null,"http://www.vg.no",null));
            event.getProperties().add(url);
        } catch (URISyntaxException e) {
            // Wrong format
        }

        if (course.getStatus().equals(CourseStatus.COURSE_CANCELLED)) {
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
        return event;
    }

    public static List<String> getRecipients(Registration registration, User instructor) {
        List<String> emails = new LinkedList<String>();

        if (registration.getUser() != null && registration.getUser().getEmail().trim().length() > 0
                && EmailValidator.getInstance().isValid(registration.getUser().getEmail())) {
            emails.add(registration.getEmail());
        }

        if (emails.size() == 0 && instructor != null && instructor.getEmail() != null
                && EmailValidator.getInstance().isValid(instructor.getEmail())) {
            emails.add(instructor.getEmail());
        }
        return emails;
    }

    public static String getSubject(Registration registration, int event, String registered, String waiting, Course course) {
        String subject = null;
        String coursename = course.getName();
        switch (event) {
        case Constants.EMAIL_EVENT_COURSECHANGED:
            if (registration.getReserved()) {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseChanged.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
            } else {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseChanged.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
            }
            break;
        case Constants.EMAIL_EVENT_COURSECANCELLED:
            if (registration.getReserved()) {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseCancelled.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
            } else {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseCancelled.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
            }
            break;
        case Constants.EMAIL_EVENT_COURSEDELETED:
            if (registration.getReserved()) {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseDeleted.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
            } else {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseDeleted.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
            }
            break;
        case Constants.EMAIL_EVENT_NOTIFICATION:
            if (registration.getReserved()) {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseNotification.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", registered).replaceAll("<coursename/>", coursename);
            } else {
                subject = StringEscapeUtils.unescapeHtml(
                		ApplicationResourcesUtil.getText("courseNotification.mail.subject", coursename)).replaceAll(
                                "<registeredfor/>", waiting).replaceAll("<coursename/>", coursename);
            }
            break;
        case Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION:
            if (registration.getReserved()) {
                subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationComplete.mail.subject", coursename));
            } else {
                subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationToWaitinglist.mail.subject", coursename));
            }
            break;
        case Constants.EMAIL_EVENT_REGISTRATION_DELETED:
            subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationDeleted.mail.subject", coursename));
            break;
        case Constants.EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST:
            subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationToWaitinglist.mail.subject", coursename));
            break;
        case Constants.EMAIL_EVENT_REGISTRATION_CONFIRMED:
            subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationConfirmed.mail.subject", coursename));
            break;
        case Constants.EMAIL_EVENT_NEW_COURSE_NOTIFICATION:
            subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("registrationNewCourse.mail.subject", coursename));
        }
        return subject;
    }

    public static String getBody(Registration registration, StringBuffer msg, String registeredMsg, String waitingMsg) {
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
        String employeeNoText = ApplicationResourcesUtil.getText("registration.employeeNumber");
        if (!StringUtils.isEmpty(employeeNoText))
            employeeNoText = employeeNoText.toLowerCase();

        if (registration.getEmployeeNumber() != null) {
            String ansattParentes = " (" + StringEscapeUtils.unescapeHtml(employeeNoText) + " "
            + registration.getEmployeeNumber().intValue() + ")";

            msgIndivid.insert(0, StringUtil.ifEmpty(registration.getEmployeeNumber(), ansattParentes));
        }

        msgIndivid.insert(0, ApplicationResourcesUtil.getText("misc.hello") + " " + registration.getFirstName() + " "
                + registration.getLastName());
        return msgIndivid.toString();
    }


}

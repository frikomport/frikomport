package no.unified.soak.webapp.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.mail.internet.MimeMessage;

import no.unified.soak.Constants;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.util.MailUtil;
import no.unified.soak.model.Course;
import no.unified.soak.model.User;
import no.unified.soak.model.Registration;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;

public class CourseEmailController extends CourseNotificationController
{
    private RegistrationManager registrationManager;
    private MessageSource messageSource;
    private MailSender mailSender;

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method...");
        }
        Map model = new HashMap();
        Locale locale = request.getLocale();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);

        model.put("mailsenders",getMailSenders((Course)command, user, locale));

        return model;
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

        Locale locale = request.getLocale();

        String mailComment = request.getParameter("mailcomment");

        String mailSender = request.getParameter("mailsender");

        // Are we to return to the course list?
        if (request.getParameter("skip") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'skip' from jsp");
            }
            return new ModelAndView(getCancelView(), "id", course.getId().toString());
        } // or to send out notification email?
        else if (request.getParameter("send") != null) {
            sendMail(locale, course, Constants.EMAIL_EVENT_NOTIFICATION, mailComment, mailSender);
        }

        model.put("course",course);

        return new ModelAndView(getSuccessView(),model);
    }

    /**
	 * Sends mail to the registered users
	 *
	 * @param locale
	 *            The locale to use
     * @param course
     * @param from
     */
	private void sendMail(Locale locale, Course course, int event, String mailComment, String from) {
		log.debug("Sending mail from CourseEmailController");
		List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null, true, null, null, null, null);

		// Sender mail til kun reserverte.
		StringBuffer msg = null;
		switch(event) {
			case Constants.EMAIL_EVENT_NOTIFICATION:
				msg = MailUtil.create_EMAIL_EVENT_NOTIFICATION_body(course, mailComment, true);
				break;
			default:
				if(log.isDebugEnabled()) log.debug("sendMail: Handling of event:" + event + " not implemented..!");
		}
		
		ArrayList<MimeMessage> emails = MailUtil.getMailMessages(registrations, event, course, msg, from, mailSender);
		MailUtil.sendMimeMails(emails, mailEngine);

	}
}

package no.unified.soak.webapp.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.User;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class CourseEmailController extends CourseNotificationController
{

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
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
            sendMail(locale, course, Constants.EMAIL_EVENT_NOTIFICATION, mailComment, mailSender, null);
        }

        return new ModelAndView(getSuccessView());
    }
}

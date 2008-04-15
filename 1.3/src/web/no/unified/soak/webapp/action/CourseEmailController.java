package no.unified.soak.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.RegistrationManager;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class CourseEmailController extends CourseNotificationController
{
    private CourseManager courseManager = null;

    private RegistrationManager registrationManager = null;

    private MessageSource messageSource = null;
    
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
        if (request.getParameter("skip") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'skip' from jsp");
            }
            return new ModelAndView(getCancelView(), "id", course.getId().toString());
        } // or to send out notification email?
        else if (request.getParameter("send") != null) {
            sendMail(locale, course, Constants.EMAIL_EVENT_NOTIFICATION, mailComment);
        }

        return new ModelAndView(getSuccessView());
    }
}

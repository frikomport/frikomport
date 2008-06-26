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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.MailUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
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

    private MailSender mailSender = null;

    protected MailEngine mailEngine = null;

	protected SimpleMailMessage message = null;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
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
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
        Locale locale = request.getLocale();
        String courseid = request.getParameter("id");
		model.put("id", courseid);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);

        // Are we to enable mail comment field and buttons?
		Boolean enableMail = new Boolean(false);
		String mailParam = request.getParameter("enablemail"); 
		if ((mailParam != null) && (mailParam.compareToIgnoreCase("true") == 0)) {
			enableMail = new Boolean(true);
		}
		model.put("enableMail", enableMail);

        model.put("mailsenders",getMailSenders((Course)command, user, locale));

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
			User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
			Object omid = user.getOrganizationid();
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
		HttpSession session = request.getSession();
		String format = getText("date.format", request.getLocale()) + " " + getText("time.format", request.getLocale());
		
		List <String> changedList = null;
		
		if (course.getStatus() != CourseStatus.COURSE_CANCELLED){
			//check what has changed
			Course originalCourse = (Course) session.getAttribute(Constants.ORG_COURSE_KEY);
			if (originalCourse != null){
				changedList = courseManager.getChangedList(originalCourse, course, format);
			}
		}

		Locale locale = request.getLocale();

		String mailComment = request.getParameter("mailcomment");

        String mailSender = request.getParameter("mailsender");

        // Are we to return to the course list?
		if (request.getParameter("return") != null) {
			if (log.isDebugEnabled()) {
				log.debug("recieved 'return' from jsp");
			}
			return new ModelAndView(getCancelView(), "id", course.getId().toString());
		} // or to send out notification email?
		else if (request.getParameter("send") != null) {
            if( course.getStatus() == CourseStatus.COURSE_CANCELLED){
                sendMail(locale, course, Constants.EMAIL_EVENT_COURSECANCELLED, mailComment, mailSender, changedList);
            }
            else{
                sendMail(locale, course, Constants.EMAIL_EVENT_COURSECHANGED, mailComment, mailSender, changedList);
            }
        }
		
		session.setAttribute(Constants.ORG_COURSE_KEY, null);

		return new ModelAndView(getSuccessView());
	}

	/**
	 * Sends mail to the user
	 * 
	 * @param locale
	 *            The locale to use
     * @param course
     * @param from
     */
	protected void sendMail(Locale locale, Course course, int event, String mailComment, String from, List <String> changedList) {
		log.debug("Sending mail from CourseNotificationController");
		List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null, null,null, null, null);
		
		StringBuffer msg; 
		if (event == Constants.EMAIL_EVENT_COURSECHANGED){
			msg = MailUtil.createChangedBody(course, event, locale, messageSource, mailComment, changedList); 
		}else {
			msg = MailUtil.createStandardBody(course, event, locale, messageSource, mailComment);
			
		}
		ArrayList<MimeMessage> emails = MailUtil.getMailMessages(registrations, event, course, msg, messageSource, locale, from, mailSender);
		MailUtil.sendMimeMails(emails, mailEngine);
		
	}

	/**
	 * @param registrationManager
	 *            The registrationManager to set.
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

    protected List<String> getMailSenders(Course course, User user, Locale locale){
        List senders = new ArrayList<String>();
        String defaultFrom = getText("mail.default.from", locale);
        senders.add(defaultFrom);

        String userfrom =  user.getFullName() + " <" + user.getEmail() + ">";
        senders.add(userfrom);

        User responsible = course.getResponsible();
        if(!user.equals(responsible)){
            String responsiblefrom = responsible.getFullName() + " <" + responsible.getEmail() + ">";
            senders.add(responsiblefrom);
        }
        return senders;
    }
    
    /**
     * Convenience method for getting a i18n key's value. Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
     *
     * @param msgKey
     * @param locale
     *            the current locale
     */
    public String getText(String msgKey, Locale locale) {
        return getMessageSourceAccessor().getMessage(msgKey, locale);
    }
}

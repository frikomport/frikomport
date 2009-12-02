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
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.WaitingListManager;
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
	protected ConfigurationManager configurationManager = null;
	private WaitingListManager waitingListManager = null;
	
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

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

	public void setWaitingListManager(WaitingListManager waitingListManager) {
		this.waitingListManager = waitingListManager;
	}

    
    /**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	public Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
        Locale locale = request.getLocale();
        Course course = (Course)command;
        String courseid = request.getParameter("id");
		model.put("id", courseid);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);

        // course is changed - waitingList should be processed
        waitingListManager.processIfNeeded(course.getId(), locale);
        
		Boolean enableMail = new Boolean(false);
		Boolean newCourse = new Boolean(false);
		Boolean waitinglist = new Boolean(false);
		String mailParam = request.getParameter("enablemail"); 
		String courseParam = request.getParameter("newCourse"); 
		String waitinglistParam = request.getParameter("waitinglist"); 
		
		if ((mailParam != null) && (mailParam.compareToIgnoreCase("true") == 0)) {
			enableMail = new Boolean(true);
		}
		if ((courseParam != null) && (courseParam.compareToIgnoreCase("true") == 0)) {
			newCourse = new Boolean(true);
		}
		
		if ((waitinglistParam != null) && (waitinglistParam.compareToIgnoreCase("true") == 0)) {
			 waitinglist = new Boolean(true);
		}
		
		model.put("waitinglist", waitinglist);
		model.put("enableMail", enableMail);
		model.put("newCourse", newCourse);
        model.put("mailsenders",getMailSenders(course, user, locale));

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
			course.setCopyid(new Long(copyid));
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

        Map model = new HashMap();
        Course course = (Course) command;
		HttpSession session = request.getSession();
		String format = getText("date.format", request.getLocale()) + " " + getText("time.format", request.getLocale());
		
		List <String> changedList = null;
		
		if (!course.getStatus().equals(CourseStatus.COURSE_CANCELLED)){
			//check what has changed
			Course originalCourse = (Course) session.getAttribute(Constants.ORG_COURSE_KEY);
			if (originalCourse != null){
				changedList = courseManager.getChangedList(originalCourse, course, format);
			}
			else log.error("NB!! changedList==null - Course not in session - caused by eZ Publish/forward/redirect...!!");
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
			if (course.getStatus().equals(CourseStatus.COURSE_PUBLISHED) && course.getCopyid() != null){
				// FKM-517
				sendMailToWaitingList(locale, course, Constants.EMAIL_EVENT_NEW_COURSE_NOTIFICATION, mailComment, mailSender, changedList);
			}
			else if( course.getStatus().equals(CourseStatus.COURSE_CANCELLED)){
                sendMail(locale, course, Constants.EMAIL_EVENT_COURSECANCELLED, mailComment, mailSender, changedList);
            }
            else{
                sendMail(locale, course, Constants.EMAIL_EVENT_COURSECHANGED, mailComment, mailSender, changedList);
            }
        }

        model.put("course",course);
        session.setAttribute(Constants.ORG_COURSE_KEY, null);

		return new ModelAndView(getSuccessView(),model);
	}

	/**
	 * Sends mail to the user
	 * 
	 * @param locale
	 *            The locale to use
     * @param course
     * @param from
     */
	private void sendMail(Locale locale, Course course, int event, String mailComment, String from, List <String> changedList) {
		log.debug("Sending mail from CourseNotificationController");
		List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null, null,null, null, null, null);
		
		StringBuffer msg = null;
		switch(event) {
			case Constants.EMAIL_EVENT_COURSECHANGED:
				msg = MailUtil.createChangedBody(course, locale, messageSource, mailComment, changedList); 
				break;
			case Constants.EMAIL_EVENT_COURSECANCELLED:
				msg = MailUtil.create_EMAIL_EVENT_COURSECANCELLED_body(course, mailComment);
				break;
			default:
				if(log.isDebugEnabled()) log.debug("sendMail: Handling of event:" + event + " not implemented..!");
		}
		ArrayList<MimeMessage> emails = MailUtil.getMailMessages(registrations, event, course, msg, from, mailSender, false);
		MailUtil.sendMimeMails(emails, mailEngine);
		
		if(configurationManager.isActive("mail.course.sendSummary", true)) {
			MailUtil.sendSummaryToResponsibleAndInstructor(course, from, registrations, msg, mailEngine, mailSender);
		}
	}
	

	/**
	 * Sends mail to the users on the waitinglist on the orginal course
	 * 
	 * @param locale
	 *            The locale to use
     * @param course
     * @param from
     */
	private void sendMailToWaitingList(Locale locale, Course course, int event, String mailComment, String from, List <String> changedList) {
		log.debug("Sending mail from CourseNotificationController");
		List<Long> ids = new ArrayList<Long> ();
		ids.add(course.getCopyid());
		List<Registration> registrations = registrationManager.getWaitingListRegistrations(ids);
		
		StringBuffer msg = null;
		switch(event) {
			case Constants.EMAIL_EVENT_NEW_COURSE_NOTIFICATION:
				msg = MailUtil.create_EMAIL_EVENT_NEW_COURSE_NOTIFICATION_body(course, mailComment);
				break;
			default:
				if(log.isDebugEnabled()) log.debug("sendMailToWaitingList: Handling of event:" + event + " not implemented..!");
		}
		
		ArrayList<MimeMessage> emails = MailUtil.getMailMessages(registrations, event, course, msg, from, mailSender, false);
		MailUtil.sendMimeMails(emails, mailEngine);

		if(configurationManager.isActive("mail.course.sendSummary", true)) {
			MailUtil.sendSummaryToResponsibleAndInstructor(course, from, registrations, msg, mailEngine, mailSender);
		}
	}

	/**
	 * @param registrationManager
	 *            The registrationManager to set.
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

    protected List<String> getMailSenders(Course course, User user, Locale locale){
        List<String> senders = new ArrayList<String>();
        String defaultFrom = getText("mail.default.from",locale);
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

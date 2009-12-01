package no.unified.soak.webapp.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.util.MailUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


/**
 * Controller for handling cancellations from mail-links
 * @author sa
 */
public class CancelRegistrationController implements Controller {
    private final Log log = LogFactory.getLog(CancelRegistrationController.class);
    private RegistrationManager registrationManager = null;
    private CourseManager courseManager = null;
    private UserManager userManager = null;
    private MessageSource messageSource = null;
    private MailEngine mailEngine = null;
    private MailSender mailSender = null;    
	private WaitingListManager waitingListManager = null;
    private ConfigurationManager configurationManager = null;
	
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setCourseManager(CourseManager courseManager){
        this.courseManager = courseManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
	public void setWaitingListManager(WaitingListManager waitingListManager) {
		this.waitingListManager = waitingListManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
    /**
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }
        Map<String,Object> model = new HashMap<String,Object>();

        String registrationid = request.getParameter("rid");
        String username_hash = request.getParameter("hash");
        String confirm = request.getParameter("confirm");
        
        model.put("rid", registrationid);
        model.put("hash", username_hash);
        
        Locale locale = request.getLocale();
        Registration registration = (Registration)formBackingObject(registrationid);
        User user = userManager.getUserByHash(username_hash);
        
        // verifies that username from stored registration matches hash in request
        if(registration.getUsername().equals(user.getUsername())) {
        	Course course = registration.getCourse();
        	
        	if(confirm != null && confirm.equals("true")) {
	        	registrationManager.removeRegistration(""+registration.getId());
	        	boolean chargeOverdue = false;
	        	// cancellation after registerBy-date might cause charge of fee 
	        	if(new Date().after(course.getRegisterBy())) {
	        		if(course.getChargeoverdue()) {
	        			chargeOverdue = true;
	        		}
	        	}
	        	
	        	model.put("completed", new Boolean(true));

	        	// confirm cancellation mail
	        	boolean ccToResponsible = configurationManager.isActive("mail.registration.notifyResponsible", false);
	        	
	        	StringBuffer msg = MailUtil.create_EMAIL_EVENT_REGISTRATION_DELETED_body(course, chargeOverdue);
	    		ArrayList<MimeMessage> email = MailUtil.getMailMessages(registration, Constants.EMAIL_EVENT_REGISTRATION_DELETED, course, msg, mailSender, ccToResponsible);
	    		MailUtil.sendMimeMails(email, mailEngine);

	    		// notify/upgrade no.1 on waitinglist
	    		waitingListManager.processIfNeeded(course.getId(), locale);
        	}
        	else if(confirm != null && confirm.equals("false")) {
	        	model.put("abort", new Boolean(true));
        	}
        	else {
        		// show confirm and cancel buttons
        		model.put("ask_for_confirmation", new Boolean(true));
        	}

        	DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    		String start = formatter.format(course.getStartTime());
    		String stop = formatter.format(course.getStopTime());
    		String date = start.equals(stop)? start : start + " - " + stop;

    		model.put("coursename", course.getName() + ", " + date);
    		model.put("firstname", user.getFirstName());
    		model.put("lastname", user.getLastName());
    		model.put("email", user.getEmail());
        }
        return new ModelAndView("registrationCancel", "cancel", model);
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(String registrationid)
        throws Exception {
        Registration registration = new Registration();
        if (registrationid == null) {
            // ERROR - should never happen
        } else {
            registration = registrationManager.getRegistration(registrationid);
        }
        return registration;
    }

}

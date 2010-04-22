package no.unified.soak.webapp.action;

import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.model.User;
import no.unified.soak.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Locale;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.mail.SimpleMailMessage;
import org.apache.commons.validator.EmailValidator;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 13.jun.2008
 * Time: 21:07:39
 * To change this template use File | Settings | File Templates.
 */
public class UserRegistrationController extends BaseFormController{
    private RegistrationManager registrationManager = null;
    private UserManager userManager = null;
    private CourseManager courseManager = null;
    private ConfigurationManager configurationManager = null;

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    protected Map referenceData(HttpServletRequest request, Object o, Errors errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method...");
        }
        Locale locale = request.getLocale();
        Map model = new HashMap();

        HttpSession session = request.getSession();
 
        User user = null;
        if(configurationManager.isActive("access.registration.userdefaults",false)){
            user = (User) session.getAttribute(Constants.USER_KEY);
            if(user != null && user.getUsername().equals(Constants.ANONYMOUS_ROLE)){
                user = (User) session.getAttribute(Constants.ALT_USER_KEY);
            }
        }
        else{
            user = (User) session.getAttribute(Constants.ALT_USER_KEY);
        }
        if(user != null){
            List registrations = registrationManager.getUserRegistrations(user.getUsername());
            model.put("userRegistrations",registrations);
        }
        return model;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {
        Map model = new HashMap();
        Locale locale = request.getLocale();
        
        String email = request.getParameter("email");
        if(EmailValidator.getInstance().isValid(email)){
            User user = userManager.findUserByEmail(email);
            if(user != null){
                sendMail(user, locale);
                saveMessage(request, getText("user.emailsent", user.getEmail() ,locale));
            }
            else {
                // User not found, view error
                saveMessage(request, getText("user.notfound", locale));
                return new ModelAndView(getSuccessView(), model);
            }
        }
        return new ModelAndView(getSuccessView(), model);
    }

    private void sendMail(User user, Locale locale) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(getText("userRegistrationForm.mail.subject",locale));
        StringBuffer msg = new StringBuffer();
        msg.append(getText("userRegistrationForm.mail.body",locale));
        msg.append(getText("javaapp.baseurl",locale));
        msg.append(getText("javaapp.profileurl",locale));
        String body = msg.toString().replaceAll("<userhash/>",user.getHash());
        message.setText(body);
        message.setFrom(getText("mail.default.from",locale));
        this.mailEngine.send(message);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Locale locale = request.getLocale();

        User user = null;
        if(configurationManager.isActive("access.registration.userdefaults",false)){
            user = (User) session.getAttribute(Constants.USER_KEY);
            if(user != null && user.getUsername().equals(Constants.ANONYMOUS_ROLE)){
                user = (User) session.getAttribute(Constants.ALT_USER_KEY);
            }            
        }
        else{
            // Dersom fellesbruker, hent anna bruker.
            user = (User) session.getAttribute(Constants.ALT_USER_KEY);
        }
        if(user == null)
            user = new User();
        return user;
    }
}

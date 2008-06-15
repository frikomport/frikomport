package no.unified.soak.webapp.action;

import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.CourseManager;
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

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException e) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method...");
        }
        Locale locale = request.getLocale();
        Map model = new HashMap();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);
        if(user == null){
            return new ModelAndView(getFormView(), model);
        }

        List registrations = registrationManager.getUserRegistrations(user);
        model.put("userRegistrations",registrations);
        model.put("user",user); // Er dette nødvendig?

        return new ModelAndView(getSuccessView(), model);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        // Dersom fellesbruker, hent anna bruker.
        User user = (User) session.getAttribute(Constants.USER_KEY);
        return user;
    }
}

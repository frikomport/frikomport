/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.unified.soak.model.User;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * <a href="UserDetailsController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserDetailsController extends BaseFormController {
    private UserManager mgr = null;
    private RegistrationManager registrationManager = null;

    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap();
 
        User user = (User)formBackingObject(request);
        List registrations = registrationManager.getUserRegistrations(user.getUsername());
        model.put("userRegistrations",registrations);
        return model;
    }

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
    */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        if(username == null || "".equals(username)) {
            username = (String)session.getAttribute("username");
        }
        return mgr.getUser(username);
    }
    
}

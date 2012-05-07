/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.model.User;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * <a href="UserController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserController extends BaseFormController {
    private transient final Log log = LogFactory.getLog(UserController.class);
    private UserManager userManager = null;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

   
    @Override
    protected Map referenceData(HttpServletRequest request, Object command,
            Errors errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }
        Map model = new HashMap();
        User user = new User();
        
        HttpSession session = request.getSession();
        Object comm = session.getAttribute("user");
        if(comm != null) {
            user = (User)comm;
        }

        List users = null;
        if(ApplicationResourcesUtil.isSVV()){
        	// SVV ønsker IKKE visning av annet enn interne brukere under "Brukerliste"
        	user.setHashuser(false);
        	users = userManager.getUsers(user, true);
        	List<User> usersWithRoles = new ArrayList<User>();
        	Iterator<User> us = users.iterator();
    		while(us.hasNext()){
    			User u = us.next();
    			if(!u.getRoleNameList().isEmpty()){
    				// SVV brukere som pt. ikke har FKP-roller taes bort fra "Brukerliste".
    				usersWithRoles.add(u);
    			}
    		}
    		users = usersWithRoles;
        }
        else {
        	users = userManager.getUsers(user, false);
        }
        
        if (users != null) {
            model.put("userList", users);
        }

        return model;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        Map model = new HashMap();
        HttpSession session = request.getSession();

        User user = (User) command;
        model.put("user", user);
        session.setAttribute("user", user);

        List users = null;
        if(ApplicationResourcesUtil.isSVV()){
        	// SVV ønsker IKKE visning av annet enn interne brukere under "Brukerliste"
        	user.setHashuser(false);
        	users = userManager.getUsers(user, true);
        	List<User> usersWithRoles = new ArrayList<User>();
        	Iterator<User> us = users.iterator();
    		while(us.hasNext()){
    			User u = us.next();
    			if(!u.getRoleNameList().isEmpty()){
    				// SVV brukere som pt. ikke har FKP-roller taes bort fra "Brukerliste".
    				usersWithRoles.add(u);
    			}
    		}
    		users = usersWithRoles;
        }
        else {
        	users = userManager.getUsers(user, false);
        }
        
        if (users != null) {
            model.put("userList", users);
        }
        
        return new ModelAndView(getSuccessView(), model);
    }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        // Get course from session
        HttpSession session = request.getSession();
        Object comm = session.getAttribute("user");
        
        if (comm == null) {
            comm = new User();
        }
        return comm;
    } 
    
 }

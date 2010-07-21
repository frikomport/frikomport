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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserExistsException;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.StringUtil;
import no.unified.soak.webapp.util.RequestUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserFormController extends BaseFormController {
    private RoleManager roleManager;
    private OrganizationManager organizationManager = null;
    private ServiceAreaManager serviceAreaManager = null;

    /**
     * @param organizationManager The organizationManager to set.
     */
    public void setOrganizationManager(OrganizationManager organizationManager) {
		this.organizationManager = organizationManager;
	}
    /**
     * @param serviceAreaManager The serviceManager to set.
     */
    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
		this.serviceAreaManager = serviceAreaManager;
	}

    /**
     * @param roleManager The roleManager to set.
     */
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
        Locale locale = request.getLocale();

        addOrganization(model,locale);
        addOrganization2(model,locale);
        
//      Retrieve all serviceareas into an array
    	List serviceAreas = serviceAreaManager.getAllIncludingDummy(getText("misc.none", locale));
    	if (serviceAreas != null) {
    		model.put("serviceareas", serviceAreas);
    	}

        return model;
    }
	

    public ModelAndView processFormSubmission(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (request.getParameter("cancel") != null) {
            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                return new ModelAndView(getCancelView());
            } else {
                return new ModelAndView(getSuccessView());
            }
        }

        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        HttpSession session = request.getSession();
        User user = (User) command;
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            this.getUserManager().removeUser(user.getUsername());
            saveMessage(request,
                getText("user.deleted", user.getFullName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
            if ("true".equals(request.getParameter("encryptPass"))) {
                String algorithm = (String) getConfiguration()
                                                .get(Constants.ENC_ALGORITHM);

                if (algorithm == null) { // should only happen for test case

                    if (log.isDebugEnabled()) {
                        log.debug(
                            "assuming testcase, setting algorithm to 'SHA'");
                    }

                    algorithm = "SHA";
                }

                user.setPassword(StringUtil.encodePassword(user.getPassword(),
                        algorithm));
            }

            String[] userRoles = request.getParameterValues("userRoles");

            if (userRoles != null) {
                // for some reason, Spring seems to hang on to the roles in
                // the User object, even though isSessionForm() == false
                user.getRoles().clear();

                for (int i = 0; i < userRoles.length; i++) {
                    String roleName = userRoles[i];
                    user.addRole(roleManager.getRole(roleName));
                }
            }

            try {
                this.getUserManager().saveUser(user);
                session.setAttribute("username", user.getUsername());
            } catch (UserExistsException e) {
                log.warn(e.getMessage());

                errors.rejectValue("username", "errors.existing.user",
                    new Object[] { user.getUsername(), user.getEmail() },
                    "duplicate user");

                // redisplay the unencrypted passwords
                user.setPassword(user.getConfirmPassword());

                return showForm2(request, response, errors);
            }

            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                session.setAttribute(Constants.USER_KEY, user);

                // update the user's remember me cookie if they didn't login
                // with a cookie
                if ((RequestUtil.getCookie(request, Constants.LOGIN_COOKIE) != null) &&
                        (session.getAttribute("cookieLogin") == null)) {
                    // delete all user cookies and add a new one
                    this.getUserManager().removeLoginCookies(user.getUsername());

                    String autoLogin = this.getUserManager()
                                           .createLoginCookie(user.getUsername());
                    RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                        autoLogin, request.getContextPath());
                }

                saveMessage(request, getText("user.saved", user.getFullName(), locale));

                // return to main Menu
//                return showForm(request, response, errors);
                return new ModelAndView(getSuccessView());
            } else {
                if (StringUtils.isBlank(request.getParameter("version"))) {
                    saveMessage(request,
                        getText("user.added", user.getFullName(), locale));

                    // Send an account information e-mail
                    message.setSubject(getText("signup.email.subject", locale));
                    sendUserMessage(user,
                        getText("newuser.email.message", user.getFullName(),
                            locale), RequestUtil.getAppURL(request));

                    return showNewForm(request, response);
                } else {
                    saveMessage(request,
                        getText("user.updated.byAdmin", user.getFullName(),
                            locale));
                }
            }
        }
        return new ModelAndView(getCancelView());
//        return showForm(request, response, errors);
    }

    protected ModelAndView showForm2(HttpServletRequest request,
        HttpServletResponse response, BindException errors)
        throws Exception {
        if (request.getRequestURI().indexOf("editProfile") > -1) {
            // if URL is "editProfile" - make sure it's the current user
            // reject if username passed in or "list" parameter passed in
            // someone that is trying this probably knows the AppFuse code
            // but it's a legitimate bug, so I'll fix it. ;-)
            if ((request.getParameter("username") != null) ||
                    (request.getParameter("from") != null)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() +
                    "' is trying to edit user '" +
                    request.getParameter("username") + "'");

                return null;
            }
        }

        // prevent ordinary users from calling a GET on editUser.html
        // unless a bind error exists.
        if ((request.getRequestURI().indexOf("editUser") > -1) &&
                (!request.isUserInRole(Constants.ADMIN_ROLE) &&
                (errors.getErrorCount() == 0) && // be nice to server-side validation for editProfile
                (request.getRemoteUser() != null))) { // be nice to unit tests
            response.sendError(HttpServletResponse.SC_FORBIDDEN);

            return null;
        }

        return super.showForm(request, response, errors);
    }

    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String username = request.getParameter("username");

        if (request.getSession().getAttribute("cookieLogin") != null) {
            saveMessage(request,
                getText("userProfile.cookieLogin", request.getLocale()));
        }

        User user = null;

        if (request.getRequestURI().indexOf("editProfile") > -1) {
            user = this.getUserManager().getUser(getUser(request).getUsername());
        } else if (!StringUtils.isBlank(username) &&
                !"".equals(request.getParameter("version"))) {
            user = this.getUserManager().getUser(username);
        } else {
            user = new User();
            user.addRole(new Role(Constants.DEFAULT_ROLE));
        }

        user.setConfirmPassword(user.getPassword());

        return user;
    }

    protected void onBind(HttpServletRequest request, Object command)
        throws Exception {
        // if the user is being deleted, turn off validation
        if (request.getParameter("delete") != null) {
            super.setValidateOnBinding(false);
        } else {
            super.setValidateOnBinding(true);
        }
    }

    private Map addOrganization(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        String typeDBvalue = ApplicationResourcesUtil.getText("show.organization.pulldown.typeDBvalue");
        if (typeDBvalue != null) {
        	Integer value = Integer.valueOf(typeDBvalue);
        	Type type = Organization.Type.getTypeFromDBValue(value);
            model.put("organizations", organizationManager.getByTypeIncludingDummy(type, getText("misc.all", locale)));
        } else {
            model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));
        }
        return model;
    }
    
    private Map addOrganization2(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

//        String typeDBvalue = ApplicationResourcesUtil.getText("show.organization.pulldown.typeDBvalue");
//        if (typeDBvalue != null) {
//            model.put("organizations", organizationManager.getByTypeIncludingDummy(Organization.Type.getTypeFromDBValue(Integer
//                    .getInteger(typeDBvalue)), getText("misc.all", locale)));
//        } else {
//            model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));
//        }
        
        model.put("organizations2", organizationManager.getByTypeIncludingParentAndDummy(Organization.Type.AREA, Organization.Type.REGION, getText("misc.all", locale)));
        return model;
    }

}

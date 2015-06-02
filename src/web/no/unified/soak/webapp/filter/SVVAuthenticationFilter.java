package no.unified.soak.webapp.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.User;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @web.filter display-name="SVV Authentication Filter" name="svvAuthenticationFilter"
 */
public class SVVAuthenticationFilter implements Filter {
    
    private FilterConfig config;
    
    private UserManager userManager;
    private UserSynchronizeManager userSynchronizeManager;
    private ExtUserDAO extUserDAO;

    protected final Log log = LogFactory.getLog(getClass());
    
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    public void setUserSynchronizeManager(UserSynchronizeManager userSynchronizeManager) {
        this.userSynchronizeManager = userSynchronizeManager;
    }
    public void setExtUserDAO(ExtUserDAO extUserDAO) {
        this.extUserDAO = extUserDAO;
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    public void destroy() {
        this.config = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // cast to the types I want to use
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(true);

        String usernameFromHTTPHeader = request.getHeader(Constants.USERID_HTTPHEADERNAME);
        
        // for IE testing
        // usernameFromHTTPHeader="extsam";
        
        String usernameFromSession = (String) session.getAttribute(Constants.USERID_HTTPHEADERNAME);
        ExtUser extUser = null;
        User user = null;

        if (StringUtils.isEmpty(usernameFromHTTPHeader)) {
            log.debug("header " + Constants.USERID_HTTPHEADERNAME + "=" + usernameFromHTTPHeader + " (ikke innlogget)");
        } else {
            log.debug("header " + Constants.USERID_HTTPHEADERNAME + "=" + usernameFromHTTPHeader + " (innlogget)");
        }

        //A different user-id from the one in the session object might come in the current http request.
        if (!StringUtils.equals(usernameFromHTTPHeader, usernameFromSession)) {
        	usernameFromSession = null;
        	session.setAttribute(Constants.USERID_HTTPHEADERNAME, null);
        }
        
        if (StringUtils.isNotBlank(usernameFromHTTPHeader)) {
            User userTmp = (User)session.getAttribute(Constants.USER_KEY);
			if (StringUtils.isEmpty(usernameFromSession) || !usernameFromSession.equals(usernameFromHTTPHeader) || userTmp == null
					|| !StringUtils.equals(userTmp.getUsername(), usernameFromHTTPHeader)) {

				try {
					extUser = extUserDAO.findUserByUsername(usernameFromHTTPHeader);
				}
				catch(Exception e){
					// problemer med tilkobling mot LDAP eller tolkning av resultat
				}

				if (extUser == null || StringUtils.isEmpty(extUser.getUsername())) {
					log.warn("No LDAP user found for username=[" + usernameFromHTTPHeader
							+ "] Cannot grant any roles to the presumed logged in user.");
				} else if (extUser.getRolenames().isEmpty() && userManager.getUserSilent(usernameFromHTTPHeader) == null) {
					// SVV-ansatt uten FKP-roller skal ikke opprettes i
					// databasen
				} else {
					user = copyUserToLocalDBAndSession(extUser, session);
				}
			} else {
				// Brukeren kan (kanskje) ha vært avlogget en kort stund og så
				// kommet tilbake med USER-ID i header, uten
				// at Tomcat-sesjonen har timet ut. Da må user atter settes for
				// å få tilbake rollesettingene i request
				// attribute'ene.
				user = userTmp;
			}
        } else if (StringUtils.isNotBlank(usernameFromSession)) {
            request.setAttribute(Constants.MESSAGES_INFO_KEY, Arrays
                    .asList("Din innlogging er utg&aring;tt. Vennligst logg inn p&aring;ny."));
        }

		if (user != null && isAdminPath(request)) {
			// Give logged in users access also as anonymous. 
//			if (!user.getRoleNameList().contains(RoleEnum.ANONYMOUS.getJavaDBRolename())) {
//				Role role = new Role(RoleEnum.ANONYMOUS.getJavaDBRolename());
//				user.addRole(role);
//			} // sa 01.09.2010 - Utkommentert da sjekk av roller i "CollectionUtils.isEqualCollection(userRoles, authorizedRoles)" i UserSecurityAdvice feiler
			Authentication authentificationToken = new SVVAuthentificationToken(user, usernameFromHTTPHeader);
			SecurityContextHolder.getContext().setAuthentication(authentificationToken);
			session.setAttribute(Constants.USER_KEY, user);
		} else {
			SecurityContextHolder.getContext().setAuthentication(null);
			session.setAttribute(Constants.USER_KEY, null);
			session.setAttribute(Constants.USERID_HTTPHEADERNAME, null);
		}

        chain.doFilter(request, response);
    }
    
    private boolean isAdminPath(HttpServletRequest request) {
    	String contextPath = request.getRequestURI();
    	if (contextPath.contains(ApplicationResourcesUtil.getPublicUrlContextAppendix())) {
    		return false;
    	}
    	return true;
	}

    private User copyUserToLocalDBAndSession(ExtUser extUser, HttpSession session) {
        User user = userSynchronizeManager.processUser(extUser, null);
		session.setAttribute(Constants.USERID_HTTPHEADERNAME, user.getUsername());
        session.setAttribute(Constants.USER_KEY, user);
        return user;
    }
}

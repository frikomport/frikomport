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

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
				extUser = extUserDAO.findUserByUsername(usernameFromHTTPHeader);
				if (extUser == null || StringUtils.isEmpty(extUser.getUsername())) {
					log.warn("No LDAP user found for username=[" + usernameFromHTTPHeader
							+ "] Cannot grant any roles to the presumed logged in user.");
				} else {
					user = copyUserToLocalDBAndSession(extUser, session);
					session.setAttribute(Constants.USERID_HTTPHEADERNAME, user.getUsername());
				}
			} else {
				// Brukeren kan (kanskje) ha v�rt avlogget en kort stund og s�
				// kommet tilbake med USER-ID i header, uten
				// at Tomcat-sesjonen har timet ut. Da m� user atter settes for
				// � f� tilbake rollesettingene i request
				// attribute'ene.
				user = userTmp;
			}
        } else if (StringUtils.isNotBlank(usernameFromSession)) {
            request.setAttribute(Constants.MESSAGES_INFO_KEY, Arrays
                    .asList("Din innlogging er utg&aring;tt. Vennligst logg inn p&aring;ny."));
        }

        if(user != null && isAdminPath(request)) {
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
    	String contextPath = request.getContextPath();
    	//TODO SSO-tilpasning inn her.
    	return true;
	}

    private User copyUserToLocalDBAndSession(ExtUser extUser, HttpSession session) {
        User user = userSynchronizeManager.processUser(extUser);
        session.setAttribute(Constants.USER_KEY, user);
        return user;
    }
}
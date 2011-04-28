package no.unified.soak.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.User;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.webapp.util.RequestUtil;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

public class EZAuthenticationFilter implements Filter {
    
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
        
        // notify the LocaleContextHolder what locale is being used so
        // service and data layer classes can get the locale
        LocaleContextHolder.setLocale(request.getLocale());
        
        ExtUser extUser = null;
        User user = null;
        boolean anonymous = true;

        /*
         * eZ publish reuses the session id when logging out and in as a
         * different user. Therefore we always need to check eZp to detect which
         * user are logged in with the session id.
         */
        Cookie cookie = RequestUtil.getCookie(request, "eZSESSID");
        String eZSessionId = null;
        if (cookie != null && cookie.getValue() != null && cookie.getValue().trim().length() > 0) {
            eZSessionId = cookie.getValue();
            extUser = extUserDAO.findUserBySessionID(cookie.getValue());
            if(extUser != null && extUser.getUsername() != null ){
                user = userSynchronizeManager.processUser(extUser, null);  // Added ", null" to
//                session.setAttribute(Constants.USER_KEY, user);
                anonymous = false;
            }
        }

        if(!anonymous) {
        	// Changed from (user, eZsessionID)
            Authentication authentificationToken = new EZAuthentificationToken(extUser, eZSessionId);
//            session.setAttribute("authenticationToken", authentificationToken);
            SecurityContextHolder.getContext().setAuthentication(authentificationToken);
            
            // if user wants to be remembered, create a remember me cookie
            if (session.getAttribute(Constants.LOGIN_COOKIE) != null) {
                session.removeAttribute(Constants.LOGIN_COOKIE);

                String loginCookie = userManager.createLoginCookie(user.getUsername());
                RequestUtil.setCookie(response, Constants.LOGIN_COOKIE, loginCookie, request.getContextPath());
            }
        } 

        chain.doFilter(request, response);
    }
}

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
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
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.webapp.util.RequestUtil;
import no.unified.soak.webapp.util.SslUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is used to filter all requests to the <code>Action</code> servlet and detect if a user is authenticated.
 * If a user is authenticated, but no user object exists, this class populates the <code>UserForm</code> from the user
 * store.
 * 
 * <p>
 * <a href="ActionFilter.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Matt Raible
 * @version $Revision: 1.23 $ $Date: 2006/05/10 11:38:16 $
 * 
 * @web.filter display-name="Action Filter" name="actionFilter"
 * 
 *             <p>
 *             Change this value to true if you want to secure your entire application. This can also be done in
 *             web-security.xml by setting <transport-guarantee> to CONFIDENTIAL.
 *             </p>
 * 
 * @web.filter-init-param name="isSecure" value="${secure.application}"
 */
public class ActionFilter implements Filter {
    private static Boolean secure = Boolean.FALSE;

    protected final Log log = LogFactory.getLog(getClass());

    private FilterConfig config = null;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;

        /* This determines if the application uconn SSL or not */
        secure = Boolean.valueOf(config.getInitParameter("isSecure"));
    }

    /**
     * Destroys the filter.
     */
    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // cast to the types I want to use
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(true);

        // notify the LocaleContextHolder what locale is being used so
        // service and data layer classes can get the locale
        LocaleContextHolder.setLocale(ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(request.getLocale()));

        // do pre filter work here
        // If using https, switch to http
        String redirectString = SslUtil.getRedirectString(request, config.getServletContext(), secure.booleanValue());

        if (redirectString != null) {
            if (log.isDebugEnabled()) {
                log.debug("protocol switch needed, redirecting to '" + redirectString + "'");
            }

            // Redirect the page to the desired URL
            response.sendRedirect(response.encodeRedirectURL(redirectString));

            // ensure we don't chain to requested resource
            return;
        }

        if (ApplicationResourcesUtil.isSVV()) {
            doHttpheaderAccessing(request, session);
            request.setAttribute("isSVV", Boolean.TRUE);
        } else {
            doEZAccessing(request, session);
            request.setAttribute("isSVV", Boolean.FALSE);
        }

        doConfiguration(request, session);

        User user = (User) session.getAttribute(Constants.USER_KEY);
        String username = request.getRemoteUser();

        // user authenticated, empty user object
        if ((username != null) && (user == null)) {
            UserManager mgr = (UserManager) getBean("userManager");
            user = mgr.getUser(username);
            session.setAttribute(Constants.USER_KEY, user);

            // if user wants to be remembered, create a remember me cookie
            if (session.getAttribute(Constants.LOGIN_COOKIE) != null) {
                session.removeAttribute(Constants.LOGIN_COOKIE);

                String loginCookie = mgr.createLoginCookie(username);
                RequestUtil.setCookie(response, Constants.LOGIN_COOKIE, loginCookie, request.getContextPath());
            }
        }

        chain.doFilter(request, response);
    }

    private void doConfiguration(HttpServletRequest request, HttpSession session) {
        ConfigurationManager configurationManager = (ConfigurationManager) getBean("configurationManager");

        // course
        session.setAttribute("showMenu", configurationManager.isActive("show.menu", false));
        session.setAttribute("singleprice", configurationManager.isActive("access.course.singleprice", false));
        session.setAttribute("usePayment", configurationManager.isActive("access.course.usePayment", true));
        session.setAttribute("showDuration", configurationManager.isActive("access.course.showDuration", true));
        session.setAttribute("showDescription", configurationManager.isActive("access.course.showDescription", true));
        session.setAttribute("showRole", configurationManager.isActive("access.course.showRole", true));
        session.setAttribute("showType", configurationManager.isActive("access.course.showType", true));
        session.setAttribute("showRestricted", configurationManager.isActive("access.course.showRestricted", true));

        
        // registration
        session.setAttribute("canDelete", configurationManager.isActive("access.registration.delete", false));
        session.setAttribute("userdefaults", configurationManager.isActive("access.registration.userdefaults", false));
        session.setAttribute("emailrepeat", configurationManager.isActive("access.registration.emailrepeat", false));
        session.setAttribute("showEmployeeFields", configurationManager.isActive("access.registration.showEmployeeFields", true));
        session.setAttribute("showServiceArea", configurationManager.isActive("access.registration.showServiceArea", true));
        session.setAttribute("showJobTitle", configurationManager.isActive("access.registration.showJobTitle", true));
        session.setAttribute("showWorkplace", configurationManager.isActive("access.registration.showWorkplace", true));
        session.setAttribute("showComment", configurationManager.isActive("access.registration.showComment", true));
        
        // profile
        session.setAttribute("showAddress", configurationManager.isActive("access.profile.showAddress", true));
        session.setAttribute("showInvoiceaddress", configurationManager.isActive("access.profile.showInvoiceaddress", true));
        
        // lists
        session.setAttribute("itemCount", configurationManager.getValue("list.itemCount", "25"));
    }

    private void doHttpheaderAccessing(HttpServletRequest request, HttpSession session) {

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
            if (StringUtils.isEmpty(usernameFromSession)) {
                ExtUserDAO extUserDAO = (ExtUserDAO) getBean("extUserDAO");
                extUser = extUserDAO.findUserByUsername(usernameFromHTTPHeader);
                if (extUser == null || StringUtils.isEmpty(extUser.getUsername())) {
                    log.warn("No LDAP user found for username=[" + usernameFromHTTPHeader
                            + "] Cannot grant any roles to the presumed logged in user.");
                } else {
                    user = copyUserToLocalDBAndSession(extUser, session);
                    session.setAttribute(Constants.USERID_HTTPHEADERNAME, user.getUsername());
                }
            } else {
                // Brukeren kan (kanskje ha vært avlogget en kort stund og så kommet tilbake med USER-ID i header, uten
                // at Tomcat-sesjonen har timet ut. Da må user atter settes for å få tilbake rollesettingene i request
                // attribute'ene.
                user = (User) session.getAttribute(Constants.USER_KEY);
            }
        } else if (StringUtils.isNotBlank(usernameFromSession)) {
            request.setAttribute(Constants.MESSAGES_INFO_KEY, Arrays
                    .asList("Din innlogging er utg&aring;tt. Vennligst logg inn p&aring;ny."));
        }

        setAcegiAutenticationToken(session, extUser, usernameFromHTTPHeader); // Vi antar at acegi trenger dette

        authenticateFromHash(request, session);

        setRoleRequestAttributes(request, user);
    }

    private void doEZAccessing(HttpServletRequest request, HttpSession session) {
        ExtUser extUser = new ExtUser();

        /*
         * eZ publish reuses the session id when logging out and in as a different user. Therefore we always need to
         * check eZp to detect which user are logged in with the session id.
         */
        Cookie cookie = RequestUtil.getCookie(request, "eZSESSID");
        String eZSessionId = null;
        if (cookie != null && cookie.getValue() != null && cookie.getValue().trim().length() > 0) {
            eZSessionId = cookie.getValue();
            ExtUserDAO extUserDAO = (ExtUserDAO) getBean("extUserDAO");
            extUser = extUserDAO.findUserBySessionID(eZSessionId);
            if (extUser != null && extUser.getUsername() != null) {
                copyUserToLocalDBAndSession(extUser, session);
            } else {
                log.info("No CMS (eZ publish) user found for eZSESSID=" + eZSessionId);
            }
        } else {
            extUser.setName("No cookie found.");
        }

        EZAuthentificationToken authentificationToken = setAcegiAutenticationToken(session, extUser, eZSessionId);

        authenticateFromHash(request, session);

        MessageSource messageSource = (MessageSource) getBean("messageSource");
        Locale locale = request.getLocale();

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        setRoleRequestAttributes(request, user);

        /* ezSessionid becomes null if not found. */
        request.setAttribute(messageSource.getMessage("cms.sessionid", null, locale), eZSessionId);

        if (eZSessionId != null && !authentificationToken.isAuthenticated()) {
            request.setAttribute(Constants.MESSAGES_INFO_KEY, Arrays
                    .asList("Din innlogging er utg&aring;tt. Vennligst logg inn p&aring;ny."));
        }
    }

    private EZAuthentificationToken setAcegiAutenticationToken(HttpSession session, ExtUser extUser, String eZSessionId) {
        EZAuthentificationToken authentificationToken = new EZAuthentificationToken(extUser, eZSessionId);

        session.setAttribute("authenticationToken", authentificationToken);
        return authentificationToken;
    }

    private void authenticateFromHash(HttpServletRequest request, HttpSession session) {
        String hash = request.getParameter("hash");
        if (hash != null & !StringUtils.isBlank(hash)) {
            UserManager mgr = (UserManager) getBean("userManager");
            User hashuser = mgr.getUserByHash(hash);
            session.setAttribute(Constants.ALT_USER_KEY, hashuser);
        }

        User userhash = (User) request.getSession().getAttribute(Constants.ALT_USER_KEY);
        if (userhash != null) {
            request.setAttribute("altusername", userhash.getUsername());
        }
    }

    private void setRoleRequestAttributes(HttpServletRequest request, User user) {
        if (user != null) {
            List<String> roleNameList = user.getRoleNameList();
            request.setAttribute("isCourseParticipant", roleNameList.contains(Constants.EMPLOYEE_ROLE));
            request.setAttribute("isReader", roleNameList.contains(Constants.READER_ROLE));
            request.setAttribute("isEventResponsible", roleNameList.contains(Constants.EVENTRESPONSIBLE_ROLE));
            request.setAttribute("isEducationResponsible", roleNameList.contains(Constants.EDITOR_ROLE));
            request.setAttribute("isAdmin", roleNameList.contains(Constants.ADMIN_ROLE));
            request.setAttribute("username", user.getUsername());
        } else {
            // User is not logged in, removing all role settings.
            request.setAttribute("isCourseParticipant", false);
            request.setAttribute("isReader", false);
            request.setAttribute("isEventResponsible", false);
            request.setAttribute("isEducationResponsible", false);
            request.setAttribute("isAdmin", false);
            request.setAttribute("username", null);
        }
    }

    private User copyUserToLocalDBAndSession(ExtUser extUser, HttpSession session) {
        UserSynchronizeManager userSynchronizeManager = (UserSynchronizeManager) getBean("userSynchronizeManager");
        User user = userSynchronizeManager.processUser(extUser);
        session.setAttribute(Constants.USER_KEY, user);
        return user;
    }

    private Object getBean(String beanId) {
        ServletContext context = config.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        return ctx.getBean(beanId);
    }

}

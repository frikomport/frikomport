/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.filter;

import java.io.IOException;
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
import no.unified.soak.dao.jdbc.EzUserDAOJdbc;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.webapp.util.RequestUtil;
import no.unified.soak.webapp.util.SslUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is used to filter all requests to the <code>Action</code>
 * servlet and detect if a user is authenticated. If a user is authenticated,
 * but no user object exists, this class populates the <code>UserForm</code>
 * from the user store.
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
 * <p>
 * Change this value to true if you want to secure your entire application. This
 * can also be done in web-security.xml by setting <transport-guarantee> to
 * CONFIDENTIAL.
 * </p>
 * 
 * @web.filter-init-param name="isSecure" value="${secure.application}"
 */
public class ActionFilter implements Filter {
	private static Boolean secure = Boolean.FALSE;

	private final transient Log log = LogFactory.getLog(ActionFilter.class);

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

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		// cast to the types I want to use
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(true);
		
		// notify the LocaleContextHolder what locale is being used so
		// service and data layer classes can get the locale
		LocaleContextHolder.setLocale(request.getLocale());

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

		doEZAccessing(request, session);

        doConfiguration(request, session);

		User user = (User) session.getAttribute(Constants.USER_KEY);
		String username = request.getRemoteUser();

		// user authenticated, empty user object
		if ((username != null) && (user == null)) {
			ApplicationContext ctx = getContext();

			UserManager mgr = (UserManager) ctx.getBean("userManager");
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

    private void doConfiguration(HttpServletRequest request, HttpSession session){
        ConfigurationManager configurationManager = (ConfigurationManager)getContext().getBean("configurationManager");

        session.setAttribute("showMenu", configurationManager.getValue("show.menu","false"));
        session.setAttribute("canDelete", configurationManager.getValue("access.registration.delete","false"));
        session.setAttribute("userdefaults", configurationManager.getValue("access.registration.userdefaults","false"));
        session.setAttribute("emailrepeat", configurationManager.getValue("access.registration.emailrepeat","false"));
    }

	private void doEZAccessing(HttpServletRequest request, HttpSession session) {
		EzUser ezUser = new EzUser();
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
			ezUser = (new EzUserDAOJdbc()).findUserBySessionID(cookie.getValue());
            if(ezUser != null && ezUser.getUsername() != null ){
                copyToUserTable(session, ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser
					.getEmail(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune());
                anonymous = false;
            }
		} else {
			ezUser.setName("No cookie found.");
		}

		EZAuthentificationToken authentificationToken = new EZAuthentificationToken(ezUser, eZSessionId);

		session.setAttribute("authenticationToken", authentificationToken);

		User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        String hash = request.getParameter("hash");
		if (hash != null & !StringUtils.isBlank(hash)) {
			UserManager mgr = (UserManager) getContext().getBean("userManager");
			User hashuser = mgr.getUserByHash(hash);
			session.setAttribute(Constants.ALT_USER_KEY, hashuser);
		}

        MessageSource messageSource = (MessageSource) getContext().getBean("messageSource");
		Locale locale = request.getLocale();
		if (user != null) {
			List<String> roleNameList = user.getRoleNameList();
			request.setAttribute("isCourseParticipant", roleNameList.contains(Constants.EMPLOYEE_ROLE));
			request.setAttribute("isCourseResponsible", roleNameList.contains(Constants.INSTRUCTOR_ROLE));
			request.setAttribute("isEducationResponsible", roleNameList.contains(Constants.EDITOR_ROLE));
			request.setAttribute("isAdmin", roleNameList.contains(Constants.ADMIN_ROLE));
            request.setAttribute("username", user.getUsername());
		}

        User userhash = (User) request.getSession().getAttribute(Constants.ALT_USER_KEY);
        if(userhash != null){
            request.setAttribute("altusername", userhash.getUsername());
        }


		
		/* ezSessionid becomes null if not found. */
		request.setAttribute(messageSource.getMessage("cms.sessionid", null, locale), eZSessionId);
		
		if (eZSessionId != null && !authentificationToken.isAuthenticated()) {
			request.setAttribute(Constants.MESSAGES_INFO_KEY,
					"Din innlogging er utg&aring;tt. Vennligst logg inn p&aring;ny.");
		}
	}

	private void copyToUserTable(HttpSession session, String username, String firstName, String lastName, String email,
			Integer id, List<String> rolenames, Integer kommune) {
		ApplicationContext ctx = getContext();
		UserManager userManager = (UserManager) ctx.getBean("userManager");
        RegistrationManager registrationManager = (RegistrationManager) ctx.getBean("registrationManager");
        User user = null;
        User user2 = null;
		try {
			user = userManager.getUser(username);
            user2 = userManager.findUser(email.toLowerCase());
            if(user2 != null && user2.getEnabled() && !user.getUsername().equals(user2.getUsername())){
                // user2 skal disablast og user overta påmeldinger
                registrationManager.moveRegistrations(user2, user);
                byttNavnOgDisable(user2, userManager);
            }
			userManager.updateUser(user, firstName, lastName, email, id, rolenames, kommune);
		} catch (ObjectRetrievalFailureException exception) {
			// User may be registered with email.
            user = userManager.findUser(email);
            if(user != null){
                // Endre epost til nokke som ikkje finnes
                byttNavnOgDisable(user, userManager);
                // Ny bruker basert på riktig brukernavn
                User newuser = userManager.addUser(username, firstName, lastName, email, id, rolenames, kommune);
                newuser.setHash(user.getHash());
                userManager.updateUser(newuser);
                // Flytt påmeldinger
                registrationManager.moveRegistrations(user, newuser);
            }
            else{
			    user = userManager.addUser(username, firstName, lastName, email, id, rolenames, kommune);
		    }
        }
		session.setAttribute(Constants.USER_KEY, user);

	}

    private void byttNavnOgDisable(User user, UserManager userManager) {
        String useremail = user.getEmail();
        user.setEmail(useremail.substring(0,useremail.indexOf('@')) + "@nonexist.no");
        user.setEnabled(false);
        userManager.updateUser(user);
    }

	private ApplicationContext getContext() {
		ServletContext context = config.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		return ctx;
	}
}

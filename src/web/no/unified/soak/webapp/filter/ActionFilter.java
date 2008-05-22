/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.filter;

import java.io.IOException;
import java.util.Iterator;
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
import no.unified.soak.dao.jdbc.UserEzDaoJdbc;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.Address;
import no.unified.soak.model.User;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.UserExistsException;
import no.unified.soak.service.UserManager;
import no.unified.soak.webapp.util.RequestUtil;
import no.unified.soak.webapp.util.SslUtil;

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

	private void doEZAccessing(HttpServletRequest request, HttpSession session) {
		EzUser ezUser = new EzUser();

		/*
		 * eZ publish reuses the session id when logging out and in as a
		 * different user. Therefore we always need to check eZp to detect which
		 * user are logged in with the session id.
		 */
		Cookie cookie = RequestUtil.getCookie(request, "eZSESSID");
		String eZSessionId = null;
		if (cookie != null && cookie.getValue() != null && cookie.getValue().trim().length() > 0) {
			eZSessionId = cookie.getValue();
			ezUser = (new UserEzDaoJdbc()).findUserBySessionID(cookie.getValue());
			copyToUserTable(ezUser, session);
		} else {
			ezUser.setName("No cookie found.");
		}

		EZAuthentificationToken authentificationToken = new EZAuthentificationToken(ezUser, eZSessionId);

		session.setAttribute("authenticationToken", authentificationToken);

		request.setAttribute("isCourseParticipant", ezUser.hasRolename(Constants.EZROLE_COURSEPARTICIPANTS));
		request.setAttribute("isCourseResponsible", ezUser.hasRolename(Constants.EZROLE_COURSERESPONSIBLE));
		request.setAttribute("isEducationResponsible", ezUser.hasRolename(Constants.EZROLE_EDUCATIONMANAGER));
		request.setAttribute("isAdmin", ezUser.hasRolename(Constants.EZROLE_ADMIN));
		// request.setAttribute("isAdmin", true);

		/* ezSessionid becomes null if not found. */
		request.setAttribute(Constants.EZ_SESSIONID, eZSessionId);

		/* ez_userid and ez_organization become null if not found. */
		if (eZSessionId != null && authentificationToken.isAuthenticated()) {
			request.setAttribute(Constants.EZ_USERID, ezUser.getId());
			request.setAttribute(Constants.EZ_ORGANIZATION, ezUser.getKommune());
			request.setAttribute(Constants.EZ_ROLES, ezUser.getRolenames());
			request.setAttribute(Constants.EZ_USER, ezUser);
		} else {
			request.setAttribute(Constants.EZ_USERID, null);
			request.setAttribute(Constants.EZ_ORGANIZATION, null);
			request.setAttribute(Constants.EZ_ROLES, null);
			request.setAttribute(Constants.EZ_USER, null);
		}

		if (eZSessionId != null && !authentificationToken.isAuthenticated()) {
			request.setAttribute(Constants.MESSAGES_INFO_KEY,
					"Din innlogging er utg&aring;tt. Vennligst logg inn p&aring;ny.");
		}
	}

	private void copyToUserTable(EzUser ezUser, HttpSession session) {
		ApplicationContext ctx = getContext();
		UserManager mgr = (UserManager) ctx.getBean("userManager");
		User user = null;
		try {
			user = mgr.getUser(ezUser.getUsername());
			if (!user.equals(ezUser)) {
				updateUser(ezUser, mgr, user, false);
				session.setAttribute(Constants.USER_KEY, user);
			}
		} catch (ObjectRetrievalFailureException exception) {
			// User does not exists, make new.
			user = new User(ezUser.getUsername());
			updateUser(ezUser, mgr, user, true);
			session.setAttribute(Constants.USER_KEY, user);
		}
	}

	private void updateUser(EzUser ezUser, UserManager mgr, User user, Boolean newUser) {
		user.setFirstName(ezUser.getFirst_name());
		user.setLastName(ezUser.getLast_name());
		user.setEmail(ezUser.getEmail());
		setRoles(ezUser, user);
		if (newUser) {
			Address address = new Address();
			address.setPostalCode("0");
			user.setAddress(address);
		}
		try {
			mgr.saveUser(user);
		} catch (UserExistsException e) {
			log.error("Exception: " + e);
		}
	}

	private void setRoles(EzUser ezUser, User user) {
		ApplicationContext ctx = getContext();
		RoleManager roleManager = (RoleManager) ctx.getBean("roleManager");
		MessageSource m = (MessageSource) ctx.getBean("messageSource");
		List<String> rolenames = ezUser.getRolenames();
		// remove existing roles before new ones are added.
		user.removeAllRoles();
		Locale locale = LocaleContextHolder.getLocale();

		for (Iterator iter = rolenames.iterator(); iter.hasNext();) {
			String rolename = (String) iter.next();
			if (rolename.equals(m.getMessage("role.employee", null, locale))) {
				user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
			} else if (rolename.equals(m.getMessage("role.anonymous", null, locale))) {
				user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
			} else if (rolename.equals(m.getMessage("role.editor", null, locale))) {
				user.addRole(roleManager.getRole(Constants.EDITOR_ROLE));
			} else if (rolename.equals(m.getMessage("role.admin", null, locale))) {
				user.addRole(roleManager.getRole(Constants.ADMIN_ROLE));
			} else if (ezUser.hasRolename(m.getMessage("role.instructor", null, locale))) {
				user.addRole(roleManager.getRole(Constants.INSTRUCTOR_ROLE));
			} else if (roleManager.getRole(rolename) != null) {
				user.addRole(roleManager.getRole(rolename));
			} else {
				no.unified.soak.model.Role role = new no.unified.soak.model.Role(rolename);
				roleManager.saveRole(role);
				user.addRole(role);
			}
		}
	}

	private ApplicationContext getContext() {
		ServletContext context = config.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		return ctx;
	}
}

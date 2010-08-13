/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import no.unified.soak.Constants;
import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.RoleEnum;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.StringUtil;
import no.unified.soak.webapp.util.RequestUtil;
import no.unified.soak.webapp.util.SslUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is used to filter all requests to the <code>Action</code> servlet
 * and detect if a user is authenticated. If a user is authenticated, but no
 * user object exists, this class populates the <code>UserForm</code> from the
 * user store.
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
 *             Change this value to true if you want to secure your entire
 *             application. This can also be done in web-security.xml by setting
 *             <transport-guarantee> to CONFIDENTIAL.
 *             </p>
 * 
 * @web.filter-init-param name="isSecure" value="${secure.application}"
 */
public class ActionFilter implements Filter {
	private static Boolean secure = Boolean.FALSE;

	protected final Log log = LogFactory.getLog(getClass());

	private FilterConfig config = null;

	private static CacheManager singletonCacheManager;

	private static String[] minimumPageDecoration = new String[] {
			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\r\n<head>\r\n", "</head>\r\n<body>\r\n",
			"</body>\r\n</html>\r\n" };

	public void init(FilterConfig config) throws ServletException {
		this.config = config;

		/* This determines if the application uconn SSL or not */
		secure = Boolean.valueOf(config.getInitParameter("isSecure"));

		singletonCacheManager = CacheManager.create();

		String tempdirPath = System.getProperty("java.io.tmpdir");
		if (!(tempdirPath.endsWith("/") || tempdirPath.endsWith("\\"))) {
			tempdirPath = tempdirPath + System.getProperty("file.separator");
		}
		tempdirPath += "non-lib-cache" + System.getProperty("file.separator");

		if (!singletonCacheManager.cacheExists("pageDecoration")) {
			int ttlSeconds = Math.round(Constants.TASK_RUN_INTERVAL_MILLISECOND / 1000);
			Cache pageDecorationCache = new Cache("pageDecoration", 3, true, false, ttlSeconds, ttlSeconds,  true, ttlSeconds * 2);
			pageDecorationCache.setDiskStorePath(tempdirPath);
			singletonCacheManager.addCache(pageDecorationCache);
		}
	}
	
	public static CacheManager getSingletonCacheManager() {
		return singletonCacheManager;
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
		ServletContext servletContext = session.getServletContext();

		ensurePageDecoration(servletContext);

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

		setJspLinkPathPrefix(request, session);
		chain.doFilter(request, response);
	}

	public void setJspLinkPathPrefix(HttpServletRequest request, HttpSession session) {
		String publicPrefix = StringUtils.strip(ApplicationResourcesUtil.getText("publicUrlprefix"), "/");
		String urlContextEnding = "/"+publicPrefix;
		if (StringUtils.isNotBlank(publicPrefix) && request.getRequestURI().contains(urlContextEnding)) {
			session.setAttribute("urlContext", request.getContextPath()+urlContextEnding);
			ApplicationResourcesUtil.setUrlContextAppendix(publicPrefix+"/");
		} else {
			session.setAttribute("urlContext", request.getContextPath());
			ApplicationResourcesUtil.setUrlContextAppendix("");
		}
	}
	
	private void ensurePageDecoration(ServletContext servletContext) {
		Cache pageDecorationCache = singletonCacheManager.getCache("pageDecoration");
		Element element1 = pageDecorationCache.get("pageDecorationBeforeHeadPleaceholder");
		Element element2 = pageDecorationCache.get("pageDecorationBetweenHeadAndBodyPleaceholders");
		Element element3 = pageDecorationCache.get("pageDecorationAfterBodyPleaceholder");
		String decorationUrl = ApplicationResourcesUtil.getText("global.pageDecorator.url");

		if (element1 == null || element2 == null || element3 == null) {
			log.warn("Not getting page decoration from ehcache. Trying to get it from " + decorationUrl);
		}

		if (element1 == null || element2 == null || element3 == null || pageDecorationCache.isExpired(element1)
				|| pageDecorationCache.isExpired(element2) || pageDecorationCache.isExpired(element3)) {

			String[] pageDecoration = fetchPageDecoration(decorationUrl, "http://localhost:7937/mengdetrening/placeholderWebpageEmulator.txthtml");
			if (ArrayUtils.getLength(pageDecoration) == 3 && StringUtils.isNotBlank(pageDecoration[0])
					&& StringUtils.isNotBlank(pageDecoration[1]) && StringUtils.isNotBlank(pageDecoration[2])) {
				pageDecorationCache.put(new Element("pageDecorationBeforeHeadPleaceholder", pageDecoration[0]));
				pageDecorationCache.put(new Element("pageDecorationBetweenHeadAndBodyPleaceholders", pageDecoration[1]));
				pageDecorationCache.put(new Element("pageDecorationAfterBodyPleaceholder", pageDecoration[2]));
				servletContext.setAttribute("pageDecorationBeforeHeadPleaceholder", pageDecoration[0]);
				servletContext.setAttribute("pageDecorationBetweenHeadAndBodyPleaceholders", pageDecoration[1]);
				servletContext.setAttribute("pageDecorationAfterBodyPleaceholder", pageDecoration[2]);
			}
		}
	}

	/**
	 * Fetches page decoration in three strings in a String array of three
	 * elements: <br>
	 * returnString[0]: Page decoration before head placeholder. <br>
	 * returnString[1]: Page decoration between head and body placeholders.<br>
	 * returnString[2]: Page decoration after body placeholder.<br>
	 * 
	 * The first base tag before head placeholder is left out of the returned
	 * page decoration.
	 * 
	 * @param decorationUrl
	 *            URL to fetch page decoration from.
	 * @param string
	 * @return null if problem occured fetching string from decorationUrl.
	 *         otherwise the fetched page decoration is returned.
	 */
	private String[] fetchPageDecoration(String decorationUrl, String backupDecorationUrl) {
		String headPlaceholder = ApplicationResourcesUtil.getText("global.pageDecorator.headPlaceholder");
		String bodyPlaceholder = ApplicationResourcesUtil.getText("global.pageDecorator.bodyPlaceholder");
		StringBuffer pageDecorationBeforeHeadPleaceholder = new StringBuffer(1000);
		StringBuffer pageDecorationBetweenHeadAndBodyPleaceholders = new StringBuffer(1000);
		StringBuffer pageDecorationAfterBodyPleaceholder = new StringBuffer(1000);

		HttpClient client = new HttpClient(new SimpleHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		String ctmpl = getStringFromUrl(decorationUrl, client);

		if (StringUtils.isBlank(ctmpl)) {
			log.warn("Failing getting page decoration from [" + decorationUrl+"]\nTrying url=["+backupDecorationUrl+"]");
			ctmpl = getStringFromUrl(backupDecorationUrl, client);
		}
		if (StringUtils.isBlank(ctmpl)) {
			log.warn("Failing getting page decoration from [" + backupDecorationUrl+"]\nUsing minimum page decoration.");
			return minimumPageDecoration;
		}

		int baseStartPos = ctmpl.indexOf("<base ");
		int baseEndPos = ctmpl.indexOf(">", baseStartPos + 1);
		int headStartPos = ctmpl.indexOf(headPlaceholder);
		int bodyStartPos = ctmpl.indexOf(bodyPlaceholder, headStartPos + 1);

		if (headStartPos == -1) {
			log.warn("Unable to find headPlaceholder [" + headPlaceholder + "] in received page decoration [" + ctmpl + "].");
			return minimumPageDecoration;
		}
		if (bodyStartPos == -1) {
			log.warn("Unable to find bodyPlaceholder [" + bodyPlaceholder + "] in received page decoration [" + ctmpl + "].");
			return minimumPageDecoration;
		}

		if (baseStartPos > -1 && baseStartPos < headStartPos) {
			// Avoid base tag and fetch everything from start to head
			// placeholder.
			pageDecorationBeforeHeadPleaceholder.append(ctmpl.substring(0, baseStartPos));
			pageDecorationBeforeHeadPleaceholder.append(ctmpl.substring(baseEndPos + 1, headStartPos));
		} else {
			// Base tag is void, do fetch everything from start to head
			// placeholder.
			pageDecorationBeforeHeadPleaceholder.append(ctmpl.substring(0, headStartPos));
		}

		if (baseStartPos > headStartPos) {
			pageDecorationBetweenHeadAndBodyPleaceholders.append(ctmpl.substring(headStartPos + headPlaceholder.length(),
					baseStartPos));
			pageDecorationBetweenHeadAndBodyPleaceholders.append(ctmpl.substring(baseEndPos + 1, bodyStartPos));
		} else {
			pageDecorationBetweenHeadAndBodyPleaceholders.append(ctmpl.substring(headStartPos + headPlaceholder.length(),
					bodyStartPos));
		}

		pageDecorationAfterBodyPleaceholder.append(ctmpl.substring(bodyStartPos + bodyPlaceholder.length()));

		return new String[] { pageDecorationBeforeHeadPleaceholder.toString(),
				pageDecorationBetweenHeadAndBodyPleaceholders.toString(), pageDecorationAfterBodyPleaceholder.toString() };
	}

	private String getStringFromUrl(String decorationUrl, HttpClient client) {
		if (StringUtils.isEmpty(decorationUrl)) {
			log.warn("Page decoration Url is blank [" + decorationUrl + "] so no page decoration can be fetched.");
			return null;
		}
		GetMethod getMethod = new GetMethod(decorationUrl);
		getMethod.setFollowRedirects(true);
		int resultCode;
		boolean failureFetching = true;
		String ctmpl = null;
		try {
			resultCode = client.executeMethod(getMethod);
			InputStream ctmplStream = getMethod.getResponseBodyAsStream();
			ctmpl = StringUtil.convertStreamToString(ctmplStream, "UTF8");
			if (StringUtils.isBlank(ctmpl) || resultCode != 200) {
				log.warn("Page decoration is blank [" + ctmpl + "] with resultcode=" + resultCode
						+ ". Unable to fetch page decoration from url " + decorationUrl + ".");
				failureFetching = true;
			} else {
				failureFetching = false;
			}
		} catch (HttpException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn("Error connecting to [" + decorationUrl + "]: " + e);
		} finally {
			getMethod.releaseConnection();
		}
		if (!failureFetching) {
			return ctmpl;
		}
		return null;
	}

	private void doConfiguration(HttpServletRequest request, HttpSession session) {
		ConfigurationManager configurationManager = (ConfigurationManager) getBean("configurationManager");

		session.setAttribute("showMenu", configurationManager.isActive("show.menu", false));

		// course
		session.setAttribute("singleprice", configurationManager.isActive("access.course.singleprice", false));
		session.setAttribute("usePayment", configurationManager.isActive("access.course.usePayment", true));
		session.setAttribute("showDuration", configurationManager.isActive("access.course.showDuration", true));
		session.setAttribute("showDescription", configurationManager.isActive("access.course.showDescription", true));
		session.setAttribute("showDescriptionToPublic", configurationManager
				.isActive("access.course.showDescriptionToPublic", true));
		session.setAttribute("showRole", configurationManager.isActive("access.course.showRole", true));
		session.setAttribute("showType", configurationManager.isActive("access.course.showType", true));
		session.setAttribute("showRestricted", configurationManager.isActive("access.course.showRestricted", true));
		session.setAttribute("useServiceArea", configurationManager.isActive("access.course.useServiceArea", true));
		session.setAttribute("showCourseName", configurationManager.isActive("access.course.showCourseName", true));
		session.setAttribute("useAttendants", configurationManager.isActive("access.course.useAttendants", false));
		session.setAttribute("useRegisterBy", configurationManager.isActive("access.course.useRegisterBy", true));
		session.setAttribute("useOrganization2", configurationManager.isActive("access.course.useOrganization2", false));
		session.setAttribute("showAttendantDetails", configurationManager.isActive("access.course.showAttendantDetails", false));

		// registration
		session.setAttribute("canDelete", configurationManager.isActive("access.registration.delete", false));
		session.setAttribute("userdefaults", configurationManager.isActive("access.registration.userdefaults", false));
		session.setAttribute("emailrepeat", configurationManager.isActive("access.registration.emailrepeat", false));
		session.setAttribute("showEmployeeFields", configurationManager.isActive("access.registration.showEmployeeFields", true));
		session.setAttribute("showServiceArea", configurationManager.isActive("access.registration.showServiceArea", true));
		session.setAttribute("showJobTitle", configurationManager.isActive("access.registration.showJobTitle", true));
		session.setAttribute("showWorkplace", configurationManager.isActive("access.registration.showWorkplace", true));
		session.setAttribute("showComment", configurationManager.isActive("access.registration.showComment", true));
		session.setAttribute("useBirthdate", configurationManager.isActive("access.registration.useBirthdate", false));

		// profile
		session.setAttribute("showAddress", configurationManager.isActive("access.profile.showAddress", true));
		session.setAttribute("showInvoiceaddress", configurationManager.isActive("access.profile.showInvoiceaddress", true));

		// lists
		session.setAttribute("itemCount", configurationManager.getValue("list.itemCount", "50"));
	}

	private void doHttpheaderAccessing(HttpServletRequest request, HttpSession session) {
		authenticateFromHash(request, session);
		User user = (User) session.getAttribute(Constants.USER_KEY);
		setRoleRequestAttributes(request, user);
	}

	private void doEZAccessing(HttpServletRequest request, HttpSession session) {
		ExtUser extUser = new ExtUser();

		/*
		 * eZ publish reuses the session id when logging out and in as a
		 * different user. Therefore we always need to check eZp to detect which
		 * user are logged in with the session id.
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

			ExtUserDAO extUserDAO = (ExtUserDAO) getBean("extUserDAO");
			ArrayList<String> roleListTranslated = new ArrayList<String>();
			for (String roleDBString : roleNameList) {
				// There is no reason to show the confusing anonymous role to
				// the user.
				if (!roleDBString.equals(RoleEnum.ANONYMOUS.getJavaDBRolename())) {
					roleListTranslated.add(extUserDAO.getStringForRole(RoleEnum.getRoleEnumFromJavaDBRolename(roleDBString)));
				}
			}
			request.setAttribute("userRolesString", StringUtils.join(roleListTranslated.iterator(), ", "));
		} else {
			// User is not logged in, removing all role settings.
			request.setAttribute("isCourseParticipant", false);
			request.setAttribute("isReader", false);
			request.setAttribute("isEventResponsible", false);
			request.setAttribute("isEducationResponsible", false);
			request.setAttribute("isAdmin", false);
			request.setAttribute("username", null);
			request.setAttribute("userRolesString", null);
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

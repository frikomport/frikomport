/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.webapp.util.SslUtil;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
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

        doConfiguration(request, session);

		chain.doFilter(request, response);
	}

    private void doConfiguration(HttpServletRequest request, HttpSession session){
        ConfigurationManager configurationManager = (ConfigurationManager)getContext().getBean("configurationManager");

        session.setAttribute("showMenu", configurationManager.isActive("show.menu",false));
        session.setAttribute("canDelete", configurationManager.isActive("access.registration.delete",false));
        session.setAttribute("userdefaults", configurationManager.isActive("access.registration.userdefaults",false));
        session.setAttribute("emailrepeat", configurationManager.isActive("access.registration.emailrepeat",false));
        session.setAttribute("showEmployeeFields", configurationManager.isActive("access.registration.showEmployeeFields",true));
        session.setAttribute("showServiceArea", configurationManager.isActive("access.registration.showServiceArea",true));
        session.setAttribute("showComment", configurationManager.isActive("access.registration.showComment",true));
        session.setAttribute("itemCount", configurationManager.getValue("list.itemCount", "25"));
        
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (user != null) {
            request.setAttribute("username", user.getUsername());
        }
        
        String hash = request.getParameter("hash");
        if (hash != null & !StringUtils.isBlank(hash)) {
            UserManager mgr = (UserManager) getContext().getBean("userManager");
            User hashuser = mgr.getUserByHash(hash);
            session.setAttribute(Constants.ALT_USER_KEY, hashuser);
        }
    }



	private ApplicationContext getContext() {
		ServletContext context = config.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		return ctx;
	}
}

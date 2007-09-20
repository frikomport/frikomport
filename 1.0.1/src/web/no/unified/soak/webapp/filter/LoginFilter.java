/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.filter;

import no.unified.soak.Constants;
import no.unified.soak.model.User;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.StringUtil;
import no.unified.soak.webapp.util.RequestUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>Intercepts Login requests for "Remember Me" functionality.</p>
 *
 * <p>
 * <a href="LoginFilter.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2006/02/08 21:35:43 $
 *
 * @web.filter display-name="Login Filter" name="loginFilter"
 * @web.filter-init-param name="enabled" value="${rememberMe.enabled}"
 */
public final class LoginFilter implements Filter {
    private transient final Log log = LogFactory.getLog(LoginFilter.class);
    private FilterConfig config = null;
    private boolean enabled = true;

    public void doFilter(ServletRequest req, ServletResponse resp,
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // See if the user has a remember me cookie
        Cookie c = RequestUtil.getCookie(request, Constants.LOGIN_COOKIE);

        WebApplicationContext context = (WebApplicationContext) config.getServletContext()
                                                                      .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        UserManager mgr = (UserManager) context.getBean("userManager");

        // Check to see if the user is logging out, if so, remove all
        // login cookies
        if ((request.getRequestURI().indexOf("logout") != -1) &&
                (request.getRemoteUser() != null)) {
            // make sure user's session hasn't timed out
            if (request.getRemoteUser() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("logging out '" + request.getRemoteUser() + "'");
                }

                mgr.removeLoginCookies(request.getRemoteUser());
                RequestUtil.deleteCookie(response, c, request.getContextPath());
                request.getSession().invalidate();
            }
        } else if ((c != null) && enabled) {
            String loginCookie = mgr.checkLoginCookie(c.getValue());

            if (loginCookie != null) {
                RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                    loginCookie, request.getContextPath());
                loginCookie = StringUtil.decodeString(loginCookie);

                String[] value = StringUtils.split(loginCookie, '|');

                User user = mgr.getUser(value[0]);

                // authenticate user without displaying login page
                String route = "/authorize?j_username=" + user.getUsername() +
                    "&j_password=" + user.getPassword();

                request.setAttribute("encrypt", "false");
                request.getSession(true).setAttribute("cookieLogin", "true");

                if (log.isDebugEnabled()) {
                    log.debug("I remember you '" + user.getUsername() +
                        "', attempting to authenticate...");
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher(route);
                dispatcher.forward(request, response);

                return;
            }
        }

        chain.doFilter(req, resp);
    }

    /**
     * Initialize controller values of filter.
     */
    public void init(FilterConfig config) {
        this.config = config;

        String param = config.getInitParameter("enabled");
        enabled = Boolean.valueOf(param).booleanValue();

        if (log.isDebugEnabled()) {
            log.debug("Remember Me enabled: " + enabled);
        }

        config.getServletContext()
              .setAttribute("rememberMeEnabled",
            config.getInitParameter("enabled"));
    }

    /**
     * destroy any instance values other than config *
     */
    public void destroy() {
    }
}

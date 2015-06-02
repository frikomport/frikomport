/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import no.unified.soak.Constants;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter to wrap request with a request including user preferred locale.
 *
 * @web.filter display-name="Locale Filter" name="localeFilter"
 */
public class LocaleFilter extends OncePerRequestFilter {
    protected final Log log = LogFactory.getLog(getClass());

    public void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String locale = null;
        Locale preferredLocale = null;

        // Figure out if locale is overridden on application level from within web.xml
        HttpSession session = request.getSession(true);

        if (session != null) {
            ServletContext ctx = session.getServletContext();

            if (ctx != null) {
                locale = ctx.getInitParameter(Constants.OVERRIDE_LOCALE_KEY);
            }
        }

        // Get browser's locale if not overridden
        if (locale == null) {
            locale = request.getParameter("locale");
        }

        if (locale != null) {
            preferredLocale = ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(new Locale(locale));
        }

        if (session != null) {
            if (preferredLocale == null) {
                preferredLocale = (Locale) session.getAttribute(Constants.PREFERRED_LOCALE_KEY);
            } else {
                session.setAttribute(Constants.PREFERRED_LOCALE_KEY,
                    preferredLocale);
                Config.set(session, Config.FMT_LOCALE, preferredLocale);
            }

            if ((preferredLocale != null) &&
                    !(request instanceof LocaleRequestWrapper)) {
                request = new LocaleRequestWrapper(request, preferredLocale);
                LocaleContextHolder.setLocale(preferredLocale);
            }
        }

        chain.doFilter(request, response);

        // Reset thread-bound LocaleContext.
        LocaleContextHolder.setLocaleContext(null);
    }
}

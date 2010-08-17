/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/**
 *
 */
package no.unified.soak.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import no.unified.soak.webapp.filter.ActionFilter;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author kst
 * 
 */
public class PageDecorationCacheController extends ParameterizableViewController {
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CacheManager singletonCacheManager = ActionFilter.getSingletonCacheManager();
		Cache pageDecorationCache = singletonCacheManager.getCache("pageDecoration");
		if (pageDecorationCache != null) {
			Element element1 = pageDecorationCache.get("pageDecorationBeforeHeadPleaceholder");
			Element element2 = pageDecorationCache.get("pageDecorationBetweenHeadAndBodyPleaceholders");
			Element element3 = pageDecorationCache.get("pageDecorationAfterBodyPleaceholder");
			element1.setTimeToLive(1);
			element2.setTimeToLive(1);
			element3.setTimeToLive(1);
		}
		response.setContentType("text/html");

		return new ModelAndView();
	}

}

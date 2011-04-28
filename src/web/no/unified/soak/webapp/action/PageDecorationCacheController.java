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

import no.unified.soak.service.DecorCacheManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author kst
 * 
 */
public class PageDecorationCacheController extends ParameterizableViewController {

	private DecorCacheManager decorCacheManager = null;
	
	public void setDecorCacheManager(DecorCacheManager decorCacheManager) {
		this.decorCacheManager = decorCacheManager;
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// updates decoration independently from recurring taskinterval
		decorCacheManager.updateDecoration();
		decorCacheManager.getDecorElements();
		response.setContentType("text/html");
		return new ModelAndView();
	}

}

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * created 14. dec 2005
 */
package no.unified.soak.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.Constants;
import no.unified.soak.service.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


/**
 * Controller class for Person. Creates a view with a list of all
 * Persons (people).
 *
 * @author hrj
 */
public class ResponsibleController implements Controller {
    private final Log log = LogFactory.getLog(ResponsibleController.class);
    private UserManager userManager = null;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        List responsibles = userManager.getResponsibles();
        
        return new ModelAndView("responsibleList", Constants.RESPONSIBLE_LIST, responsibles);
    }

}

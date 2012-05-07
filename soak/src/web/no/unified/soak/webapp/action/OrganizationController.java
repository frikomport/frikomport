/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 13.des.2005
 */
package no.unified.soak.webapp.action;

import no.unified.soak.Constants;
import no.unified.soak.service.OrganizationManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controller class for Organizations. Creates a view with a list of all
 * Organizations.
 *
 * @author hrj
 */
public class OrganizationController implements Controller {
    private final Log log = LogFactory.getLog(OrganizationController.class);
    private OrganizationManager mgr = null;

    public void setOrganizationManager(OrganizationManager mgr) {
        this.mgr = mgr;
    }

    /**
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse reponse) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        return new ModelAndView("organizationList",
            Constants.ORGANIZATION_LIST, mgr.getAllIncludingDisabled());
    }
}

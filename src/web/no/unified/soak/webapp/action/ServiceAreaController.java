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

import no.unified.soak.Constants;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.ServiceAreaManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controller class for ServiceArea. Creates a view with a list of all
 * ServiceAreas.
 *
 * @author hrj
 */
public class ServiceAreaController implements Controller {
    private final Log log = LogFactory.getLog(ServiceAreaController.class);
    private ServiceAreaManager serviceAreaManager = null;

    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        ServiceArea serviceArea = new ServiceArea();
        // populate object with request parameters
        BeanUtils.populate(serviceArea, request.getParameterMap());

        List serviceAreas = serviceAreaManager.getAllIncludingDisabled();

        return new ModelAndView("serviceAreaList", Constants.SERVICEAREA_LIST,
            serviceAreas);
    }
}

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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.ServiceAreaManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller class for ServiceArea. Creates a view with a list of all
 * ServiceAreas.
 *
 * @author hrj
 */
public class ServiceAreaController extends BaseFormController {
    private final Log log = LogFactory.getLog(ServiceAreaController.class);
    private ServiceAreaManager serviceAreaManager = null;
    private OrganizationManager organizationManager = null;

    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }

    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Locale locale = request.getLocale();

        HttpSession session = request.getSession();
        ServiceArea comm = (ServiceArea)session.getAttribute("serviceArea");

        ServiceArea serviceArea = new ServiceArea();

        if(comm != null){
            serviceArea = comm;
        }

                // Don't modify organization if in postback
        String postback = request.getParameter("ispostbackservicearealist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
        	// Check if a specific organization has been requested
	        String mid = request.getParameter("mid");
	        if ((mid != null) && StringUtils.isNumeric(mid)) {
	            serviceArea.setOrganizationid(new Long(mid));
	        }
        }
        // Set up parameters, and return them to the view
        Map model = new HashMap();
        model = addOrganizations(model, locale);
        model.put("serviceArea",serviceArea);
        session.setAttribute("serviceArea", serviceArea);

        List serviceAreas = serviceAreaManager.searchServiceAreas(serviceArea);
        model.put("serviceAreaList",serviceAreas);

        return model;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
                                    Object command, BindException e) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        HttpSession session = request.getSession();

        Map model = new HashMap();

        Locale locale = request.getLocale();

        ServiceArea serviceArea = (ServiceArea) command;

        // Set up parameters, and return them to the view
        model = addOrganizations(model, locale);
        model.put("serviceArea", serviceArea);
        session.setAttribute("serviceArea", serviceArea);

        // Add all courses to the list
        List serviceAreas = serviceAreaManager.searchServiceAreas(serviceArea);
        model.put("serviceAreaList",serviceAreas);

        return new ModelAndView(getSuccessView(), model);
    }

    /**
     * Used to create a list of all organization with an option 0 that says
     * "all organization" and is therefore made with search forms in mind.
     *
     * @param model
     *            model to send to view
     * @param locale
     *            currently used locale
     * @return map with all organization and one with id=0 that is "all
     *         organization"
     */
    private Map addOrganizations(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));

        return model;
    }
}

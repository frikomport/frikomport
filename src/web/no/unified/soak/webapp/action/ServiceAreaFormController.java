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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.ServiceAreaManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


/**
 * Implementation of SimpleFormController that interacts with the
 * ServiceAreaManager to retrieve/persist values to the database.
 *
 * @author hrj
 */
public class ServiceAreaFormController extends BaseFormController {
    private ServiceAreaManager serviceAreaManager = null;
    private OrganizationManager organizationManager = null;

    /**
     * @param serviceAreaManager 
     */
    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }
    
    /**
     * @param organizationManager 
     */
    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String id = request.getParameter("id");
        ServiceArea serviceArea = null;

        if (!StringUtils.isEmpty(id)) {
            serviceArea = serviceAreaManager.getServiceArea(id);
        } else {
            serviceArea = new ServiceArea();
        }

        return serviceArea;
    }
    
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

        model.put("organizations", organizationManager.getAll());
        
        return model;
    }



    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        ServiceArea serviceArea = (ServiceArea) command;
        boolean isNew = (serviceArea.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            serviceAreaManager.removeServiceArea(serviceArea.getId().toString());

            saveMessage(request, getText("serviceArea.deleted", locale));
        } else {
            serviceAreaManager.saveServiceArea(serviceArea);

            String key = (isNew) ? "serviceArea.added" : "serviceArea.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editServiceArea.html", "id", serviceArea.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}

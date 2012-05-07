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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Organization;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


/**
 * Implementation of SimpleFormController that interacts with the
 * OrganizationManager to retrieve/persist values to the database.
 *
 * @author hrj
 */
public class OrganizationFormController extends BaseFormController {
    private OrganizationManager organizationManager = null;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        List<Type> types = new LinkedList<Type>();

        for (Type type : Organization.Type.values()) {
            types.add(type);
        }
        model.put("types", types);

        return model;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String id = request.getParameter("id");
        Organization organization = null;

        if (!StringUtils.isEmpty(id)) {
            organization = organizationManager.getOrganization(new Long(id));
        } else {
            organization = new Organization();
        }

        return organization;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Organization organization = (Organization) command;
        boolean isNew = (organization.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            organizationManager.removeOrganization(organization.getId());
            saveMessage(request, getText("organization.deleted", locale));
        }
        else {
        	
        	Object[] args = null;

			String navn = request.getParameter("name");
			if(!StringUtils.isNotEmpty(navn)){
				args = new Object[] { getText("organization.name", request.getLocale()), "", ""};
				errors.rejectValue("name", "errors.required", args, "");
			}

			String number = request.getParameter("number");
			if("".equals(number)){ number = null; } // fordi isNumberic for "" er true..!
			if(!StringUtils.isNumeric(number)){
				args = new Object[] { getText("organization.number", request.getLocale()), "", ""};
				errors.rejectValue("number", "errors.positivNumber", args, "");
			}

			if (args != null) {
				organizationManager.evict(organization);
				return showForm(request, response, errors);
			}
        	
        	if (organization.getType() == null && !ApplicationResourcesUtil.isSVV()) {
				organization.setType(Organization.Type.MUNICIPALITY);
			}
        	
            organizationManager.saveOrganization(organization);

            String key = (isNew) ? "organization.added" : "organization.updated";
            saveMessage(request, getText(key, locale));
//
//            if (!"list".equals(request.getAttribute("from"))) {
//                return new ModelAndView("redirect:detailsOrganization.html", "id", organization.getId());
//            }
        }

        return new ModelAndView(getSuccessView());
    }
}

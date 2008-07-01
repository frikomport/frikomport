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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Organization;
import no.unified.soak.service.OrganizationManager;

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
    private OrganizationManager mgr = null;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setOrganizationManager(OrganizationManager mgr) {
        this.mgr = mgr;
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
            organization = mgr.getOrganization(new Long(id));
        } else {
            organization = new Organization();
        }

        return organization;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    public ModelAndView processFormSubmission(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(getSuccessView());
        }

        return super.processFormSubmission(request, response, command, errors);
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
            mgr.removeOrganization(organization.getId());
            saveMessage(request, getText("organization.deleted", locale));
        } else {
            mgr.saveOrganization(organization);

            String key = (isNew) ? "organization.added"
                                 : "organization.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editOrganization.html", "id", organization.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}

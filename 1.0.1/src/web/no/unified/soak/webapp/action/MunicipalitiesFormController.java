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

import no.unified.soak.model.Municipalities;
import no.unified.soak.service.MunicipalitiesManager;

import org.apache.commons.lang.StringUtils;

import org.springframework.validation.BindException;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of SimpleFormController that interacts with the
 * MunicipalitiesManager to retrieve/persist values to the database.
 *
 * @author hrj
 */
public class MunicipalitiesFormController extends BaseFormController {
    private MunicipalitiesManager mgr = null;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setMunicipalitiesManager(MunicipalitiesManager mgr) {
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
        Municipalities municipalities = null;

        if (!StringUtils.isEmpty(id)) {
            municipalities = mgr.getMunicipalities(new Long(id));
        } else {
            municipalities = new Municipalities();
        }

        return municipalities;
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

        Municipalities municipalities = (Municipalities) command;
        boolean isNew = (municipalities.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            mgr.removeMunicipalities(municipalities.getId());
            saveMessage(request, getText("municipalities.deleted", locale));
        } else {
            mgr.saveMunicipalities(municipalities);

            String key = (isNew) ? "municipalities.added"
                                 : "municipalities.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                success = "editMunicipalities.html?id=" +
                    municipalities.getId();
            }
        }

        return new ModelAndView(getSuccessView());
    }
}

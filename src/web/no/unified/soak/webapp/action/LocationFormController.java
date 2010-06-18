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
import no.unified.soak.model.Location;
import no.unified.soak.model.User;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.OrganizationManager;

import org.apache.commons.lang.StringUtils;

import org.springframework.validation.BindException;

import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of SimpleFormController that interacts with the
 * LocationManager to retrieve/persist values to the database.
 *
 * @author hrj
 */
public class LocationFormController extends BaseFormController {
    private LocationManager locationManager = null;
    private OrganizationManager organizationManager = null;

    /**
    * @param roleManager
    *            The roleManager to set.
    */
    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public void setOrganizationManager(
        OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest request)
        throws Exception {
        Map<String, List> parameters = new HashMap<String, List>();

        // Retrieve all organization into an array
        List organization = organizationManager.getAll();

        if (organization != null) {
            parameters.put("organizations", organization);
        }

        return parameters;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String id = request.getParameter("id");
        Location location = null;

        if (!StringUtils.isEmpty(id)) {
            location = locationManager.getLocation(id);
        } else {
            location = new Location();
	        // Check if a default organization should be applied
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Object omid = user.getOrganizationid();
	        if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
	            location.setOrganizationid(new Long(omid.toString()));
	        }
        }

        return location;
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

        Location location = (Location) command;
        boolean isNew = (location.getId() == null);
        Locale locale = request.getLocale();


        if (request.getParameter("delete") != null) {
            locationManager.removeLocation(location.getId().toString());

            saveMessage(request, getText("location.deleted", locale));
        } else {
            locationManager.saveLocation(location);

            String key = (isNew) ? "location.added" : "location.updated";
            saveMessage(request, getText(key, locale));

            if (!"list".equals(request.getParameter("from"))) {
                return new ModelAndView("redirect:detailsLocation.html", "id", location.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}

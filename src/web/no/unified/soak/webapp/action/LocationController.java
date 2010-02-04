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

import no.unified.soak.model.Location;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.OrganizationManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller class for Location. Creates a view with a list of all
 * Locations.
 *
 * @author ceg
 */
public class LocationController extends BaseFormController {
    private LocationManager locationManager = null;
    private OrganizationManager organizationManager = null;

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public void setOrganizationManager(
        OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest request, Object command,
        Errors errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method...");
        }
        HttpSession session = request.getSession();
        Location location = (Location)session.getAttribute("location");
        
        Locale locale = request.getLocale();

        if (location != null) {
            location = new Location();
        }
        
        // Don't modify organization if in postback
        String postback = request.getParameter("ispostbacklocationlist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
        	// Check if a specific organization has been requested
	        String mid = request.getParameter("mid");
	        if ((mid != null) && StringUtils.isNumeric(mid)) {
	            location.setOrganizationid(new Long(mid));
	        }
        }

        // Set up parameters, and return them to the view
        Map model = new HashMap();
        model = addOrganizations(model, locale);
        model.put("location", location);

        // Add all locations to the list
        List locations = locationManager.searchLocations(location);

        if (locations != null) {
            model.put("locationList", locations);
        }

        return model;
    }

    /**
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
        
        HttpSession session = request.getSession();

        Map model = new HashMap();

        Locale locale = request.getLocale();

        Location location = (Location) command;

        // Set up parameters, and return them to the view
        model = addOrganizations(model, locale);
        model.put("location", location);
        session.setAttribute("location", location);

        // Add all courses to the list
        List locations = locationManager.searchLocations(location);
        model.put("locationList", locations);

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

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

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Location;
import no.unified.soak.model.Organization;
import no.unified.soak.model.User;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.FollowupManager;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.PostalCodesSuperduperLoader;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


/**
 * Implementation of SimpleFormController that interacts with the
 * LocationManager to retrieve/persist values to the database.
 *
 * @author hrj
 */
public class LocationFormController extends BaseFormController {
    private LocationManager locationManager = null;
    private OrganizationManager organizationManager = null;
    private CourseManager courseManager = null;
    private FollowupManager followupManager = null;

    
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setFollowupManager(FollowupManager followupManager) {
        this.followupManager = followupManager;
    }

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
    	Locale locale = request.getLocale();

    	Map<String, List> parameters = new HashMap<String, List>();

        String typeDBvalue = ApplicationResourcesUtil.getText("show.organization.pulldown.typeDBvalue");
        if (typeDBvalue != null) {
        	Integer value = Integer.valueOf(typeDBvalue);
        	Type type = Organization.Type.getTypeFromDBValue(value);
        	parameters.put("organizations", organizationManager.getByTypeIncludingDummy(type, getText("misc.all", locale)));
        } else {
        	parameters.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));
        }

        parameters.put("organizations2", organizationManager.getByTypeIncludingParentAndDummy(Organization.Type.AREA, Organization.Type.REGION, getText("misc.all", locale)));

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
            
            Object orgId = user.getOrganizationid();
	        if ((orgId != null) && StringUtils.isNumeric(orgId.toString())) {
	            location.setOrganizationid(new Long(orgId.toString()));
	        }

	        Object org2id = user.getOrganization2id();
	        if ((org2id != null) && StringUtils.isNumeric(org2id.toString())) {
	            location.setOrganization2id(new Long(org2id.toString()));
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

        // Are we to return to the list?
        if (request.getParameter("return") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'return' from jsp");
            }
            
        } // or to delete?
        else if (request.getParameter("delete") != null) {
            Course course = new Course();
            course.setStatus(null); // se etter alle statuser
            course.setLocationid(location.getId());
            List<Course> searchedCourses = courseManager.searchCourses(course, null, null, null);
            List searchedFollowups = followupManager.getFollowupsWhereLocation(location.getId());
            if ((searchedCourses == null || searchedCourses.size() == 0) && (searchedFollowups == null || searchedFollowups.size() == 0)) {
                locationManager.removeLocation(location.getId().toString());
//                locationManager.removePostalCodeLocationDistancesForLocation(location);
                saveMessage(request, getText("location.deleted", locale));
            } else {
                String text = getText("location.canNotDeleteDueToCourse", locale);
                saveMessage(request, text);
            }
            

        } else {
        	String mapURL = location.getmapURL();
        	if(StringUtils.isNotEmpty(mapURL) && !mapURL.startsWith("http://")){
        		location.setMapURL("http://" + mapURL);
        	}

        	String detailURL = location.getDetailURL();
        	if(StringUtils.isNotEmpty(detailURL) && !detailURL.startsWith("http://")){
        		location.setDetailURL("http://" + detailURL);
        	}
        	
        	Object[] args = null;
        	String postalcode = location.getPostalCode();
        	if(StringUtils.isNumeric(postalcode) && postalcode.length() >= 4){
        		if(!PostalCodesSuperduperLoader.isValidPostalCode(postalcode)){
        			args = new Object[]{postalcode};
        			errors.rejectValue("postalCode", "errors.postalCodeInvalid", args, "");
        		}
        	}
        	
			if (validateAnnotations(location, errors, null) > 0 || args != null) {
				locationManager.evict(location);
				return showForm(request, response, errors);
			}
        	
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

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 20. dec 2005
 */
package no.unified.soak.webapp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller class for Course. Creates a view with a list of all Courses.
 *
 * @author hrj
 */
public class CourseController extends BaseFormController {
    private CourseManager courseManager = null;
    private OrganizationManager organizationManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private RegistrationManager registrationManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setOrganizationManager(
        OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }
    
    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method...");
        }
        Locale locale = request.getLocale();
        Map model = new HashMap();
        
        Course course = new Course();

        // use course from session if set
        HttpSession session = request.getSession();
        Object comm = session.getAttribute("course");
        
        Map map = request.getParameterMap();
        
        // if params then this is navigation in table
        if (comm != null && map.size() > 1) {
            course = (Course) comm;
        }
        
        Boolean historic = new Boolean(false);
        Boolean past = new Boolean(false);
        
        Date starttime = new Date();
        Date stoptime = null;
        
        Boolean isAdmin = (Boolean)request.getAttribute("isAdmin");
        List<String> roles = (List)request.getAttribute(Constants.EZ_ROLES);
        if (roles == null) {
            roles = new ArrayList<String>();
            roles.add(Constants.EZROLE_ANONYMOUS); // Make sure not logged in users sees anonymous courses
        }

        // Don't modify organization if in postback
        String postback = request.getParameter("ispostbackcourselist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
        	// Promote inter organization cooperation by not setting organization here
	        // Object omid = request.getAttribute(Constants.EZ_ORGANIZATION);
	        // if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
	        //     course.setOrganizationid(new Long(omid.toString()));
	        // }

        	// Check if a specific organization has been requested
	        String mid = request.getParameter("mid");
	        if ((mid != null) && StringUtils.isNumeric(mid)) {
	            course.setOrganizationid(new Long(mid));
	        }
        }

        // Check whether we should display historic data as well
        String hist = request.getParameter("historic");
        if ((hist != null) && StringUtils.isNumeric(hist)) {
            if (hist.compareTo("0") != 0) {
                starttime = null;
                historic = new Boolean(true);
            }
        }

        // Check whether we should only display past data
        String pastreq = request.getParameter("past");
        if ((pastreq != null) && StringUtils.isNumeric(pastreq)) {
            if (pastreq.compareTo("0") != 0) {
            	starttime = null;
                stoptime = new Date();
                historic = new Boolean(true);
                past = new Boolean(true);
            }
        }
        
        // Set up parameters, and return them to the view
        model = addServiceAreas(model, locale);
        model = addOrganization(model, locale);
        model.put("historic", historic);
        model.put("past", past);
        
        // Add all courses to the list
        List courses = courseManager.searchCourses(course, starttime, stoptime);
        List<Course> filtered = filterByRole(isAdmin, roles, courses);

        if (courses != null) {
            model.put("courseList", filtered);
        }

        model.put("JSESSIONID", session.getId());

        return model;
    }

    private List<Course> filterByRole(Boolean admin, List<String> roles, List courses) {
        List<Course> filtered = new ArrayList<Course>();
        // Filter all courses not visible for the user.
        if (roles != null) {
            for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
                Course roleCourse = (Course) iterator.next();
                roleCourse.setAvailableAttendants(0);
                if (roles.contains(roleCourse.getRole()) || admin.booleanValue()) {
                    if(roleCourse.getStopTime().after(new Date())){
                        roleCourse.setAvailableAttendants(registrationManager.getAvailability(true, roleCourse));
                    }
                    filtered.add(roleCourse);
                }
            }
        } else {
            filtered = courses;
        }
        return filtered;
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

        Map model = new HashMap();
        HttpSession session = request.getSession();

        Locale locale = request.getLocale();

        Course course = (Course) command;

        // Set up parameters, and return them to the view
        model = addServiceAreas(model, locale);
        model = addOrganization(model, locale);
        model.put("course", course);
        session.setAttribute("course", course);

        Boolean historic = new Boolean(false);
        Boolean past = new Boolean(false);
        
        Date starttime = new Date();
        Date stoptime = null;
        
        Boolean isAdmin = (Boolean)request.getAttribute("isAdmin");
        List<String> roles = (List)request.getAttribute(Constants.EZ_ROLES);
        if (roles == null) {
            roles = new ArrayList<String>();
            roles.add(Constants.EZROLE_ANONYMOUS); // Make sure not logged in users sees anonymous courses
        }
        
        // Check whether we should display historic data as well
        String hist = request.getParameter("historic");
        if ((hist != null) && StringUtils.isNumeric(hist)) {
            if (hist.compareTo("0") != 0) {
               	starttime = null;
                historic = new Boolean(true);
            }
        }

        // Check whether we should only display past data
        String pastreq = request.getParameter("past");
        if ((pastreq != null) && StringUtils.isNumeric(pastreq)) {
            if (pastreq.compareTo("0") != 0) {
            	starttime = null;
                stoptime = new Date();
                past = new Boolean(true);
            }
        }
        
        Date startInterval = course.getStartTime();
        Date stopInterval = course.getStopTime();
        
        if (startInterval != null) {
        	starttime = startInterval;
        }
        if (stopInterval != null) {
        	stoptime = stopInterval;
        }

        // Add all courses to the list
        List courses = courseManager.searchCourses(course, starttime, stoptime);
        List<Course> filtered = filterByRole(isAdmin, roles, courses);
        
        model.put("courseList", filtered);
        model.put("historic", historic);
        model.put("past", past);

        return new ModelAndView(getSuccessView(), model);
    }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        // Get course from session
        HttpSession session = request.getSession();
        Object comm = session.getAttribute("course");
        
        Map map = request.getParameterMap();
        
        // if no params, use new course
        if (comm == null || map.size() < 2) {
            comm = new Course();
        }
        return comm;
    } 

    /**
     * Used to create a list of all service areas with an option 0 that says
     * "all service areas" and is therefore made with search forms in mind.
     *
     * @param model
     *            model to send to view
     * @param locale
     *            currently used locale
     * @return map with all service areas and one with id=0 that is "all service
     *         areas"
     */
    private Map addServiceAreas(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        // Get all organizations in the database
        List serviceAreasInDB = serviceAreaManager.getAll();
        List serviceAreas = new ArrayList<ServiceArea>();
        ServiceArea serviceAreaDummy = new ServiceArea();
        serviceAreaDummy.setId(new Long(0));
        serviceAreaDummy.setName(getText("misc.all", locale));
        serviceAreas.add(serviceAreaDummy);
        serviceAreas.addAll(serviceAreasInDB);

        if (serviceAreas != null) {
            model.put("serviceareas", serviceAreas);
        }

        return model;
    }

    /**
     * Used to create a list of all organizations with an option 0 that says
     * "all organizations" and is therefore made with search forms in mind.
     *
     * @param model
     *            model to send to view
     * @param locale
     *            currently used locale
     * @return map with all organizations and one with id=0 that is "all
     *         organizations"
     */
    private Map addOrganization(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));

        return model;
    }
}

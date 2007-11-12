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

import no.unified.soak.model.Course;
import no.unified.soak.model.Municipalities;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MunicipalitiesManager;
import no.unified.soak.service.ServiceAreaManager;

import org.apache.commons.lang.StringUtils;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Controller class for Course. Creates a view with a list of all Courses.
 *
 * @author hrj
 */
public class CourseController extends BaseFormController {
    private CourseManager courseManager = null;
    private MunicipalitiesManager municipalitiesManager = null;
    private ServiceAreaManager serviceAreaManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setMunicipalitiesManager(
        MunicipalitiesManager municipalitiesManager) {
        this.municipalitiesManager = municipalitiesManager;
    }

    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
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
        Object comm = session.getAttribute("course");

        Locale locale = request.getLocale();
        Map model = new HashMap();

        Course course = new Course();
        
        Boolean historic = new Boolean(false);
        Boolean past = new Boolean(false);
        
        Date starttime = new Date();
        Date stoptime = null;

        if (comm != null) {
            course = (Course) comm;
        }

        // Don't modify municipality if in postback
        String postback = request.getParameter("ispostbackcourselist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
        	// Promote inter municipality cooperation by not setting municipality here
	        // Object omid = request.getAttribute(Constants.EZ_MUNICIPALITY);
	        // if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
	        //     course.setMunicipalityid(new Long(omid.toString()));
	        // }

        	// Check if a specific municipality has been requested
	        String mid = request.getParameter("mid");
	        if ((mid != null) && StringUtils.isNumeric(mid)) {
	            course.setMunicipalityid(new Long(mid));
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
        model = addMunicipalities(model, locale);
        model.put("course", course);
        model.put("historic", historic);
        model.put("past", past);

        // Add all courses to the list
        List courses = courseManager.searchCourses(course, starttime, stoptime);

        if (courses != null) {
            model.put("courseList", courses);
        }

        model.put("JSESSIONID", session.getId());

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

        Map model = new HashMap();
        HttpSession session = request.getSession();

        Locale locale = request.getLocale();

        Course course = (Course) command;

        // Set up parameters, and return them to the view
        model = addServiceAreas(model, locale);
        model = addMunicipalities(model, locale);
        model.put("course", course);
        session.setAttribute("course", course);

        Boolean historic = new Boolean(false);
        Boolean past = new Boolean(false);
        
        Date starttime = new Date();
        Date stoptime = null;
        
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
        model.put("courseList", courses);
        model.put("historic", historic);
        model.put("past", past);

        return new ModelAndView(getSuccessView(), model);
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

        // Get all municipalities in the database
        List serviceAreasInDB = serviceAreaManager.getServiceAreas();
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
     * Used to create a list of all municipalities with an option 0 that says
     * "all municipalities" and is therefore made with search forms in mind.
     *
     * @param model
     *            model to send to view
     * @param locale
     *            currently used locale
     * @return map with all municipalities and one with id=0 that is "all
     *         municipalities"
     */
    private Map addMunicipalities(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        // Get all municipalities in the database
        List municipalitiesInDB = municipalitiesManager.getAll();
        List municipalities = new ArrayList();
        Municipalities municipalityDummy = new Municipalities();
        municipalityDummy.setId(new Long(0));
        municipalityDummy.setName(getText("misc.all", locale));
        municipalities.add(municipalityDummy);
        municipalities.addAll(municipalitiesInDB);

        if (municipalities != null) {
            model.put("municipalities", municipalities);
        }

        return model;
    }
}

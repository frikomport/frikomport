/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 21. dec 2005
 */
package no.unified.soak.webapp.action;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Municipalities;
import no.unified.soak.model.Registration;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MunicipalitiesManager;
import no.unified.soak.service.RegistrationManager;
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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Controller class for Registration. Creates a view with a list of all
 * Registration.
 *
 * @author hrj
 */
public class RegistrationController extends BaseFormController {
    private CourseManager courseManager = null;
    private MunicipalitiesManager municipalitiesManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private RegistrationManager registrationManager = null;

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

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
        Object comm = session.getAttribute("registration");

        Boolean reserved = null;
        Boolean invoiced = null;
        Locale locale = request.getLocale();

        Registration registration = new Registration();

        Boolean historic = new Boolean(false);

        if (comm != null) {
            registration = (Registration) comm;
        }

        // Don't modify municipality if in postback
        String postback = request.getParameter("ispostbackregistrationlist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
	        // Check if a default municipality should be applied
	        Object omid = request.getAttribute(Constants.EZ_MUNICIPALITY);
	        if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
	            registration.setMunicipalityid(new Long(omid.toString()));
	        }
	
	        // Check if a specific municipality has been requested
	        String mid = request.getParameter("mid");
	        if ((mid != null) && StringUtils.isNumeric(mid)) {
	            registration.setMunicipalityid(new Long(mid));
	        }
        }

        String courseid = request.getParameter("courseid");
        if ((courseid != null) && StringUtils.isNumeric(courseid)) {
            registration.setCourseid(new Long(courseid));
        }

        String serviceareaid = request.getParameter("sid");
        if ((serviceareaid != null) && StringUtils.isNumeric(serviceareaid)) {
            registration.setServiceareaid(new Long(serviceareaid));
        }

        // String reserved = request.getParameter("reserved");
        // if (reserved != null)

        // Check whether we should display historic data as well
        String hist = request.getParameter("historic");

        if ((hist != null) && StringUtils.isNumeric(hist)) {
            if (hist.compareTo("0") != 0) {
                historic = new Boolean(true);
            }
        }

        // Set up parameters, and return them to the view
        Map model = new HashMap();
        model = addServiceAreas(model, locale);
        model = addMunicipalities(model, locale);
        model = addReserved(model, locale);
        model = addInvoiced(model, locale);
        
        List courses = getCourses(locale, historic);
        List courseIds = null;
        if (courses != null) {
        	model.put("courses", courses);
        	if (historic.booleanValue() == false) {
        		courseIds = getCourseIdList(courses);
        	}
        }

        // Set the historic-flag
        model.put("historic", historic);
        model.put("reservedValue", new Integer(2));
        model.put("invoicedValue", new Integer(2));

        // These are set to false to trick the jsp
        registration.setInvoiced(new Boolean(false));
        registration.setReserved(new Boolean(false));

        // Fetch all registrations that match the parameters
        List registrations = registrationManager.getSpecificRegistrations(registration.getCourseid(),
                registration.getMunicipalityid(),
                registration.getServiceareaid(), reserved, invoiced, courseIds);

        if (registrations != null) {
            model.put("registrationList", registrations);
        }

        model.put("registration", registration);

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

        // Setup some default parameters
        Locale locale = request.getLocale();
        Integer reservedParameter = 0;
        Integer invoicedParameter = 0;
        Boolean historic = new Boolean(false);

        // Fetch the form's command object
        Registration registration = (Registration) command;

        // Check whether we should display historic data as well
        String hist = request.getParameter("historic");
        if ((hist != null) && StringUtils.isNumeric(hist)) {
            if (hist.compareTo("0") != 0) {
                historic = new Boolean(true);
            }
        }

        String reservedRequest = request.getParameter("reservedField");
        String invoicedRequest = request.getParameter("invoicedField");

        // Check the "boolean" values for invoiced and reserved
        if ((reservedRequest != null) && (reservedRequest.compareTo("0") == 0)) {
            registration.setReserved(new Boolean(false));
        } else if ((reservedRequest != null) &&
                (reservedRequest.compareTo("1") == 0)) {
            registration.setReserved(new Boolean(true));
        } else if ((reservedRequest != null) &&
                (reservedRequest.compareTo("2") == 0)) {
            registration.setReserved(null);
        }

        if ((invoicedRequest != null) && (invoicedRequest.compareTo("0") == 0)) {
            registration.setInvoiced(new Boolean(false));
        } else if ((invoicedRequest != null) &&
                (invoicedRequest.compareTo("1") == 0)) {
            registration.setInvoiced(new Boolean(true));
        } else if ((invoicedRequest != null) &&
                (invoicedRequest.compareTo("2") == 0)) {
            registration.setInvoiced(null);
        }

        if ((invoicedRequest != null) &&
                StringUtils.isNumeric(invoicedRequest)) {
            invoicedParameter = new Integer(invoicedRequest);
        }

        if ((reservedRequest != null) &&
                StringUtils.isNumeric(reservedRequest)) {
            reservedParameter = new Integer(reservedRequest);
        }

        // Set up parameters, and return them to the view
        Map model = new HashMap();
        model = addServiceAreas(model, locale);
        model = addMunicipalities(model, locale);
        model = addReserved(model, locale);
        model = addInvoiced(model, locale);
        model.put("registration", registration);
        model.put("historic", historic);
        model.put("reservedValue", reservedParameter);
        model.put("invoicedValue", invoicedParameter);
        
        session.setAttribute("registration", registration);

        // Add all courses to the list
        List courses = getCourses(locale, historic);
        List courseIds = null;
        if (courses != null) {
        	model.put("courses", courses);
        	if (historic.booleanValue() == false) {
        		courseIds = getCourseIdList(courses);
        	}
        }

        // Find the courses that match the parameters
        model.put("registrationList",
            registrationManager.getSpecificRegistrations(
                registration.getCourseid(), registration.getMunicipalityid(),
                registration.getServiceareaid(), registration.getReserved(),
                registration.getInvoiced(), courseIds));

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
        List serviceAreas = new ArrayList();
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
     *            model to send to the view
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

    private List getCourses(Locale locale, Boolean historic) {
        // Determine correct startdate if historic
        Date startDate = null;
        if(historic.booleanValue() == false) {
        	startDate = new Date();
        }

        // Add all courses to the list
        List courses = courseManager.searchCourses(null, startDate, null);

        if (courses != null) {
            List courseList = new ArrayList();
            Course courseDummy = new Course();
            courseDummy.setId(new Long("0"));
            courseDummy.setName(getText("misc.all", locale));
            courseList.add(courseDummy);
            courseList.addAll(courses);

            return courseList;
        } else {
            // TODO: Handle this (if this in fact is anything to handle)
        }

        return null;
    }

    private List getCourseIdList(List courses) {
        if (courses != null) {
            List<Long> courseIds = new Vector<Long>();
            
            for (Course course : (List<Course>)courses) {
            	courseIds.add(course.getId());
            }

            return courseIds;
        }

        return null;
    }

    /**
     * Creates a list of choices for "reserved" to be used in drop-down box
     *
     * @param model
     *            model to send to the view
     * @param locale
     *            currently used locale
     * @return map with all/reserved/on waitinglist
     */
    private Map addReserved(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        HashMap reserver = new HashMap();
        reserver.put("null", getText("misc.all", locale));
        reserver.put("true", getText("registrationlist.reserved", locale));
        reserver.put("false", getText("registrationlist.notReserved", locale));

        model.put("reserved", reserver);

        return model;
    }

    /**
     * Creates a list of choices for "invoiced" to be used in drop-down box
     *
     * @param model
     *            model to send to the view
     * @param locale
     *            currently used locale
     * @return map with all/invoiced/not invoiced
     */
    private Map addInvoiced(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        HashMap invoiced = new HashMap();
        invoiced.put("null", getText("misc.all", locale));
        invoiced.put("true", getText("registrationList.invoiced", locale));
        invoiced.put("false", getText("registrationList.notInvoiced", locale));

        if (invoiced != null) {
            model.put("invoiced", invoiced);
        }

        return model;
    }
}

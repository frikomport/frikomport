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

import no.unified.soak.Constants;
import no.unified.soak.dao.hibernate.RegistrationStatusCriteria;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller class for Registration. Creates a view with a list of all
 * Registration.
 *
 * @author hrj
 */
public class RegistrationController extends BaseFormController {
    private CourseManager courseManager = null;
    private OrganizationManager organizationManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private RegistrationManager registrationManager = null;

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

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

        Boolean invoiced = null;
        Boolean attended = null;
        Locale locale = request.getLocale();

        Registration registration = new Registration();

        Boolean historic = new Boolean(false);

        if (comm != null) {
            registration = (Registration) comm;
        }

        // Don't modify organization if in postback
        String postback = request.getParameter("ispostbackregistrationlist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
            // Check if a default organization should be applied
            User user = (User) session.getAttribute(Constants.USER_KEY);
            if (user != null && user.getOrganizationid() != null) {
                registration.setOrganizationid(user.getOrganizationid());
            }

            // Check if a specific organization has been requested
            String mid = request.getParameter("mid");
            if ((mid != null) && StringUtils.isNumeric(mid)) {
                registration.setOrganizationid(new Long(mid));
            }
        }

        String courseid = request.getParameter("courseId");
        if ((courseid != null) && StringUtils.isNumeric(courseid)) {
            registration.setCourseid(new Long(courseid));
        }

        String serviceareaid = request.getParameter("sid");
        if ((serviceareaid != null) && StringUtils.isNumeric(serviceareaid)) {
            registration.setServiceAreaid(new Long(serviceareaid));
        }

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
        model = addOrganizations(model, locale);
        model = addReserved(model, locale);
        model = addInvoiced(model, locale);
        model = addAttended(model, locale);

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
        model.put("attendedValue", new Integer(2));

        // These are set to false to trick the jsp
        registration.setInvoiced(new Boolean(false));
        registration.setAttended(new Boolean(false));

        RegistrationStatusCriteria statusCriteria;
        // Get user from acegi
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (user.getRoleNameList().contains("admin") 
                || user.getRoleNameList().contains("editor") 
                || user.getRoleNameList().contains("instructor")) {
            statusCriteria = null;
        } else {
            statusCriteria = RegistrationStatusCriteria.getNotCanceledCriteria();
        }

        // Fetch all registrations that match the parameters
        List registrations = registrationManager.getSpecificRegistrations(registration.getCourseid(), registration
                .getOrganizationid(), registration.getServiceAreaid(), statusCriteria, invoiced, attended, courseIds, null);

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
        Integer attendedParameter = 0;
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
        String attendedRequest = request.getParameter("attendedField");

        // Check the "boolean" values for invoiced, reserved and attended
        RegistrationStatusCriteria statusCriteria = null;
        if ((reservedRequest != null) && (reservedRequest.compareTo("0") == 0)) {
            registration.setStatus(Registration.Status.WAITING);
            statusCriteria = new RegistrationStatusCriteria(Registration.Status.WAITING);
        } else if ((reservedRequest != null) &&
                (reservedRequest.compareTo("1") == 0)) {
            registration.setStatus(Registration.Status.RESERVED);
            statusCriteria = new RegistrationStatusCriteria(Registration.Status.RESERVED);
        } else if ((reservedRequest != null) &&
                (reservedRequest.compareTo("2") == 0)) {
            
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (user.getRoleNameList().contains("admin") 
                    || user.getRoleNameList().contains("editor") 
                    || user.getRoleNameList().contains("instructor")) {
                statusCriteria = RegistrationStatusCriteria.getNotCanceledCriteria();
            }
            registration.setStatus((Registration.Status) null);
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
        
        if ((attendedRequest != null) && (attendedRequest.compareTo("0") == 0)) {
            registration.setAttended(new Boolean(false));
        } else if ((attendedRequest != null) &&
                (attendedRequest.compareTo("1") == 0)) {
            registration.setAttended(new Boolean(true));
        } else if ((attendedRequest != null) &&
                (attendedRequest.compareTo("2") == 0)) {
            registration.setAttended(null);
        }

        if ((invoicedRequest != null) &&
                StringUtils.isNumeric(invoicedRequest)) {
            invoicedParameter = new Integer(invoicedRequest);
        }

        if ((reservedRequest != null) &&
                StringUtils.isNumeric(reservedRequest)) {
            reservedParameter = new Integer(reservedRequest);
        }
        
        if ((attendedRequest != null) &&
                StringUtils.isNumeric(attendedRequest)) {
            attendedParameter = new Integer(attendedRequest);
        }

        // Set up parameters, and return them to the view
        Map model = new HashMap();
        model = addServiceAreas(model, locale);
        model = addOrganizations(model, locale);
        model = addReserved(model, locale);
        model = addInvoiced(model, locale);
        model = addAttended(model, locale);
        model.put("registration", registration);
        model.put("historic", historic);
        model.put("reservedValue", reservedParameter);
        model.put("invoicedValue", invoicedParameter);
        model.put("attendedValue", attendedParameter);
        
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
                registration.getCourseid(), registration.getOrganizationid(),
                registration.getServiceAreaid(), statusCriteria,
                registration.getInvoiced(), registration.getAttended(), courseIds, null));

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
        
        model.put("organizations", serviceAreaManager.getAllIncludingDummy(getText("misc.all", locale)));model.put("serviceareas", serviceAreaManager.getAllIncludingDummy(getText("misc.all", locale)));

        return model;
    }

    /**
     * Used to create a list of all organizations with an option 0 that says
     * "all organizations" and is therefore made with search forms in mind.
     *
     * @param model
     *            model to send to the view
     * @param locale
     *            currently used locale
     * @return map with all organizations and one with id=0 that is "all
     *         organizations"
     */
    private Map addOrganizations(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));

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
        reserver.put("true", getText("registrationList.reserved", locale));
        reserver.put("false", getText("registrationList.notReserved", locale));

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
    
    /**
     * Creates a list of choices for "attended" to be used in drop-down box
     *
     * @param model
     *            model to send to the view
     * @param locale
     *            currently used locale
     * @return map with all/attended/not attended
     */
    private Map addAttended(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        HashMap attended = new HashMap();
        attended.put("null", getText("misc.all", locale));
        attended.put("true", getText("registrationList.attended", locale));
        attended.put("false", getText("registrationList.notAttended", locale));

        if (attended != null) {
            model.put("attended", attended);
        }

        return model;
    }
}

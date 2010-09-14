/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 * Created 20. dec 2005
 */
package no.unified.soak.webapp.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import no.unified.soak.model.Organization;
import no.unified.soak.model.User;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.impl.CategoryManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
    private CategoryManager categoryManager = null;
    private LocationManager locationManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Locale locale = request.getLocale();
        Map model = new HashMap();

        Course course = (Course) command;
        Course unpublished = new Course();
        
        // use course from session if set
        HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute(Constants.USER_KEY);
        String queryString = request.getQueryString();
        
		/**
		 * queryString er forskjellig fra null dersom referenceData er kalt fra
		 * displayTag-rammeverket, geografidata skal da IKKE overskrives
		 */
        if(ApplicationResourcesUtil.isSVV() && user != null && queryString == null){
	        // default visning knyttet til org2 for SVV
	        course.setOrganization2(user.getOrganization2());
	        course.setOrganization2id(user.getOrganization2id());
        }
        
        String alreadyRegistered = request.getParameter("alreadyRegistered");
        if (alreadyRegistered != null && alreadyRegistered.equals("true")) {
            model.put("alreadyRegistered", true);
        }

        // default published courses
        course.setStatus(CourseStatus.COURSE_PUBLISHED);

        Boolean historic = new Boolean(false);
        Boolean past = new Boolean(false);

        Date starttime = new Date();
        Date stoptime = null;

        Boolean isAdmin = false;
        List<String> roles = null;
        if (user != null) {
            roles = user.getRoleNameList();
            isAdmin = (Boolean) roles.contains(Constants.ADMIN_ROLE);
        }
        if (roles == null) {
            roles = new ArrayList<String>();
            roles.add(Constants.ANONYMOUS_ROLE);
        }

        // Don't modify organization if in postback
        String postback = request.getParameter("ispostbackcourselist");
        if ((postback == null) || (postback.compareTo("1") != 0)) {
            // Promote inter organization cooperation by not setting organization here
            // Object omid = request.getAttribute(Constants.EZ_ORGANIZATION);
            // if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
            // course.setOrganizationid(new Long(omid.toString()));
            // }

            // Check if a specific organization has been requested
            String mid = request.getParameter("mid");
            if ((mid != null) && StringUtils.isNumeric(mid)) {
                course.setOrganizationid(new Long(mid));
                unpublished.setOrganizationid(new Long(mid));
            }
        }

        // Check whether we should display historic data as well
        String hist = request.getParameter("historic");
        if ((hist != null) && StringUtils.isNumeric(hist)) {
            if (hist.compareTo("0") != 0) {
                starttime = null;
                historic = new Boolean(true);
                // also finished courses
                course.setStatus(CourseStatus.COURSE_FINISHED);
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
                // also finished courses
                course.setStatus(CourseStatus.COURSE_FINISHED);
            }
        }

        // Check whether a specific search is requested
        String name = request.getParameter("name");
        if (name != null) {
            course.setName(name);
        }
        
        // Set up parameters, and return them to the view
        model = addServiceAreas(model, locale);
        model = addOrganization(model, locale);
        model = addOrganization2(model, locale);
        model = addCategories(model, locale);
        model = addLocations(model, locale);
        model.put("historic", historic);
        model.put("past", past);

        List<Course> courseList = new ArrayList<Course>();

        boolean avoidSearch = false;
        String startTimeString = request.getParameter("startTime");
		if (!StringUtils.isBlank(startTimeString)) {
			try {
				starttime = DateUtil.convertStringToDate(startTimeString);
				model.put("startTime", starttime);
			} catch (ParseException e) {
				String[] msgArgs = new String[] { startTimeString,
						getText("date.format", ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(null)), "" };
				saveMessage(request, getText("errors.dateformat", msgArgs, ApplicationResourcesUtil
						.getNewLocaleWithDefaultCountryAndVariant(null)));
				avoidSearch = true;
			}
		}
		
		String stopTimeString = request.getParameter("stopTime");
		if (!StringUtils.isBlank(stopTimeString)) {
			try {
				stoptime = DateUtil.convertStringToDate(stopTimeString);
				model.put("stopTime", stoptime);
			} catch (ParseException e) {
				String[] msgArgs = new String[] { stopTimeString,
						getText("date.format", ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(null)), "" };
				saveMessage(request, getText("errors.dateformat", msgArgs, ApplicationResourcesUtil
						.getNewLocaleWithDefaultCountryAndVariant(null)));
				avoidSearch = true;
			}
		}

		model.put("JSESSIONID", session.getId());

		if (avoidSearch) {
			return model;
		}
		
        if(ApplicationResourcesUtil.isSVV()){ // alle LDAP-brukere ser alle møter (kurs), men endringstilgang styres av JSP.
        	Integer[] status = null; 
        		
        	if(roles.contains(Constants.ANONYMOUS_ROLE) && roles.size() == 1){ 
        		// publikumsbruker
        		status = new Integer[]{ CourseStatus.COURSE_PUBLISHED };
        	}
        	else{ 
        		// isReader / isEventResponsible / isEducationResponsible / isAdministrator
        		status = new Integer[]{ CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED, CourseStatus.COURSE_CANCELLED };
        	}
        	courseList = courseManager.searchCourses(course, starttime, stoptime, status);
        	courseList = updateAvailableAttendants(courseList, request);
        }
        else {
	        List courses = courseManager.searchCourses(course, starttime, stoptime, null);
	        courseList = filterByRole(isAdmin, roles, courses);
	        User responsible = null;
	        if (user != null && roles.contains(Constants.EVENTRESPONSIBLE_ROLE)) {
	            responsible = user;
	            unpublished.setResponsible(responsible);
	        }
	        List unpubCourses = courseManager.getUnpublished(unpublished);// Søke på samme måte som på kurs.
	        if (!past && unpubCourses != null && !unpubCourses.isEmpty() && isAdmin(roles)) {
	        	courseList.addAll(0, unpubCourses);
	        }
        }

        model.put("courseList", courseList);

        return model;
    }

	private boolean isAdmin(List<String> roles) {
        if (roles.contains(Constants.EVENTRESPONSIBLE_ROLE) || roles.contains(Constants.EDITOR_ROLE)
                || roles.contains(Constants.ADMIN_ROLE)) {
            return true;
        }
        return false;
    }
 
    private List<Course> filterByRole(Boolean admin, List<String> roles, List courses) {
        List<Course> filtered = new ArrayList<Course>();
        // Filter all courses not visible for the user.
        if (roles != null) {
            for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
                Course roleCourse = (Course) iterator.next();
                roleCourse.setAvailableAttendants(0);
                if (roles.contains(roleCourse.getRole()) || admin.booleanValue()) {
                    if (roleCourse.getStopTime().after(new Date())) {
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

	private List<Course> updateAvailableAttendants(List courses, HttpServletRequest request) {
		List<Course> updated = new ArrayList<Course>();
		Integer availableSum = NumberUtils.INTEGER_ZERO;
		for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
			Course course = (Course) iterator.next();
			course.setAvailableAttendants(NumberUtils.INTEGER_ZERO);
//			if (course.getStopTime().after(new Date())) {
				Integer available = registrationManager.getAvailability(true, course);
				course.setAvailableAttendants(available);
				availableSum += available;
//			}
			updated.add(course);
		}

		request.setAttribute("sumTotal", availableSum);
		return updated;
	}
    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Map model = new HashMap();
		HttpSession session = request.getSession();
		Locale locale = request.getLocale();

		// validering av postnummer fra welcome.html
		String postalcode = request.getParameter("postalcode");
		if(!StringUtils.isBlank(postalcode)){
			if(postalcode.length() != 4 || !StringUtils.isNumeric(postalcode)){
				saveErrorMessage(request, getText("welcome.postalcode.error", new String[] { postalcode }, locale));
				return new ModelAndView("redirect:welcome.html");
			}
		}

		boolean avoidSearch = false;

		Course course = (Course) command;
		Course unpublished = new Course();

		course.setStatus(CourseStatus.COURSE_PUBLISHED);

		// Set up parameters, and return them to the view
		model = addServiceAreas(model, locale);
		model = addOrganization(model, locale);
		model = addOrganization2(model, locale);
		model = addLocations(model, locale);
		model = addCategories(model, locale);
		model.put("course", course);

		Boolean historic = new Boolean(false);
		Boolean past = new Boolean(false);

		Date starttime = new Date();
		Date stoptime = null;

		User user = (User) session.getAttribute(Constants.USER_KEY);
		Boolean isAdmin = false;
		List<String> roles = null;
		if (user != null) {
			roles = user.getRoleNameList();
			isAdmin = (Boolean) roles.contains(Constants.ADMIN_ROLE);
		}
		if (roles == null) {
			roles = new ArrayList<String>();
			// Make sure not logged in users sees anonymous courses
			roles.add(Constants.ANONYMOUS_ROLE); 
		}

		// Check whether we should display historic data as well
		String hist = request.getParameter("historic");
		if ((hist != null) && StringUtils.isNumeric(hist)) {
			if (hist.compareTo("0") != 0) {
				starttime = null;
				historic = new Boolean(true);
				// also finished courses
				course.setStatus(CourseStatus.COURSE_FINISHED);
			}
		}

		// Check whether we should only display past data
		String pastreq = request.getParameter("past");
		if ((pastreq != null) && StringUtils.isNumeric(pastreq)) {
			if (pastreq.compareTo("0") != 0) {
				starttime = null;
				stoptime = new Date();
				past = new Boolean(true);
				// also finished courses
				course.setStatus(CourseStatus.COURSE_FINISHED);
			}
		}

		// Check whether a specific search is requested
		String name = request.getParameter("name");
		if (name != null) {
			course.setName(name);
		}

		Date startInterval = course.getStartTime();
		Date stopInterval = course.getStopTime();

		if (startInterval != null) {
			if (stopInterval != null && startInterval.after(stopInterval)) {
				saveMessage(request, getText("errors.FromDateMustBeLessThanToDate", new String[] {
						DateUtil.convertDateToString(startInterval), DateUtil.convertDateToString(stopInterval) }, locale));
				avoidSearch = true;
			}

			// if startInterval is in the past, the search will include historic
			// data
			if (startInterval.before(new Date())) {
				historic = new Boolean(true);
				course.setStatus(CourseStatus.COURSE_FINISHED);
			}
			starttime = startInterval;
			model.put("startTime", startInterval);
		}

		if (stopInterval != null) {
			// legger på 24timer for å sikre til-og-med sluttdato
			Calendar stop = new GregorianCalendar();
			stop.setTime(stopInterval);
			stop.add(Calendar.HOUR, 24);
			stopInterval = stop.getTime();
			stoptime = stopInterval;
			model.put("stopTime", course.getStopTime());
		}

		List<Course> courseList = new ArrayList<Course>();
		model.put("historic", historic);
		model.put("past", past);

		if (avoidSearch) {
			return new ModelAndView(getSuccessView(), model);
		}

		if (ApplicationResourcesUtil.isSVV()) { // alle LDAP-brukere ser
			// alle møter (kurs), men
			// endringstilgang styres av
			// JSP.
			Integer[] status = null;

			if (roles.contains(Constants.ANONYMOUS_ROLE) && roles.size() == 1) {
				// publikumsbruker
				status = new Integer[] { CourseStatus.COURSE_PUBLISHED };
			} else {
				// isReader / isEventResponsible / isEducationResponsible /
				// isAdministrator
				status = new Integer[] { CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED,
						CourseStatus.COURSE_CANCELLED };
			}

			if (StringUtils.isBlank(postalcode)) {
				courseList = courseManager.searchCourses(course, starttime, stoptime, status);
				courseList = updateAvailableAttendants(courseList, request);
	        } else {
				List<Long> locationIds = locationManager.getLocationIds(postalcode);
	        	int numberOfHits = 15; // default
	        	String numberOfHitsStr = getText("courseList.numberOfHits", locale);
	        	if(StringUtils.isNumeric(numberOfHitsStr)) numberOfHits = Integer.parseInt(numberOfHitsStr);
				courseList = courseManager.findByLocationIds(locationIds, numberOfHits);
				courseList = updateAvailableAttendants(courseList, request);

				// melding til bruker som forklarer postnr-søk
				saveMessage(request, getText("courseList.postalCodeInfo", new String[] { postalcode }, locale));
				
	        }
		} else {
			List courses = courseManager.searchCourses(course, starttime, stoptime, null);
			courseList = filterByRole(isAdmin, roles, courses);

			if (course.getOrganizationid() != null) {
				unpublished.setOrganizationid(course.getOrganizationid());
			}

			if (course.getOrganization2id() != null) {
				unpublished.setOrganization2id(course.getOrganization2id());
			}

			if (course.getLocationid() != null) {
				unpublished.setLocationid(course.getLocationid());
			}

			User responsible = null;
			if (user != null && roles.contains(Constants.EVENTRESPONSIBLE_ROLE)) {
				responsible = user;
				unpublished.setResponsible(responsible);
			}
			List unpubCourses = courseManager.getUnpublished(unpublished);
			// Søke på samme måte som på kurs.
			if (!past && unpubCourses != null && !unpubCourses.isEmpty() && isAdmin(roles)) {
				courseList.addAll(0, unpubCourses);
			}
		}

		model.put("courseList", courseList);

		return new ModelAndView(getSuccessView(), model);
	}

    /**
     * Bygger opp bakgrunnsobjekt
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Course course = new Course();

        String mid = request.getParameter("mid");
        if ((mid != null) && StringUtils.isNumeric(mid)) {
            course.setOrganizationid(new Long(mid));
        }
        String orgid = request.getParameter("organizationid");
        if (isNumber(orgid)) {
            course.setOrganizationid(new Long(orgid));
        }
        String org2id = request.getParameter("organization2id");
        if (isNumber(org2id)) {
            course.setOrganization2id(new Long(org2id));
        }
        String locationid = request.getParameter("locationid");
        if (isNumber(locationid)) {
            course.setLocationid(new Long(locationid));
        }
        String areaid = request.getParameter("serviceAreaid");
        if (isNumber(areaid)) {
            course.setServiceAreaid(new Long(areaid));
        }
        String catid = request.getParameter("categoryid");
        if (isNumber(catid)) {
            course.setCategoryid(new Long(catid));
        }
        String name = request.getParameter("name");
        if (name != null) {
            course.setName(name);
        }


        return course;
    }

    private boolean isNumber(String orgid) {
        return (orgid != null) && (!orgid.equals("")) && StringUtils.isNumeric(orgid);
    }

    /**
     * Used to create a list of all service areas with an option 0 that says "all service areas" and is therefore made
     * with search forms in mind.
     * 
     * @param model
     *            model to send to view
     * @param locale
     *            currently used locale
     * @return map with all service areas and one with id=0 that is "all service areas"
     */
    private Map addServiceAreas(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        // Get all organizations in the database
        List serviceAreas = serviceAreaManager.getAllIncludingDummy(getText("misc.all.serviceareas", locale));

        if (serviceAreas != null) {
            model.put("serviceareas", serviceAreas);
        }

        return model;
    }

    /**
     * Used to create a list of all organizations with an option 0 that says "all organizations" and is therefore made
     * with search forms in mind.
     * 
     * @param model
     *            model to send to view
     * @param locale
     *            currently used locale
     * @return map with all organizations and one with id=0 that is "all organizations"
     */
    private Map addOrganization(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        String typeDBvalue = ApplicationResourcesUtil.getText("show.organization.pulldown.typeDBvalue");
        if (StringUtils.isNotBlank(typeDBvalue)) {
        	Integer value = Integer.valueOf(typeDBvalue);
        	Type type = Organization.Type.getTypeFromDBValue(value);
            model.put("organizations", organizationManager.getByTypeIncludingDummy(type, getText("misc.all.organizations", locale)));
        } else {
            model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));
        }
        return model;
    }

    
    private Map addOrganization2(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }
        
        model.put("organizations2", organizationManager.getByTypeIncludingParentAndDummy(Organization.Type.AREA, Organization.Type.REGION, getText("misc.all.organization2s", locale)));
        return model;
    }

    private Map addLocations(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }
        
        model.put("locations", locationManager.getAllIncludingDummy(null, false, getText("misc.all.locations", locale)));
        return model;
    }
    

    private Map addCategories(Map model, Locale locale) {
        if (model == null) {
            model = new HashMap();
        }

        model.put("categories", categoryManager.getAllIncludingDummy(getText("misc.all", locale)));

        return model;
    }
}

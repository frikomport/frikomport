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
import no.unified.soak.util.PostalCodesSuperduperLoader;

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

        model.put("enableExport", new Boolean(true));
        
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
				
				// legger på 24timer for å sikre til-og-med sluttdato
				Calendar stop = new GregorianCalendar();
				stop.setTime(stoptime);
				stop.add(Calendar.HOUR, 24);
				stoptime = stop.getTime();
				// --
				
				model.put("stopTime", DateUtil.convertStringToDate(stopTimeString));
			} catch (ParseException e) {
				String[] msgArgs = new String[] { stopTimeString,
						getText("date.format", ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(null)), "" };
				saveMessage(request, getText("errors.dateformat", msgArgs, ApplicationResourcesUtil
						.getNewLocaleWithDefaultCountryAndVariant(null)));
				avoidSearch = true;
			}
		}

		if (avoidSearch) {
			return model;
		}
		
        if(ApplicationResourcesUtil.isSVV()){ // alle LDAP-brukere ser alle møter (kurs), men endringstilgang styres av JSP.
        	Integer[] status = null; 
        		
        	if(roles.contains(Constants.ANONYMOUS_ROLE) && roles.size() == 1){ 
        		// publikumsbruker
        		status = new Integer[]{ CourseStatus.COURSE_PUBLISHED };
                model.put("enableExport", new Boolean(false));
        	}
        	else{ 
        		// isReader / isEventResponsible / isEducationResponsible / isAdministrator
        			status = new Integer[]{ CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED };
        	}
        	
    		String postalcode = request.getParameter("postalcode");
			if (StringUtils.isBlank(postalcode)) {
				courseList = courseManager.searchCourses(course, starttime, stoptime, status);
				courseList = enrichCoursesWithExternalValues(courseList, request);
				configureColumnView(status, courseList, model);
	        } else {
	        	List<Long> locationIds = locationManager.getLocationIds(postalcode);
	        	int numberOfHits = 15; // default
	        	String numberOfHitsStr = getText("courseList.numberOfHits", locale);
	        	if(StringUtils.isNumeric(numberOfHitsStr)) numberOfHits = Integer.parseInt(numberOfHitsStr);
				courseList = courseManager.findByLocationIds(locationIds, numberOfHits);
				courseList = enrichCoursesWithExternalValues(courseList, request);

				model.put("containsUnfinished", new Boolean(true));
				model.put("containsFinished", new Boolean(false));
	        }
        }
        else {
        	Integer[] status = null; 
        	if(roles.contains(Constants.ANONYMOUS_ROLE) && roles.size() == 1){ 
        		// publikumsbruker
        		status = new Integer[]{ CourseStatus.COURSE_PUBLISHED };
                model.put("enableExport", new Boolean(false));
        	}
        	else if(roles.contains(Constants.ANONYMOUS_ROLE) && roles.contains(Constants.EMPLOYEE_ROLE) && roles.size() == 2){ 
        		// publikumsbruker
        		status = new Integer[]{ CourseStatus.COURSE_PUBLISHED };
                model.put("enableExport", new Boolean(false));
        	}
        	else{ 
        		// isReader / isEventResponsible / isEducationResponsible / isAdministrator
        			status = new Integer[]{ CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED, CourseStatus.COURSE_CANCELLED };
        	}
	        
			courseList = courseManager.searchCourses(course, starttime, stoptime, status);
			courseList = enrichCoursesWithExternalValues(courseList, request);
			configureColumnView(status, courseList, model);
        }

        model.put("courseList", courseList);

        return model;
    }

    /**
     * Metode for å sette parametre i modell som benyttes av jsp for å vise/skjule kolonner
     * @param status
     * @param courseList
     * @param model
     */
	private void configureColumnView(Integer[] status, List<Course> courseList, Map model) {
		if(status.length == 1){
			// publikum
			model.put("containsUnfinished", new Boolean(true));
			model.put("containsFinished", new Boolean(false));
		}
		else{
			// default
			model.put("containsUnfinished", new Boolean(false));
			model.put("containsFinished", new Boolean(false));
			
			boolean after = false;
			boolean before = false;
			
			if(!courseList.isEmpty()){
				Iterator<Course> iterator = courseList.iterator(); 
				while(iterator.hasNext()){
					Course c = iterator.next();
					if(c.getStatus().equals(CourseStatus.COURSE_FINISHED)){
						model.put("containsFinished", new Boolean(true));
						before = true;
					}
					if(c.getStatus().equals(CourseStatus.COURSE_PUBLISHED) || c.getStatus().equals(CourseStatus.COURSE_CREATED)){
						model.put("containsUnfinished", new Boolean(true));
						after = true;
					}
					if(before && after) break; // trenger ikke sjekke flere 
				}
			}
		}
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

	private List<Course> enrichCoursesWithExternalValues(List courses, HttpServletRequest request) {
		List<Course> updated = new ArrayList<Course>();
		Integer availableSum = NumberUtils.INTEGER_ZERO;
		Integer registrationsSum = NumberUtils.INTEGER_ZERO;
		Integer participantsSum = NumberUtils.INTEGER_ZERO;
		Integer attendedSum = NumberUtils.INTEGER_ZERO;

		for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
			Course course = (Course) iterator.next();
			
			Integer available = registrationManager.getAvailability(true, course);
			course.setAvailableAttendants(available);
			
			Integer registrations = registrationManager.getNumberOfRegistrations(course.getId());
			course.setNumberOfRegistrations(registrations);
			
			Integer participants = registrationManager.getNumberOfOccupiedSeats(course, false);
			course.setNumberOfParticipants(participants);

			availableSum += available;
			registrationsSum += registrations;
			participantsSum += participants;
			attendedSum += course.getAttendants() != null ? course.getAttendants() : 0;
			
			updated.add(course);
		}

		request.setAttribute("availableSum", availableSum);
		request.setAttribute("registrationsSum", registrationsSum);
		request.setAttribute("participantsSum", participantsSum);
		request.setAttribute("attendedSum", attendedSum);

		return updated;
	}
    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Map model = new HashMap();
		
        model.put("enableExport", new Boolean(true));

		HttpSession session = request.getSession();
		Locale locale = request.getLocale();
		String postalCodeApprox = null;
		
		// validering av postnummer fra welcome.html
		String postalcode = request.getParameter("postalcode");
		if(!StringUtils.isBlank(postalcode)){
			if(postalcode.length() != 4 || lessThanZeroOrNAN(postalcode)){
				saveErrorMessage(request, getText("welcome.postalcode.error", new String[] { postalcode }, locale));
				return new ModelAndView("redirect:welcome.html");
			}
			else {
				boolean approx = !PostalCodesSuperduperLoader.isValidPostalCode(postalcode);
				int numberOfTries = 0;
				int MAX_TRIES = 25;
				int pc = Integer.parseInt(postalcode);

				// håndterer kun grenseverdier slik at søk mot ikke-eksisterende x000 kun gir søk oppover
				// tilsvarende for ikke eksisterene x999 som kun vil gi søk nedover
				boolean doNotGoUp = pc % 1000 == 999;
				boolean doNotGoDown = pc % 1000 == 0;
				
				while(approx){
					// oppgitt postnummer finnes ikke - vi søker i omkringliggende nr-serier
					numberOfTries++;
					int pcUp = pc + numberOfTries;
					int pcDown = pc - numberOfTries;
					
					if(numberOfTries > MAX_TRIES){
						saveErrorMessage(request, getText("errors.postalCodeInvalid", new String[] { "(" + postalcode + ")" }, locale));
						return new ModelAndView("redirect:welcome.html");
					}

					if(!doNotGoUp && pcUp <= 9999 && PostalCodesSuperduperLoader.isValidPostalCode(StringUtils.leftPad(""+pcUp, 4, '0'))){
						postalCodeApprox = StringUtils.leftPad(""+pcUp, 4, '0');
						break;
					}
					if(!doNotGoDown && pcDown >= 0001 && PostalCodesSuperduperLoader.isValidPostalCode(StringUtils.leftPad(""+pcDown, 4, '0'))){
						postalCodeApprox = StringUtils.leftPad(""+pcDown, 4, '0');
						break;
					}
				}
			}
		}

		boolean avoidSearch = false;

		Course course = (Course) command;
		Course unpublished = new Course();

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
		if (StringUtils.isNotBlank(name)) {
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

		if (ApplicationResourcesUtil.isSVV()) { // alle LDAP-brukere ser alle møter (kurs), men endringstilgang styres av JSP.
			Integer[] status = null;

			if (roles.contains(Constants.ANONYMOUS_ROLE) && roles.size() == 1) {
				// publikumsbruker
				status = new Integer[] { CourseStatus.COURSE_PUBLISHED };
                model.put("enableExport", new Boolean(false));
			} else {
				// isReader / isEventResponsible / isEducationResponsible / isAdministrator
				status = new Integer[] { CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED };
			}

			if (StringUtils.isBlank(postalcode)) {
				courseList = courseManager.searchCourses(course, starttime, stoptime, status);
				courseList = enrichCoursesWithExternalValues(courseList, request);
				configureColumnView(status, courseList, model);
	        } else {
				List<Long> locationIds = locationManager.getLocationIds((postalCodeApprox!=null?postalCodeApprox:postalcode));
	        	int numberOfHits = 15; // default
	        	String numberOfHitsStr = getText("courseList.numberOfHits", locale);
	        	if(StringUtils.isNumeric(numberOfHitsStr)) numberOfHits = Integer.parseInt(numberOfHitsStr);
				courseList = courseManager.findByLocationIds(locationIds, numberOfHits);
				courseList = enrichCoursesWithExternalValues(courseList, request);

				model.put("containsUnfinished", new Boolean(true));
				model.put("containsFinished", new Boolean(false));
		        model.put("enableExport", new Boolean(false)); // deaktivering av fileksport for postnummersøkeresultat
				
				// melding til bruker som forklarer postnr-søk
				if(postalCodeApprox != null){
					saveMessage(request, getText("courseList.postalCodeInfoApprox", new String[] { postalcode, postalCodeApprox }, locale));
				}else {
					saveMessage(request, getText("courseList.postalCodeInfo", new String[] { postalcode }, locale));
				}
				
	        }
		} 
		else {
			Integer[] status = null;
			if (roles.contains(Constants.ANONYMOUS_ROLE) && roles.size() == 1) {
				// publikumsbruker
				status = new Integer[] { CourseStatus.COURSE_PUBLISHED };
                model.put("enableExport", new Boolean(false));
			} 
        	else if(roles.contains(Constants.ANONYMOUS_ROLE) && roles.contains(Constants.EMPLOYEE_ROLE) && roles.size() == 2){ 
        		// publikumsbruker
        		status = new Integer[]{ CourseStatus.COURSE_PUBLISHED };
                model.put("enableExport", new Boolean(false));
        	}
			else {
				// isReader / isEventResponsible / isEducationResponsible / isAdministrator
				status = new Integer[] { CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED, CourseStatus.COURSE_CANCELLED };
			}

			courseList = courseManager.searchCourses(course, starttime, stoptime, status);
			courseList = enrichCoursesWithExternalValues(courseList, request);
			configureColumnView(status, courseList, model);
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
            model.put("organizations", organizationManager.getByTypeIncludingDummy(type, getText(("misc.all.organizations" + value), locale)));
        } else {
            model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all.organizations", locale)));
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
    
    private boolean lessThanZeroOrNAN(String postalcode){
    	if(StringUtils.isNumeric(postalcode)){
    		return Integer.parseInt(postalcode) < 0; // postalcode < 0 return true / postalcode >= 0 return false 
    	}
    	else {
    		// negativt tall / ikke et tall
    		return true;
    	}
    }
    
}

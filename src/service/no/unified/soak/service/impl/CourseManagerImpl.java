/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 20. Dec 2005
 */
package no.unified.soak.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Person;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseAccessException;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.UserManager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.springframework.context.MessageSource;


/**
 * Implementation of CourseManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class CourseManagerImpl extends BaseManager implements CourseManager {
    private CourseDAO courseDAO;

    private ConfigurationManager configurationManager = null;
    private UserManager userManager = null;

	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

	/**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setCourseDAO(CourseDAO dao) {
        this.courseDAO = dao;
    }

    /**
     * @see no.unified.soak.service.CourseManager#getAllCourses()
     */
    public List getAllCourses() {
    	try {
        	return courseDAO.getAllCourses();
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    	    	return courseDAO.getAllCourses();
    		}
    		throw e;
    	}
    }

    /**
     * @see no.unified.soak.service.CourseManager#getCoursesWhereCategory()
     */
    public List<Course> getCoursesWhereCategory(Long categoryid) {
        try {
            return courseDAO.getCoursesWhereCategory(categoryid);
        }
        catch(StaleObjectStateException e){
            if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
                return courseDAO.getAllCourses();
            }
            throw e;
        }
    }

    /**
     * @see no.unified.soak.service.CourseManager#getCourse(String id)
     */
    public Course getCourse(final String id) {
    	Course result = null;
    	try {
        	if (!StringUtils.isEmpty(id))
            	result = courseDAO.getCourse(new Long(id));
        	else
        		log.error("Call to CourseManagerImpl.getCourse with empty or null id (" + id + ")");
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    	    	if (!StringUtils.isEmpty(id))
    	        	result = courseDAO.getCourse(new Long(id));
    	    	else
    	    		log.error("Call to CourseManagerImpl.getCourse with empty or null id (" + id + ")");
    		}
    		throw e;
    	}
		catch (CourseAccessException e) {
			String[] stringArr = {id};
			CourseAccessException courseAccessException = new CourseAccessException(messageSource.getMessage("courseErrorPage.message", stringArr, new Locale("no_NO")), e);
			throw courseAccessException;
		}
    	return result;
    }

    /**
     * @see no.unified.soak.service.CourseManager#saveCourse(Course course)
     */
    public void saveCourse(Course course) {
        courseDAO.saveCourse(course);
    }

    /**
     * @see no.unified.soak.service.CourseManager#removeCourse(String id)
     */
    public void removeCourse(final String id) {
    	if (!StringUtils.isEmpty(id))
    		courseDAO.removeCourse(new Long(id));
    	else
    		log.error("Call to CourseManagerImpl.removeCourse with empty or null id (" + id + ")");
    		
    }

    /**
     * @see no.unified.soak.service.CourseManager#searchCourses(no.unified.soak.model.Course, Date, Date)
     */
    public List<Course> searchCourses(Course course, Date startDate, Date stopDate, Integer[] status) {
    	boolean showUntilFinished = configurationManager.isActive("access.course.showCourseUntilFinished", true);
    	try {
    		return courseDAO.searchCourses(course, startDate, stopDate, status, showUntilFinished);
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    			return courseDAO.searchCourses(course, startDate, stopDate, status, showUntilFinished);
    		}
    		throw e;
    	}
    }

    /**
     * @see no.unified.soak.service.CourseManager#getWaitingListCourses()
     */
    public List<Course> getWaitingListCourses() {
    	try {
            return courseDAO.getWaitingListCourses();
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    	        return courseDAO.getWaitingListCourses();
    		}
    		throw e;
    	}
    }

    public List<Course> findByInstructor(Person person, Integer[] coursestatus) {
    	try {
            return courseDAO.findByInstructor(person, coursestatus);
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    	        return courseDAO.findByInstructor(person, coursestatus);
    		}
    		throw e;
    	}
    }

    public List<Course> findByLocationIds(List<Long> locationIds, Integer numberOfHits){
    	try {
        	return courseDAO.findByLocationIds(locationIds, numberOfHits);
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    	    	return courseDAO.findByLocationIds(locationIds, numberOfHits);
    		}
    		throw e;
    	}
    }

    public List<Course> findByLocationIdsAndCategory(List<Long> locationIds, Long categoryid, Integer numberOfHits) {
        try {
            return courseDAO.findByLocationIdsAndCategory(locationIds, categoryid, numberOfHits);
        }
        catch(StaleObjectStateException e){
            if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
                return courseDAO.findByLocationIds(locationIds, numberOfHits);
            }
            throw e;
        }
    }
    
    /**
     * @see no.unified.soak.service.CourseManager#getUnpublished()
     */
    public List<Course> getUnpublished(Course course) {
    	try {
            return courseDAO.getUnpublished(course);
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    	        return courseDAO.getUnpublished(course);
    		}
    		throw e;
    	}
    }

    /**
     * @see no.unified.soak.service.CourseManager#getUnpublished()
     */
    public List <String> getChangedList(Course originalCourse, Course changedCourse, String dateFormat) {
    	List <String> changedList = new ArrayList<String>();
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	
        if (originalCourse.getStatus().intValue() != changedCourse.getStatus().intValue()){
            changedList.add("status");
        }
		if (!originalCourse.getName().equals(changedCourse.getName())){
			changedList.add("name");
		}
		if (!StringUtils.equals(originalCourse.getType(), changedCourse.getType())){
			changedList.add("type");
		}
		if (!sdf.format(originalCourse.getStartTime()).equals(sdf.format(changedCourse.getStartTime()))){
			changedList.add("startTime");
		}
		if (!sdf.format(originalCourse.getStopTime()).equals(sdf.format(changedCourse.getStopTime()))){
			changedList.add("stopTime");
		}
		if (!StringUtils.equals(originalCourse.getDuration(), changedCourse.getDuration())) {
			changedList.add("duration");
		}
		if (!originalCourse.getOrganizationid().equals(changedCourse.getOrganizationid())){
			changedList.add("organization");
		}
		if ((originalCourse.getServiceAreaid() == null && changedCourse.getServiceAreaid() != null) || (originalCourse.getServiceAreaid() != null && !originalCourse.getServiceAreaid().equals(changedCourse.getServiceAreaid()))){
			changedList.add("serviceArea");
		}
		if (!originalCourse.getLocationid().equals(changedCourse.getLocationid())){
			changedList.add("location");
		}
		if (!originalCourse.getResponsibleUsername().equals(changedCourse.getResponsibleUsername())){
			changedList.add("responsible");
		}
		if (!originalCourse.getInstructorid().equals(changedCourse.getInstructorid())){
			changedList.add("instructor");
		}
		if (originalCourse.getMaxAttendants() != changedCourse.getMaxAttendants()){
			changedList.add("maxAttendants");
		}
		if (!StringUtils.equals(originalCourse.getDescription(), changedCourse.getDescription())){
			changedList.add("description");
		}
		if (originalCourse.getChargeoverdue() != changedCourse.getChargeoverdue()){
			changedList.add("chargeoverdue");
		}
		return changedList;
    }
    
    public List<Course> getCoursesWhereRegisterByExpired(long millis){
        return courseDAO.getCoursesWhereRegisterByExpired(millis);
    }
    
    @Override
    public void evict(Object entity) {
    	courseDAO.evict(entity);
    }
    
    @Override
    public void flush() {
    	courseDAO.flush();
    }
    
    @Override 
    public boolean contains(Object entity) {
    	return courseDAO.contains(entity);
    }
}

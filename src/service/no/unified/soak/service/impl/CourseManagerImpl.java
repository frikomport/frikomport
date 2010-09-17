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

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Person;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;

import org.apache.commons.lang.StringUtils;


/**
 * Implementation of CourseManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class CourseManagerImpl extends BaseManager implements CourseManager {
    private CourseDAO courseDAO;

    private ConfigurationManager configurationManager = null;

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
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
        return courseDAO.getAllCourses();
    }

    /**
     * @see no.unified.soak.service.CourseManager#getCourse(String id)
     */
    public Course getCourse(final String id) {
    	Course result = null;
    	if (!StringUtils.isEmpty(id))
        	result = courseDAO.getCourse(new Long(id));
    	else
    		log.error("Call to CourseManagerImpl.getCourse with empty or null id (" + id + ")");
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
    	return courseDAO.searchCourses(course, startDate, stopDate, status, showUntilFinished);
    }

    /**
     * @see no.unified.soak.service.CourseManager#getWaitingListCourses()
     */
    public List<Course> getWaitingListCourses() {
        return courseDAO.getWaitingListCourses();
    }

    public List<Course> findByInstructor(Person person) {
        return courseDAO.findByInstructor(person);
    }

    public List<Course> findByLocationIds(List<Long> locationIds, Integer numberOfHits){
    	return courseDAO.findByLocationIds(locationIds, numberOfHits);
    }
    
    /**
     * @see no.unified.soak.service.CourseManager#getUnpublished()
     */
    public List<Course> getUnpublished(Course course) {
        return courseDAO.getUnpublished(course);
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

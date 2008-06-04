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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.User;
import no.unified.soak.service.CourseManager;


/**
 * Implementation of CourseManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class CourseManagerImpl extends BaseManager implements CourseManager {
    private CourseDAO dao;

    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setCourseDAO(CourseDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.CourseManager#getCourses(no.unified.soak.model.Course)
     */
    public List getCourses(final Course course) {
        return dao.getCourses(course);
    }

    /**
     * @see no.unified.soak.service.CourseManager#getCourse(String id)
     */
    public Course getCourse(final String id) {
    	Course result = null;
    	if (!StringUtils.isEmpty(id))
        	result = dao.getCourse(new Long(id));
    	else
    		log.error("Call to CourseManagerImpl.getCourse with empty or null id (" + id + ")");
    	return result;
    }

    /**
     * @see no.unified.soak.service.CourseManager#saveCourse(Course course)
     */
    public void saveCourse(Course course) {
        dao.saveCourse(course);
    }

    /**
     * @see no.unified.soak.service.CourseManager#removeCourse(String id)
     */
    public void removeCourse(final String id) {
    	if (!StringUtils.isEmpty(id))
    		dao.removeCourse(new Long(id));
    	else
    		log.error("Call to CourseManagerImpl.removeCourse with empty or null id (" + id + ")");
    		
    }

    /**
     * @see no.unified.soak.service.CourseManager#searchCourses(no.unified.soak.model.Course)
     */
    public List searchCourses(Course course, Date startDate, Date stopDate) {
        return dao.searchCourses(course, startDate, stopDate);
    }

    /**
     * @see no.unified.soak.service.CourseManager#getWaitingListCourses()
     */
    public List getWaitingListCourses() {
        return dao.getWaitingListCourses();
    }

    /**
     * @see no.unified.soak.service.CourseManager#getUnpublished()
     */
    public List getUnpublished(User user) {
        return dao.getUnpublished(user);
    }
}

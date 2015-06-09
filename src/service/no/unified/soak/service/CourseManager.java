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
package no.unified.soak.service;

import java.util.Date;
import java.util.List;

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Person;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface CourseManager extends Manager {
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setCourseDAO(CourseDAO courseDAO);

    /**
     * Retrieves all of the courses
     */
    public List getAllCourses();

    /**
     * Retrieves all course with specified category
     */
    public List<Course> getCoursesWhereCategory(Long categoryid);

    /**
     * Gets course's information based on id.
     * @param id the course's id
     * @return course populated course object
     */
    public Course getCourse(final String id);

    /**
     * Saves a course's information
     * @param course the object to be saved
     */
    public void saveCourse(Course course);

    /**
     * Removes a course from the database by id
     * @param id the course's id
     */
    public void removeCourse(final String id);

    /**
     * Searches for all courses that apples to the conditions given
     * @param course contains the parameteres used for searching
     * @param startTime Course has to have the start date by this date
     * @param stopTime Course has to have the stop date by this date
     * @return list of all courses that applies to the given criteria
     */
    public List<Course> searchCourses(Course course, Date startTime, Date stopTime, Integer[] status);

    /**
     * Gets all unpublished courses
     * @return List of courses with status 0 @param user @param course
     */
    public List<Course> getUnpublished(Course course);

    /**
     * Finds all courses in the timespan between registerBy and startTime
     *
     * @return A list of all courses that needs looking into
     */
    public List<Course> getWaitingListCourses();

    public List<Course> findByInstructor(Person person, Integer[] coursestatus);

	public List<Course> findByLocationIds(List<Long> locationIds, Integer numberOfHits);
    public List<Course> findByLocationIdsAndCategory(List<Long> locationIds, Long categoryid, Integer numberOfHits);
	
    /**
     * Finds all changes relevant between original course and changed course relevant for 
     * users registered on the course.
     * @param originalCourse - course before change
     * @param changedCourse - course after change
     * @param dateFormat - format for date.
     *
     * @return A list of all changes 
     */
    public List<String> getChangedList(Course originalCourse, Course changedCourse, String dateFormat);
    
    public List<Course> getCoursesWhereRegisterByExpired(long millis);

}

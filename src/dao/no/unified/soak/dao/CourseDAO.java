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
package no.unified.soak.dao;

import java.util.Date;
import java.util.List;

import no.unified.soak.model.Course;
import no.unified.soak.model.Person;


/**
 * User Data Access Object (DAO) interface.
 *
 * @author hrj
 */
public interface CourseDAO extends DAO {
    /**
     * Retrieves all of the courses
     */
    public List<Course> getAllCourses();

    /**
     * Gets course's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the course's id
     * @return course populated course object
     */
    public Course getCourse(final Long id);

    /**
     * Saves a course's information
     * @param course the object to be saved
     */
    public void saveCourse(Course course);

    /**
    * Removes a course from the database by id
    * @param id the course's id
    */
    public void removeCourse(final Long id);

    /**
     * Searches for all courses that apples to the conditions given
     * @param course contains the parameteres used for searching
     * @param startDate Course has to have the start date by this date
     * @param stopDate Course has to have the stop date by this date
     * @return list of all courses that applies to the given criteria
     */
    public List<Course> searchCourses(Course course, Date startDate, Date stopDate, Integer[] status);

    /**
     * Finds all courses in the timespan between registerBy and startTime
     *
     * @return A list of all courses that needs looking into
     */
    public List<Course> getWaitingListCourses();

    /**
     * Gets all courses with status 0
     * @return list of all courses with status 0 @param course
     */
    public List<Course> getUnpublished(Course course);

    public List<Course> findByInstructor(Person person);
    
    public List<Course> getCoursesWhereRegisterByExpired(long millis);
	
    /**
     * Searches for published courses on given locations
     * Handles each locationId separately and in given order until list reaches numberOfHits
     * @param locationIds list of locationIds
     * @param numberOfHits max number of courses returned
     * @return list of courses limited by numberOfHits
     */
	public List<Course> findByLocationIds(List<Long> locationIds, Integer numberOfHits);
	
}

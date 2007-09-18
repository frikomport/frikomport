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

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;

import java.util.Date;
import java.util.List;


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
    public List getCourses(Course course);

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
     * @param historic if this is not set, only future courses will be returned
     * @param startTime Course has to have the start date by this date
     * @param stopTime Course has to have the stop date by this date
     * @return list of all courses that applies to the given criteria
     */
    public List searchCourses(Course course, Date startTime, Date stopTime);

    /**
     * Finds all courses in the timespan between registerBy and startTime
     *
     * @return A list of all courses that needs looking into
     */
    public List<Course> getWaitingListCourses();
}

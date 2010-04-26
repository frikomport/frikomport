/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao;

import no.unified.soak.model.Category;
import no.unified.soak.model.Course;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.Date;
import java.util.List;


/**
 * Test class for the CourseDAO class
 *
 * @author hrj
 */
public class CourseDAOTest extends BaseDAOTestCase {
    private Long courseId = new Long("1");
    private Course course = null;
    private CourseDAO dao = null;

    public void setCourseDAO(CourseDAO dao) {
        this.dao = dao;
    }

    /**
     * Tests the add new Course functionality. 1) Set required fields 2) Save
     * the new Course 3) Verify that it has been saved correctly
     *
     * @throws Exception
     */
    public void testAddCourse() throws Exception {
        course = new Course();

        // set required fields
        Long instructorid = new Long(1);
        course.setInstructorid(instructorid);

        Long responsibleid = new Long(1);
        course.setResponsibleid(responsibleid);

        String responsibleusername = "editor";
        course.setResponsibleUsername(responsibleusername);


        Long serviceAreaid = new Long(1);
        course.setServiceAreaid(serviceAreaid);

        String duration = "En times tid";
        course.setDuration(duration);

        Double feeExternal = new Double(1500.67);
        course.setFeeExternal(feeExternal);

        Double feeInternal = new Double(1000.50);
        course.setFeeInternal(feeInternal);

        Integer maxAttendants = new Integer(200);
        course.setMaxAttendants(maxAttendants);

        String name = "Et kurs";
        course.setName(name);

        java.util.Date registerBy = new Date(2005 - 12 - 15);
        course.setRegisterBy(registerBy);

        java.util.Date registerStart = new Date(2005 - 12 - 15);
        course.setRegisterStart(registerStart);

        Integer reservedInternal = new Integer(150);
        course.setReservedInternal(reservedInternal);

        java.util.Date startTime = new Date(2005 - 12 - 15);
        course.setStartTime(startTime);

        java.util.Date stopTime = new Date(2005 - 12 - 15);
        course.setStopTime(stopTime);

        Long locationid = new Long(1);
        course.setLocationid(locationid);

        Long organizationid = new Long(1);
        course.setOrganizationid(organizationid);

        String description = "Et test kurs";
        course.setDescription(description);
        
        String role = "Anonymous";
        course.setRole(role);

        Category category = new Category();
        category.setName("Kurs");
        course.setCategory(category);
        course.setCategoryid(1L);
        
        dao.saveCourse(course);

        // verify a primary key was assigned
        assertNotNull(course.getId());

        // verify set fields are same after save
        assertEquals(instructorid, course.getInstructorid());
        assertEquals(responsibleusername, course.getResponsibleUsername());
        assertEquals(serviceAreaid, course.getServiceAreaid());
        assertEquals(duration, course.getDuration());
        assertEquals(feeExternal, course.getFeeExternal());
        assertEquals(feeInternal, course.getFeeInternal());
        assertEquals(maxAttendants, course.getMaxAttendants());
        assertEquals(name, course.getName());
        assertEquals(registerBy, course.getRegisterBy());
        assertEquals(registerStart, course.getRegisterStart());
        assertEquals(reservedInternal, course.getReservedInternal());
        assertEquals(startTime, course.getStartTime());
        assertEquals(stopTime, course.getStopTime());
        assertEquals(locationid, course.getLocationid());
        assertEquals(organizationid, course.getOrganizationid());
        assertEquals(description, course.getDescription());
        assertEquals(role, course.getRole());
    }

    /**
     * Tests getting an existing Course Prerequisite: A Course with Id = 1
     * exists in the database
     *
     * @throws Exception
     */
    public void testGetCourse() throws Exception {
        course = dao.getCourse(courseId);
        assertNotNull(course);
    }

    /**
     * Tests getting a list of all Courses in the database Prerequisite: A
     * minimum of one Course must exist in the datbase
     *
     * @throws Exception
     */
    public void testGetCourses() throws Exception {
        course = new Course();

        List results = dao.getAllCourses();
        assertTrue(results.size() > 0);
    }

    /**
     * Tests the persistance of a Course to the databsae 1) Set required fields
     * 2) Save it 3) Verify that it has been stored correctly
     *
     * @throws Exception
     */
    public void testSaveCourse() throws Exception {
        course = dao.getCourse(courseId);

        // update required fields
        Long instructorid = new Long(2);
        course.setInstructorid(instructorid);

        String responsible = "admin";
        course.setResponsibleUsername(responsible);

        Long serviceAreaid = new Long(2);
        course.setServiceAreaid(serviceAreaid);

        String duration = "En times tid";
        course.setDuration(duration);

        Double feeExternal = new Double(4.908686985688043E307);
        course.setFeeExternal(feeExternal);

        Double feeInternal = new Double(2.319715755428284E306);
        course.setFeeInternal(feeInternal);

        Integer maxAttendants = new Integer(299363422);
        course.setMaxAttendants(maxAttendants);

        String name = "Kursnavn";
        course.setName(name);

        java.util.Date registerBy = new Date(2005 - 12 - 15);
        course.setRegisterBy(registerBy);

        java.util.Date registerStart = new Date(2005 - 12 - 15);
        course.setRegisterStart(registerStart);

        Integer reservedInternal = new Integer(410209687);
        course.setReservedInternal(reservedInternal);

        java.util.Date startTime = new Date(2005 - 12 - 15);
        course.setStartTime(startTime);

        java.util.Date stopTime = new Date(2005 - 12 - 15);
        course.setStopTime(stopTime);

        Long locationid = new Long(2);
        course.setLocationid(locationid);

        Long organizationid = new Long(2);
        course.setOrganizationid(organizationid);

        String description = "Et test kurs";
        course.setDescription(description);
        
        String role = "Ansatt";
        course.setRole(role);

        dao.saveCourse(course);

        assertEquals(instructorid, course.getInstructorid());
        assertEquals(responsible, course.getResponsibleUsername());
        assertEquals(serviceAreaid, course.getServiceAreaid());
        assertEquals(duration, course.getDuration());
        assertEquals(feeExternal, course.getFeeExternal());
        assertEquals(feeInternal, course.getFeeInternal());
        assertEquals(maxAttendants, course.getMaxAttendants());
        assertEquals(name, course.getName());
        assertEquals(registerBy, course.getRegisterBy());
        assertEquals(registerStart, course.getRegisterStart());
        assertEquals(reservedInternal, course.getReservedInternal());
        assertEquals(startTime, course.getStartTime());
        assertEquals(stopTime, course.getStopTime());
        assertEquals(locationid, course.getLocationid());
        assertEquals(organizationid, course.getOrganizationid());
        assertEquals(description, course.getDescription());
        assertEquals(role, course.getRole());
    }

    /**
     * Tests the removal of an existing Course Prerequisite: A Course with id =
     * 3 must exist in the database
     *
     * 1) Remove Course 2) Try to retrieve the (hopefully) deleted object from
     * the db 3) If Exception gotten => Object not found => Everything is ok!
     *
     * @throws Exception
     */
    public void testRemoveCourse() throws Exception {
        Long removeId = new Long("3");
        dao.removeCourse(removeId);

        try {
            dao.getCourse(removeId);
            fail("course found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Tests the basic search functionaly for Course
     *
     * Prerequisite: A Course with organizationid=1 and serviceAreaid=1
     * where all dates are in the futur must exist in the database
     *
     * 1) Set the object
     * 2) Search
     * 3) See if we got any results (we should get at least one)
     *
     * @throws Exception
     */
    public void testSearchCourses() throws Exception {
        Course course = new Course();
        course.setOrganizationid(new Long("1"));
        course.setServiceAreaid(new Long("1"));

        List searchResults = dao.searchCourses(course, null, null);
        assertNotNull(searchResults);
    }

    /**
     * Tests to see if any courses with status 0 is present.
     * @throws Exception
     */
    public void testGetUnpublished() throws Exception {
        List unpublished = dao.getUnpublished(new Course());
        assertNotNull(unpublished);
    }
}

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
import no.unified.soak.service.impl.CourseManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Test class for CourseManager
 *
 * @author hrj
 */
public class CourseManagerTest extends BaseManagerTestCase {
    private final String courseId = "1";
    private CourseManager courseManager = new CourseManagerImpl();
    private Mock courseDAO = null;
    private Course course = null;

    protected void setUp() throws Exception {
        super.setUp();
        courseDAO = new Mock(CourseDAO.class);
        courseManager.setCourseDAO((CourseDAO) courseDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        courseManager = null;
    }

    /**
     * Tests getting a list of Course using a mock DAO
     *
     * @throws Exception
     */
    public void testGetCourses() throws Exception {
        List results = new ArrayList();
        course = new Course();
        results.add(course);

        // set expected behavior on dao
        courseDAO.expects(once()).method("getCourses").will(returnValue(results));

        List courses = courseManager.getCourses(null);
        assertTrue(courses.size() == 1);
        courseDAO.verify();
    }

    /**
     * Tests searching for courses using a mock DAO
     * @throws Exception
     */
    public void testSearchCourses() throws Exception {
        course = new Course();
        course.setMunicipalityid(new Long("1"));
        course.setServiceAreaid(new Long("1"));

        courseDAO.expects(once()).method("searchCourses")
                 .will(returnValue(new ArrayList()));

        List searchResults = courseManager.searchCourses(course, null, null);
        assertNotNull(searchResults);
        courseDAO.verify();
    }

    /**
     * Tests getting a single Course using a mock DAO
     *
     * @throws Exception
     */
    public void testGetCourse() throws Exception {
        // set expected behavior on dao
        courseDAO.expects(once()).method("getCourse")
                 .will(returnValue(new Course()));
        course = courseManager.getCourse(courseId);
        assertTrue(course != null);
        courseDAO.verify();
    }

    /**
     * Tests persisting a Course using a mock DAO
     *
     * @throws Exception
     */
    public void testSaveCourse() throws Exception {
        // set expected behavior on dao
        courseDAO.expects(once()).method("saveCourse").with(same(course))
                 .isVoid();

        courseManager.saveCourse(course);
        courseDAO.verify();
    }

    /**
     * Tests adding and then removing a Course using a mock DAO
     *
     * 1) Set the required fields
     * 2) "Save" it
     * 3) "Remove it"
     * 4) Check that everything went according to plan
     *
     * @throws Exception
     */
    public void testAddAndRemoveCourse() throws Exception {
        course = new Course();

        // set required fields
        course.setInstructorid(new Long(152051052));
        course.setResponsibleid(new Long(737098585));
        course.setServiceAreaid(new Long(1072915406));
        course.setDuration("En times tid");
        course.setFeeExternal(new Double(1.861804799761001E307));
        course.setFeeMunicipal(new Double(7.100663138877625E307));
        course.setFreezeAttendance(new Date(2005 - 12 - 15));
        course.setMaxAttendants(new Integer(329979612));
        course.setName("Test av kursnavn");
        course.setRegisterBy(new Date(2005 - 12 - 15));
        course.setRegisterStart(new Date(2005 - 12 - 15));
        course.setReservedMunicipal(new Integer(1700392601));
        course.setStartTime(new Date(2005 - 12 - 15));
        course.setStopTime(new Date(2005 - 12 - 15));
        course.setLocationid(new Long(2115135111));
        course.setMunicipalityid(new Long(1505726338));
        course.setDescription("Et test kurs");
        course.setRole("Anonymous");

        // set expected behavior on dao
        courseDAO.expects(once()).method("saveCourse").with(same(course))
                 .isVoid();
        courseManager.saveCourse(course);
        courseDAO.verify();

        // reset expectations
        courseDAO.reset();

        courseDAO.expects(once()).method("removeCourse")
                 .with(eq(new Long(courseId)));
        courseManager.removeCourse(courseId);
        courseDAO.verify();

        // reset expectations
        courseDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Course.class,
                course.getId());
        courseDAO.expects(once()).method("removeCourse").isVoid();
        courseDAO.expects(once()).method("getCourse").will(throwException(ex));
        courseManager.removeCourse(courseId);

        try {
            courseManager.getCourse(courseId);
            fail("Course with identifier '" + courseId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        courseDAO.verify();
    }
}

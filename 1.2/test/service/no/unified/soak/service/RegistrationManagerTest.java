/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 21. dec 2005
 */
package no.unified.soak.service;

import no.unified.soak.dao.RegistrationDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.impl.RegistrationManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Test class for RegistrationManager
 *
 * @author hrj
 */
public class RegistrationManagerTest extends BaseManagerTestCase {
    private final String registrationId = "1";
    private RegistrationManager registrationManager = new RegistrationManagerImpl();
    private Mock registrationDAO = null;
    private Registration registration = null;
    private Course course = null;

    protected void setUp() throws Exception {
        super.setUp();
        registrationDAO = new Mock(RegistrationDAO.class);
        registrationManager.setRegistrationDAO((RegistrationDAO) registrationDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        registrationManager = null;
    }

    /**
     * Tests getting a list of Registrations using a mock DAO
     *
     * @throws Exception
     */
    public void testGetRegistrations() throws Exception {
        List results = new ArrayList();
        registration = new Registration();
        results.add(registration);

        // set expected behavior on dao
        registrationDAO.expects(once()).method("getRegistrations")
                       .will(returnValue(results));

        List registrations = registrationManager.getRegistrations(null);
        assertTrue(registrations.size() == 1);
        registrationDAO.verify();
    }

    /**
     * Tests getting a single Registration using a mock DAO
     *
     * @throws Exception
     */
    public void testGetRegistration() throws Exception {
        // set expected behavior on dao
        registrationDAO.expects(once()).method("getRegistration")
                       .will(returnValue(new Registration()));
        registration = registrationManager.getRegistration(registrationId);
        assertTrue(registration != null);
        registrationDAO.verify();
    }

    /**
     * Tests persisting a Registration using a mock DAO
     *
     * @throws Exception
     */
    public void testSaveRegistration() throws Exception {
        // set expected behavior on dao
        registrationDAO.expects(once()).method("saveRegistration")
                       .with(same(registration)).isVoid();

        registrationManager.saveRegistration(registration);
        registrationDAO.verify();
    }

    /**
     * Test the getNumberOfAttendants function
     *
     * 1. Set up the required variables 2. Set up the mock 3. Execute 4. See
     * that the result is what we expected
     *
     * @throws Exception
     */
    public void testGetNumberOfAttendants() throws Exception {
        course = new Course();
        course.setId(new Long(1));
        course.setMaxAttendants(new Integer(50));
        course.setReservedMunicipal(new Integer(50));
        course.setMunicipalityid(new Long(1));

        Boolean localsOnly = new Boolean(true);

        // set expected behavoir on dao
        registrationDAO.expects(once()).method("getNumberOfAttendants")
                       .will(returnValue(new Integer(1)));
        registrationManager.getNumberOfAttendants(localsOnly, course);
        registrationDAO.verify();
    }

    /**
     * Test the getAvailability function
     *
     * 1. Set up the required variables 2. Set up the mock 3. Execute 4. See
     * that the result is what we expected
     *
     * @throws Exception
     */
    public void testGetAvailablity() throws Exception {
        course = new Course();
        course.setId(new Long(1));
        course.setMaxAttendants(new Integer(50));
        course.setReservedMunicipal(new Integer(50));
        course.setMunicipalityid(new Long(1));

        Boolean localsOnly = new Boolean(true);

        // set expected behavoir on dao
        registrationDAO.expects(once()).method("getAvailability")
                       .will(returnValue(new Integer(1)));
        registrationManager.getAvailability(localsOnly, course);
        registrationDAO.verify();
    }

    /**
     * Test the getSpecificRegistration-function
     * 1. Set up required variables
     * 2. Set up the mock
     * 3. Exectue
     * 4. Check that the right returntype was given
     *
     * @throws Exception
     */
    public void testGetSpecificRegistrations() throws Exception {
        Long courseId = new Long(1);
        Long municipalityId = new Long(1);
        Long serviceareaId = new Long(1);
        Boolean reserved = new Boolean(true);
        Boolean invoiced = new Boolean(true);
		List limitToCourses = null;

        registrationDAO.expects(once()).method("getSpecificRegistrations")
                       .will(returnValue(new ArrayList()));
        registrationManager.getSpecificRegistrations(courseId, municipalityId,
            serviceareaId, reserved, invoiced, limitToCourses);
        registrationDAO.verify();
    }

    /**
     * Tests adding and then removing a Registration using a mock DAO
     *
     * 1) Set the required fields 2) "Save" it 3) "Remove it" 4) Check that
     * everything went according to plan
     *
     * @throws Exception
     */
    public void testAddAndRemoveRegistration() throws Exception {
        registration = new Registration();

        // set required fields
        registration.setCourseid(new Long(1));
        registration.setInvoiced(new Boolean("false"));
        registration.setMunicipalityid(new Long(1));
        registration.setRegistered(new Date());
        registration.setReserved(new Boolean("false"));
        registration.setServiceareaid(new Long(1));

        // set expected behavior on dao
        registrationDAO.expects(once()).method("saveRegistration")
                       .with(same(registration)).isVoid();
        registrationManager.saveRegistration(registration);
        registrationDAO.verify();

        // reset expectations
        registrationDAO.reset();

        registrationDAO.expects(once()).method("removeRegistration")
                       .with(eq(new Long(registrationId)));
        registrationManager.removeRegistration(registrationId);
        registrationDAO.verify();

        // reset expectations
        registrationDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Registration.class,
                registration.getId());
        registrationDAO.expects(once()).method("removeRegistration").isVoid();
        registrationDAO.expects(once()).method("getRegistration")
                       .will(throwException(ex));
        registrationManager.removeRegistration(registrationId);

        try {
            registrationManager.getRegistration(registrationId);
            fail("Registration with identifier '" + registrationId +
                "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        registrationDAO.verify();
    }
}

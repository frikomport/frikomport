/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created 20. des 2005
 */
package no.unified.soak.dao;

import java.util.Date;
import java.util.List;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;

import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * Test class for the RegistrationDAO class
 * 
 * @author hrj
 */
public class RegistrationDAOTest extends BaseDAOTestCase {
	private Long registrationId = new Long("1");

	private Registration registration = null;

	private RegistrationDAO dao = null;

	public void setRegistrationDAO(RegistrationDAO dao) {
		this.dao = dao;
	}

	/**
	 * Tests the add new Registration functionality. 1) Set required fields 2)
	 * Save the new Registration 3) Verify that it has been saved correctly
	 * 
	 * @throws Exception
	 */
	public void testAddRegistration() throws Exception {
		registration = new Registration();

		// set required fields
		Long courseid = new Long(1);
		registration.setCourseid(courseid);

		Boolean invoiced = new Boolean("false");
		registration.setInvoiced(invoiced);

		Long organizationid = new Long(1);
		registration.setOrganizationid(organizationid);

		Date registered = new Date();
		registration.setRegistered(registered);

		Status status = Registration.Status.WAITING;
		registration.setStatus(status);

		Long serviceareaid = new Long(1);
		registration.setServiceAreaid(serviceareaid);
		
		String firstname = new String("fn");
		registration.setFirstName(firstname);

		String lastname = new String("ln");
		registration.setLastName(lastname);

		String email = new String("e@ma.il");
		registration.setEmail(email);
		
		String locale = new String("no");
		registration.setLocale(locale);
		
		String comment = new String("Kommentar");
		registration.setComment(comment);
		
		Boolean attended = new Boolean(false);
		registration.setAttended(attended);
		
		String workplace = "Arbeidssted";
		registration.setWorkplace(workplace);

		dao.saveRegistration(registration);

		// verify a primary key was assigned
		assertNotNull(registration.getId());

		// verify set fields are same after save
		assertEquals(courseid, registration.getCourseid());
		assertEquals(invoiced, registration.getInvoiced());
		assertEquals(organizationid, registration.getOrganizationid());
		assertEquals(registered, registration.getRegistered());
		assertEquals(status, registration.getStatusAsEnum());
		assertEquals(serviceareaid, registration.getServiceAreaid());
		assertEquals(locale, registration.getLocale());
		assertEquals(comment, registration.getComment());
		assertEquals(attended, registration.getAttended());
		assertEquals(workplace, registration.getWorkplace());
	}

	/**
	 * Tests getting an existing Registration Prerequisite: A Registration with
	 * Id = 1 exists in the database
	 * 
	 * @throws Exception
	 */
	public void testGetRegistration() throws Exception {
		registration = dao.getRegistration(registrationId);
		assertNotNull(registration);
	}

	/**
	 * Tests getting a list of all Registrations in the database Prerequisite: A
	 * minimum of one Registration must exist in the datbase
	 * 
	 * @throws Exception
	 */
	public void testGetRegistrations() throws Exception {
		registration = new Registration();

		List results = dao.getRegistrations(registration);
		assertTrue(results.size() > 0);
	}

	/**
	 * Test the getSpecificRegistrations function. Prerequisite: A registration
	 * in the database with - id=1 - serviceareaid=1 - organizationid=1 -
	 * status=RESERVED - invoiced=true
	 * 
	 * 1. Readies all needed parameteres 2. Executes the function 3. Asserts
	 * that the result is as expected
	 * 
	 * @throws Exception
	 */
	public void testGetSpecificRegistrations() throws Exception {
		Long courseId = new Long(1);
		Long organizationId = new Long(1);
		Long serviceAreaId = new Long(1);
		Registration.Status status = Registration.Status.RESERVED;
		String firstname = null;
		String lastname = null;
		Boolean invoiced = new Boolean(true);
		Boolean attended = new Boolean(false);
		List limitToCourses = null;
		String[] orderBy = null;
		
		List courses = dao.getSpecificRegistrations(courseId, organizationId,
				serviceAreaId, status, firstname, lastname, invoiced, attended, limitToCourses, orderBy);
		assertTrue(courses.size() == 1);
	}

	/**
	 * Tests the persistance of a Registration to the databsae 1) Set required
	 * fields 2) Save it 3) Verify that it has been stored correctly
	 * 
	 * @throws Exception
	 */
	public void testSaveRegistration() throws Exception {
		registration = dao.getRegistration(registrationId);

		// update required fields
		Long courseid = new Long(2);
		registration.setCourseid(courseid);

		Boolean invoiced = new Boolean("false");
		registration.setInvoiced(invoiced);

		Long organizationid = new Long(2);
		registration.setOrganizationid(organizationid);

		Date registered = new Date();
		registration.setRegistered(registered);

		Status status = Registration.Status.WAITING;
		registration.setStatus(status);

		Long serviceAreaid = new Long(2);
		registration.setServiceAreaid(serviceAreaid);
		
		Boolean attended = new Boolean("false");
		registration.setAttended(attended);

		dao.saveRegistration(registration);

		assertEquals(courseid, registration.getCourseid());
		assertEquals(invoiced, registration.getInvoiced());
		assertEquals(organizationid, registration.getOrganizationid());
		assertEquals(registered, registration.getRegistered());
		assertEquals(status, registration.getStatusAsEnum());
		assertEquals(serviceAreaid, registration.getServiceAreaid());
		assertEquals(attended, registration.getAttended());
	}

	/**
	 * Tests the removal of an existing Registration Prerequisite: A
	 * Registration with id = 3 must exist in the database
	 * 
	 * 1) Remove Registration 2) Try to retrieve the (hopefully) deleted object
	 * from the db 3) If Exception gotten => Object not found => Everything is
	 * ok!
	 * 
	 * @throws Exception
	 */
	public void testRemoveRegistration() throws Exception {
		Long removeId = new Long("4");
		dao.removeRegistration(removeId);

		try {
			dao.getRegistration(removeId);
			fail("registration found in database");
		} catch (ObjectRetrievalFailureException e) {
			assertNotNull(e.getMessage());
		}
	}

	/**
	 * Test getting number of attendants on a course Prerequisite: At least one
	 * local attendant on course with id=1
	 * 
	 * 1. Set up the "mock" course object 2. Get the number of local attendants
	 * of the course 3. Check that the result is not null (should never happen)
	 * 4. Check that there is more than zero attendants
	 * 
	 * @throws Exception
	 */
	public void testGetNumberOfAttendants() throws Exception {
		Course course = new Course();
		course.setId(new Long(1));
		course.setMaxAttendants(new Integer(50));
		course.setReservedInternal(new Integer(50));
		course.setOrganizationid(new Long(1));

		Boolean local = new Boolean(true);

		Integer result = dao.getNumberOfAttendants(local, course, null);

		// Should never be null
		assertNotNull(result);
		assertTrue(result.intValue() > 0);
	}

	/**
	 * Tests finding if there are available seats on the course Prerequisite:
	 * none
	 * 
	 * 1) Create a "fake" Course-object 2) Retrieve the number of seats
	 * available 3) Check that the result is not null
	 * 
	 * @throws Exception
	 */
	public void testGetAvailablity() throws Exception {
		// Create a course that we can use
		Course course = new Course();
		course.setId(new Long(1));
		course.setMaxAttendants(new Integer(50));
		course.setReservedInternal(new Integer(50));
		course.setOrganizationid(new Long(1));

		Boolean local = new Boolean(true);

		Integer result = dao.getAvailability(local, course);
		assertNotNull(result);

		local = new Boolean(false);
		result = dao.getAvailability(local, course);
		assertNotNull(result);
	}
}

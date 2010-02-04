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

import java.util.Collection;
import java.util.List;

import no.unified.soak.dao.RegistrationDAO;
import no.unified.soak.dao.hibernate.RegistrationStatusCriteria;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;

/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 * 
 * @author hrj
 */
public interface RegistrationManager extends Manager {
	/**
	 * Setter for DAO, convenient for unit testing
	 */
	public void setRegistrationDAO(RegistrationDAO registrationDAO);

	/**
	 * Retrieves all of the registrations
	 */
	public List getRegistrations(Registration registration);

	/**
	 * Gets registration's information based on id.
	 * 
	 * @param id
	 *            the registration's id
	 * @return registration populated registration object
	 */
	public Registration getRegistration(final String id);

	/**
	 * Saves a registration's information
	 * 
	 * @param registration
	 *            the object to be saved
	 */
	public void saveRegistration(Registration registration);

	/**
	 * Removes a registration from the database by id
	 * 
	 * @param id
	 *            the registration's id
	 */
	public void removeRegistration(final String id);

	/**
	 * Marks a registration as canceled in the database, by id
	 * 
	 * @param id
	 *            the registration's id
	 */
	public void cancelRegistration(final String id);

	/**
	 * Checks availabilty on a given course for a person.
	 * 
	 * @param localAttendant
	 *            Is this query for a local attendant?
	 * @param course
	 *            The course in question
	 * @return Number of spots available for this person on this course
	 */
	public Integer getAvailability(Boolean localAttendant, Course course);

	/**
	 * Returns the number of attendants registered to the course. If the
	 * localsOnly parameter is set, the function returns the number of
	 * participants that belong to the same organization that is responsible for
	 * the course
	 * 
	 * @param localOnly
	 *            Include only local participants?
	 * @param course
	 *            The course in questions
	 * @return the number of attendants that fulfil the critera
	 */
	public Integer getNumberOfAttendants(Boolean localOnly, Course course);
	
	/**
	 * Returns a list of registration based on serveral given vital attributes.
	 * If a parameter is set to null, it is ignored, hence a call to this
	 * function with all the parameters set to null would return all
	 * registrations.
	 * 
	 * @param courseId
	 *            Restrict list to registrations to this specific course
	 * @param organizationId
	 *            Restrict list to registrations where the registered user
	 *            belongs to this specific organization
	 * @param serviceareaId
	 *            Restrict list to registrations where the registered user
	 *            belongs to this service area
	 * @param status
	 *            Restrict list to registrations where the registration have a
	 *            specific status.
	 * @param invoiced
	 *            Restrict list to registrations where the registration has been
	 *            invoiced (true) or not (false)
	 * @param attended
	 *            Restrict list to registrations where the course is attended
	 *            (true) or not (false)
	 * @param orderBy
	 *            Specify ordering.
	 * @return List of Courses
	 */
	public List getSpecificRegistrations(Long courseId, Long organizationId, Long serviceareaId, Registration.Status status,
			Boolean invoiced, Boolean attended, Collection limitToCourses, String[] orderBy);

	/**
	 * Returns a list of registration based on serveral given vital attributes.
	 * If a parameter is set to null, it is ignored, hence a call to this
	 * function with all the parameters set to null would return all
	 * registrations.
	 * 
	 * @param courseId
	 *            Restrict list to registrations to this specific course
	 * @param organizationId
	 *            Restrict list to registrations where the registered user
	 *            belongs to this specific organization
	 * @param serviceareaId
	 *            Restrict list to registrations where the registered user
	 *            belongs to this service area
	 * @param status
	 *            Restrict list to registrations where the registration have one
	 *            of the statuses in statusCriteria.
	 * @param invoiced
	 *            Restrict list to registrations where the registration has been
	 *            invoiced (true) or not (false)
	 * @param attended
	 *            Restrict list to registrations where the course is attended
	 *            (true) or not (false)
	 * @param orderBy
	 *            Specify ordering.
	 * @return List of Courses
	 */
	public List getSpecificRegistrations(Long courseId, Long organizationId, Long serviceareaId, RegistrationStatusCriteria statusCriteria,
			Boolean invoiced, Boolean attended, Collection limitToCourses, String[] orderBy);

	/**
	 * Returns all registrations that are not confirmed (and thus are on the
	 * waiting list)
	 * 
	 * @param courseIds
	 *            A list with all the ids of the courses that we are looking at
	 * @return a list of all registrations that have not been confirmed within
	 *         the given courses
	 */
	public List<Registration> getWaitingListRegistrations(List courseIds);

	/**
	 * Returns the number of taken seats at the course
	 * 
	 * @param course
	 *            The course to examine the registrations for
	 * @param localOnly
	 *            Only include local attendants
	 * @return The number of occupied seats at the course
	 */
	public Integer getNumberOfOccupiedSeats(Course course, Boolean localOnly);

	/**
	 * 
	 * Retrieves all registrations for a given course
	 * 
	 * @param courseId
	 *            the Id of the course to get the registrations for
	 * @return A list of all registrations for the given course
	 */
	public List<Registration> getCourseRegistrations(Long courseId);

	/**
	 * Get all registrations for specified user
	 * 
	 * @param username
	 * @return List of registrations
	 */
	public List getUserRegistrations(String username);

	/**
	 * Get all registration on specified course for specified user
	 * 
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @param courseId
	 * @return List of registrations
	 */
	public List<Registration> getUserRegistrationsForCourse(String email,
			String firstname, String lastname, Long courseId);

	/**
	 * Get registration(s) on specified course for specified user
	 * 
	 * @param username
	 * @param courseId
	 */
	public List<Registration> getUserRegistrationsForCourse(String username,
			Long courseId);

	/**
	 * Moves registrations from old user to new user
	 * 
	 * @param olduser
	 * @param newuser
	 */
	void moveRegistrations(User olduser, User newuser);

}

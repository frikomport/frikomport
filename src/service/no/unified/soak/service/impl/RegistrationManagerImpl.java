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
package no.unified.soak.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

import no.unified.soak.dao.RegistrationDAO;
import no.unified.soak.dao.hibernate.RegistrationStatusCriteria;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.RegistrationManager;

/**
 * Implementation of RegistrationManager interface to talk to the persistence
 * layer.
 * 
 * @author hrj
 */
public class RegistrationManagerImpl extends BaseManager implements
		RegistrationManager {
	private RegistrationDAO dao;

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */
	public void setRegistrationDAO(RegistrationDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getRegistrations(no.unified.soak.model.Registration)
	 */
	public List getRegistrations(final Registration registration) {
		return dao.getRegistrations(registration);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getRegistration(String
	 *      id)
	 */
	public Registration getRegistration(final String id) {
		return dao.getRegistration(new Long(id));
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#saveRegistration(Registration
	 *      registration)
	 */
	public void saveRegistration(Registration registration) {
		dao.saveRegistration(registration);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#removeRegistration(String
	 *      id)
	 */
	public void removeRegistration(final String id) {
		dao.removeRegistration(new Long(id));
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#cancelRegistration(String
	 *      id)
	 */
	public void cancelRegistration(String id) {
		Registration registration = dao.getRegistration(new Long(id));
		registration.setStatus(Registration.Status.CANCELED);
		dao.saveRegistration(registration);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getAvailability(java.lang.Boolean,
	 *      Course)
	 */
	public Integer getAvailability(Boolean localAttendant, Course course) {
		return dao.getAvailability(localAttendant, course);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getNumberOfAttendants(java.lang.Boolean,
	 *      no.unified.soak.model.Course)
	 */
	public Integer getNumberOfAttendants(Boolean localOnly, Course course) {
		return dao.getNumberOfAttendants(localOnly, course, null);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getNumberOfAttendants(java.lang.Boolean,
	 *      no.unified.soak.model.Course)
	 */
	public Integer getNumberOfAttendants(Boolean localOnly, Course course,
			Boolean reserved) {
		return dao.getNumberOfAttendants(localOnly, course, reserved);
	}
	
	/**
	 * @see no.unified.soak.service.RegistrationManager#getSpecificRegistrations(java.lang.Long,
	 *      java.lang.Long, java.lang.Long, java.lang.Boolean,
	 *      java.lang.Boolean)
	 */
	public List getSpecificRegistrations(Long courseId, Long organizationId,
			Long serviceareaId, Registration.Status status, String firstname, String lastname, Boolean invoiced,
			Boolean attended, Collection limitToCourses, String[] orderBy) {
		return dao.getSpecificRegistrations(courseId, organizationId,
				serviceareaId, status, firstname, lastname, invoiced, attended, limitToCourses,
				orderBy);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getSpecificRegistrations(Long,
	 *      Long, Long, RegistrationStatusCriteria, Boolean, Boolean,
	 *      Collection, String[])
	 */
	public List getSpecificRegistrations(Long courseId, Long organizationId, Long serviceareaId,
			RegistrationStatusCriteria statusCriteria, String firstname, String lastname, Boolean invoiced, Boolean attended, Collection limitToCourses,
			String[] orderBy) {
		return dao.getSpecificRegistrations(courseId, organizationId, serviceareaId, statusCriteria, firstname, lastname, invoiced, attended,
				limitToCourses, orderBy);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getWaitingListRegistrations(java.util.List)
	 */
	public List<Registration> getWaitingListRegistrations(List courseIds) {
		return dao.getWaitingListRegistrations(courseIds);
	}

	/**
	 * @see no.unified.soak.service.RegistrationManager#getNumberOfOccupiedSeats(no.unified.soak.model.Course,
	 *      java.lang.Boolean)
	 */
	public Integer getNumberOfOccupiedSeats(Course course, Boolean localOnly) {
		return dao.getNumberOfOccupiedSeats(course, localOnly);
	}

	public List<Registration> getRegistrations(Long courseId) {
		return dao.getCourseRegistrations(courseId);
	}

	public List<Registration> getCourseRegistrations(Long courseId) {
		return dao.getCourseRegistrations(courseId);
	}

	public List<Registration> getUserRegistrations(String username) {
		return dao.getUserRegistrations(username);
	}

	public List<Registration> getUserRegistrationsForCourse(String email,
			String firstname, String lastname, Long courseId) {
		return dao.getUserRegistationsForCourse(email, firstname, lastname,
				courseId);
	}

	public List<Registration> getUserRegistrationsForCourse(String username, Long courseId) {
		return dao.getUserRegistationsForCourse(username, courseId);
	}
	
	public void moveRegistrations(User olduser, User newuser) {
		List<Registration> registrations = getUserRegistrations(olduser
				.getUsername());
		Iterator<Registration> it = registrations.iterator();
		while (it.hasNext()) {
			Registration registration = it.next();
			registration.setUser(newuser);
			registration.setUsername(newuser.getUsername());
			saveRegistration(registration);
		}
	}

	public Integer getNumberOfRegistrations(Long courseId){
		return dao.getNumberOfRegistrations(courseId);
	}

	
	/**
	 * Evict entity for hibernate sessions. This avoids automatic saving
	 * (flush) of the entity.
	 * 
	 * @param entity
	 */
	public void evict(Object entity) {
		dao.evict(entity);
	}

}

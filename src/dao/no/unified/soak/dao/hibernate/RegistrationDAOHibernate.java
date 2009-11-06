/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created Dec 21. 2005
 */
package no.unified.soak.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.unified.soak.dao.RegistrationDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;
import no.unified.soak.model.Registration;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * Implementation of the RegistrationDAO
 * 
 * @author hrj
 */
public class RegistrationDAOHibernate extends BaseDAOHibernate implements RegistrationDAO {
    
    /**
	 * @see no.unified.soak.dao.RegistrationDAO#getRegistrations(no.unified.soak.model.Registration)
	 */
	public List getRegistrations(final Registration registration) {
		return getHibernateTemplate().find("from Registration");
	}

    /**
	 * @see no.unified.soak.dao.RegistrationDAO#getRegistration(Long id)
	 */
	public Registration getRegistration(final Long id) {
		Registration registration = (Registration) getHibernateTemplate().get(
				Registration.class, id);

		if (registration == null) {
			log.warn("uh oh, registration with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Registration.class, id);
		}

		return registration;
	}

	/**
	 * @see no.unified.soak.dao.RegistrationDAO#saveRegistration(Registration
	 *      registration)
	 */
	public void saveRegistration(final Registration registration) {
        getHibernateTemplate().saveOrUpdate(registration);
        getHibernateTemplate().flush();
    }

	/**
	 * @see no.unified.soak.dao.RegistrationDAO#removeRegistration(Long id)
	 */
	public void removeRegistration(final Long id) {
		
		// First we need to make sure there are no Notifications to this registration
		DetachedCriteria criteria = DetachedCriteria
		.forClass(Notification.class);
		criteria.add(Restrictions.eq("registrationid", id));
		List result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0) {
			for (int i=0; i < result.size(); i++) {
				Notification notification = (Notification) result.get(i);
				getHibernateTemplate().delete(notification);
			}
		}

		// Now we should be allowed to delete this registration
		getHibernateTemplate().delete(getRegistration(id));
	}

	/**
	 * @see no.unified.soak.dao.RegistrationDAO#getNumberOfAttendants(java.lang.Boolean,
	 *      no.unified.soak.model.Course, Boolean)
	 */
	public Integer getNumberOfAttendants(Boolean localOnly, Course course,
			Boolean reservedOnly) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Registration.class);

		// Find number of registrations totally on the course
		criteria.add(Restrictions.eq("courseid", course.getId()));

		if (reservedOnly != null) {
			criteria.add(Restrictions.eq("reserved", reservedOnly));
		}
		if (localOnly.booleanValue()) {
			criteria.add(Restrictions.eq("organizationid", course
					.getOrganizationid()));
		}

		criteria.setProjection(Projections.rowCount());

		List queryResult = getHibernateTemplate().findByCriteria(criteria);
		Integer result = (Integer) queryResult.get(0);

		return result;
	}

	/**
	 * @see no.unified.soak.dao.RegistrationDAO#getAvailability(java.lang.Boolean,
	 *      Course)
	 */
	public Integer getAvailability(Boolean localAttendant, Course course) {
		Integer result = null;

		// Find number of registrations totally on the course
		Integer allAttendants = getNumberOfAttendants(new Boolean(false),
				course, true);

		// If this is a local attendant, we simply calculate the result
		if (localAttendant.booleanValue()) {
			if ((course.getMaxAttendants().intValue() - allAttendants
					.intValue()) > 0) {
				result = new Integer(course.getMaxAttendants().intValue()
						- allAttendants.intValue());
			} else {
				result = new Integer(0);
			}
		} else {
			// If it is not a local attendant, we need to see about their quota
			Integer localAttendants = getNumberOfAttendants(new Boolean(true),
					course, true);

			// Find number of seats left reserved for the locals
			int seatsLocalAvailable = course.getReservedInternal().intValue()
					- localAttendants.intValue();

			if ((course.getMaxAttendants().intValue()
					- allAttendants.intValue() - seatsLocalAvailable) > 0) {
				result = new Integer(course.getMaxAttendants().intValue()
						- seatsLocalAvailable);
			} else {
				result = new Integer(0);
			}
		}

		return result;
	}

    public List getSpecificRegistrations(Long courseId, Long organizationId,
			Long serviceAreaId, Boolean reserved, Boolean invoiced, 
			Boolean attended, Collection limitToCourses, String[] orderBy) {
		// The default setup - returns everything
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Registration.class);

		// Start adding restrictions based on the input parameteres

		// Course (the applicant has registered to)
		if ((courseId != null) && (courseId.intValue() != 0)) {
			criteria.add(Restrictions.eq("courseid", courseId));
		}

		// Organization (of the applicant)
		if ((organizationId != null) && (organizationId.intValue() != 0)) {
			criteria.add(Restrictions.eq("organizationid", organizationId));
		}

		// Service area (of the applicant)
		if ((serviceAreaId != null) && (serviceAreaId.intValue() != 0)) {
			criteria.add(Restrictions.eq("serviceAreaid", serviceAreaId));
		}

		// Applicant has reservation or is on waiting list
		if (reserved != null) {
			criteria.add(Restrictions.eq("reserved", reserved));
		}

		// Is an invoice sent
		if (invoiced != null) {
			criteria.add(Restrictions.eq("invoiced", invoiced));
		}
		
		// If the course is attended
	    if (attended != null) {
	        criteria.add(Restrictions.eq("attended", attended));
	    }

        if (limitToCourses != null) {
        	if (limitToCourses.isEmpty() == false) {
        		criteria.add(Restrictions.in("courseid", limitToCourses));
        	} else {
        		criteria.add(Restrictions.eq("courseid", new Long(0)));
        	}
        }

        if(orderBy != null) {
        	for(int i=0; i<orderBy.length; i++) {
    			criteria.addOrder(Order.asc(orderBy[i]));
        	}
        }
        
        List result = getHibernateTemplate().findByCriteria(criteria);

		return result;
	}

	/**
	 * @see no.unified.soak.dao.RegistrationDAO#getWaitingListRegistrations(java.util.List)
	 */
	public List<Registration> getWaitingListRegistrations(List courseIds) {
		if ((courseIds != null) && (courseIds.size() > 0)) {
			DetachedCriteria criteria = DetachedCriteria
					.forClass(Registration.class);
			criteria.add(Restrictions.in("courseid", courseIds));
			criteria.add(Restrictions.eq("reserved", new Boolean(false)));
			criteria.addOrder(Order.asc("courseid"));
			criteria.addOrder(Order.asc("registered"));

			return getHibernateTemplate().findByCriteria(criteria);
		} else {
			return null;
		}
	}

	/**
	 * @see no.unified.soak.dao.RegistrationDAO#getNumberOfOccupiedSeats(no.unified.soak.model.Course,
	 *      java.lang.Boolean)
	 */
	public Integer getNumberOfOccupiedSeats(Course course, Boolean localOnly) {
		Integer result = null;

		if (course != null) {
			DetachedCriteria criteria = DetachedCriteria
					.forClass(Registration.class);
			criteria.add(Restrictions.eq("courseid", course.getId()));
			criteria.add(Restrictions.eq("reserved", new Boolean(true)));

			// Are we only to include "locals" in the search
			if (localOnly.booleanValue()) {
				criteria.add(Restrictions.eq("organizationid", course
						.getOrganizationid()));
			}

			criteria.setProjection(Projections.rowCount());

			List queryResult = getHibernateTemplate().findByCriteria(criteria);
			result = (Integer) queryResult.get(0);
		}

		return result;
	}

	public List<Registration> getCourseRegistrations(Long courseId) {
		List<Registration> result = new ArrayList<Registration>();
		if (courseId != null) {
			DetachedCriteria criteria = DetachedCriteria
			.forClass(Registration.class);
			criteria.add(Restrictions.eq("courseid", courseId));
			result = getHibernateTemplate().findByCriteria(criteria);
		}
		return result;
	}

    public List<Registration> getUserRegistrations(String username) {
        List<Registration> result = new ArrayList<Registration>();
        if(username != null){
            DetachedCriteria criteria = DetachedCriteria.forClass(Registration.class);
            criteria.add(Restrictions.eq("username",username));
            result = getHibernateTemplate().findByCriteria(criteria);
        }
        return result;
    }

	public List <Registration> getUserRegistationsForCourse(String email, String firstname, String lastname, Long courseId) {
		List<Registration> result = new ArrayList<Registration>();
        
		DetachedCriteria criteria = DetachedCriteria.forClass(Registration.class);
        criteria.add(Restrictions.eq("email",email));
        criteria.add(Restrictions.eq("firstName",firstname));
        criteria.add(Restrictions.eq("lastName",lastname));
        criteria.add(Restrictions.eq("courseid",courseId));
        
        result = getHibernateTemplate().findByCriteria(criteria);
		
 		return result;
	}

}

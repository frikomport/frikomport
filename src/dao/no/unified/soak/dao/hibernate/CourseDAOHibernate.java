/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created Dec 20. 2005
 */
package no.unified.soak.dao.hibernate;

import java.util.Date;
import java.util.List;

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.User;
import no.unified.soak.model.Person;
import no.unified.soak.util.CourseStatus;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;


/**
 * Implementation of the CourseDAO
 *
 * @author hrj
 */
public class CourseDAOHibernate extends BaseDAOHibernate implements CourseDAO {
    
    /**
     * @see no.unified.soak.dao.CourseDAO#getCourses(no.unified.soak.model.Course)
     */
    public List getCourses(final Course course) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        criteria.addOrder(Order.asc("startTime"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * @see no.unified.soak.dao.CourseDAO#getCourse(Long id)
     */
    public Course getCourse(final Long id) {
        Course course = (Course) getHibernateTemplate().get(Course.class, id);

        if (course == null) {
            log.warn("uh oh, course with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Course.class, id);
        }

        return course;
    }

    /**
     * @see no.unified.soak.dao.CourseDAO#saveCourse(Course course)
     */
    public void saveCourse(final Course course) {
        getHibernateTemplate().saveOrUpdate(course);
    }

    /**
     * @see no.unified.soak.dao.CourseDAO#removeCourse(Long id)
     */
    public void removeCourse(final Long id) {
        getHibernateTemplate().delete(getCourse(id));
    }

    /**
     * @see no.unified.soak.dao.CourseDAO#searchCourses(no.unified.soak.model.Course,
     *      boolean)
     */
    public List searchCourses(Course course, Date startDate, Date stopDate) {
        // Default search is "find all"
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);

        // Check for parameteres (in other words - look for restrictions)
        if (course != null) {
            if ((course.getOrganizationid() != null) &&
                    (course.getOrganizationid().longValue() != 0)) {
                criteria.add(Restrictions.eq("organizationid",
                        course.getOrganizationid()));
            }

            if ((course.getServiceAreaid() != null) &&
                    (course.getServiceAreaid().longValue() != 0)) {
                criteria.add(Restrictions.eq("serviceAreaid",
                        course.getServiceAreaid()));
            }

            if ((course.getName() != null) && (course.getName().length() > 0)) {
                criteria.add(Restrictions.like("name",
                        "%" + course.getName() + "%").ignoreCase());
            }
            if ((course.getType() != null) && (course.getType().length() > 0)) {
                criteria.add(Restrictions.like("type",
                        "%" + course.getType() + "%").ignoreCase());
            }
            
            criteria.add(Restrictions.ge("status",course.getStatus()));
        }

        if (startDate != null) {
            criteria.add(Restrictions.gt("stopTime", startDate));
        }

        if (stopDate != null) {
            criteria.add(Restrictions.lt("stopTime", stopDate));
        }

        criteria.addOrder(Order.asc("startTime"));

        // Perform search
        List courses = getHibernateTemplate().findByCriteria(criteria);

        return courses;
    }

    /**
	 * @see no.unified.soak.dao.CourseDAO#getWaitingListCourses()
	 */
    public List<Course> getWaitingListCourses() {
        Date now = new Date();

        // Default search is "find all"
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        // Only those after the registration limit
        criteria.add(Restrictions.lt("registerBy", now));
        // Only those before the course has started
        criteria.add(Restrictions.gt("freezeAttendance", now));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
	 * @see no.unified.soak.dao.CourseDAO#getUnpublished()
	 */
    public List<Course> getUnpublished(User user){
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        criteria.add(Restrictions.eq("status",new Integer(CourseStatus.COURSE_CREATED)));
        if(user != null){
            criteria.add(Restrictions.eq("responsible",user));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public List<Course> findByInstructor(Person person) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        criteria.add(Restrictions.eq("instructorid",person.getId()));
        return getHibernateTemplate().findByCriteria(criteria);    }
}

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
     * @see no.unified.soak.dao.CourseDAO#getAllCourses()
     */
    public List getAllCourses() {
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
            log.warn("Course with id '" + id + "' not found.");
            throw new ObjectRetrievalFailureException(Course.class, id);
        }
//        getHibernateTemplate().initialize(course);
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
     * @see no.unified.soak.dao.CourseDAO#searchCourses(no.unified.soak.model.Course, Date, Date)
     */
    public List<Course> searchCourses(Course course, Date startDate, Date stopDate) {
        // Default search is "find all"
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);

        // Check for parameteres (in other words - look for restrictions)
        if (course != null) {
            if ((course.getOrganizationid() != null) &&
                    (course.getOrganizationid().longValue() != 0)) {
                criteria.add(Restrictions.eq("organizationid",
                        course.getOrganizationid()));
            }

            if ((course.getOrganization2id() != null) &&
                    (course.getOrganization2id().longValue() != 0)) {
                criteria.add(Restrictions.eq("organization2id",
                        course.getOrganization2id()));
            }

            if ((course.getServiceAreaid() != null) &&
                    (course.getServiceAreaid().longValue() != 0)) {
                criteria.add(Restrictions.eq("serviceAreaid",
                        course.getServiceAreaid()));
            }
            
            if ((course.getLocation() != null) &&
                    (course.getLocation().getId() != 0)) {
                criteria.add(Restrictions.eq("locationid",
                        course.getLocation().getId()));
            } else if (course.getLocationid() != null) {
                criteria.add(Restrictions.eq("locationid", course.getLocationid()));
            }

            if((course.getCategoryid() != null) && (course.getCategoryid().longValue() != 0)){
                criteria.add(Restrictions.eq("categoryid",course.getCategoryid()));
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

        return getHibernateTemplate().findByCriteria(criteria);
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
        criteria.add(Restrictions.gt("startTime", now));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
	 * @see no.unified.soak.dao.CourseDAO#getUnpublished(no.unified.soak.model.Course)
     * @param course
	 */
    public List<Course> getUnpublished(Course course){
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        criteria.add(Restrictions.eq("status",new Integer(CourseStatus.COURSE_CREATED)));
        if(course.getResponsible() != null){
            criteria.add(Restrictions.eq("responsible", course.getResponsible()));
        }
        if(course.getOrganizationid() != null){
            criteria.add(Restrictions.eq("organizationid", course.getOrganizationid()));
        }

        if(course.getOrganization2id() != null){
            criteria.add(Restrictions.eq("organization2id", course.getOrganization2id()));
        }

        criteria.addOrder(Order.asc("startTime"));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public List<Course> findByInstructor(Person person) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        criteria.add(Restrictions.eq("instructorid",person.getId()));
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
    public List<Course> getCoursesWhereRegisterByExpired(long millis){
        DetachedCriteria criteria = DetachedCriteria.forClass(Course.class);
        criteria.add(Restrictions.eq("status",new Integer(CourseStatus.COURSE_PUBLISHED)));
        Date to = new Date();
        Date from = new Date(to.getTime() - millis); // low is now-millis
        criteria.add(Restrictions.between("registerBy", from, to));
        return getHibernateTemplate().findByCriteria(criteria);
    }
    
}

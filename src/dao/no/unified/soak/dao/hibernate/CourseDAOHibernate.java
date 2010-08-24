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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Person;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.CourseStatus;

import org.apache.commons.lang.StringUtils;
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
     * @see no.unified.soak.dao.CourseDAO#searchCourses(no.unified.soak.model.Course, Date, Date, Integer[])
     */
    public List<Course> searchCourses(Course course, Date startDate, Date stopDate, Integer[] status) {
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

            	List family = new ArrayList<Long>();
            	family.add(course.getOrganization2id());
            	
                DetachedCriteria subCriteria = DetachedCriteria.forClass(Organization.class);
            	subCriteria.add(Restrictions.eq("parentid", course.getOrganization2id()));
            	subCriteria.add(Restrictions.eq("type", Type.AREA.getTypeDBValue()));
            	List childOrgs = getHibernateTemplate().findByCriteria(subCriteria);
            	if(!childOrgs.isEmpty()){
            		Iterator<Organization> it = childOrgs.iterator();
            		while(it.hasNext()){
            			Organization o = it.next();
            			family.add(o.getId());
            		}
            	}
            	criteria.add(Restrictions.in("organization2id", family));
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
            
            if(status != null && status.length > 0){ // gir mulighet for å hente flere typer samtidig
                criteria.add(Restrictions.in("status", status));
            }
            else if(course.getStatus() != null){
            	criteria.add(Restrictions.eq("status", course.getStatus()));
            }
            else {
            	// alle statuser..
            }
        }

        if (startDate != null) {
            criteria.add(Restrictions.gt("stopTime", startDate));
        }

        if (stopDate != null) {
            criteria.add(Restrictions.lt("startTime", stopDate));
        }

		String sortorderCSVString = ApplicationResourcesUtil.getText("courseList.order");
		String[] sortorderArray = StringUtils.split(sortorderCSVString, ",");
		for (String fieldName : sortorderArray) {
			criteria.addOrder(Order.asc(fieldName));
		}

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
        	List family = new ArrayList<Long>();
        	family.add(course.getOrganization2id());
        	
            DetachedCriteria subCriteria = DetachedCriteria.forClass(Organization.class);
        	subCriteria.add(Restrictions.eq("parentid", course.getOrganization2id()));
        	subCriteria.add(Restrictions.eq("type", Type.AREA.getTypeDBValue()));
        	List childOrgs = getHibernateTemplate().findByCriteria(subCriteria);
        	if(!childOrgs.isEmpty()){
        		Iterator<Organization> it = childOrgs.iterator();
        		while(it.hasNext()){
        			Organization o = it.next();
        			family.add(o.getId());
        		}
        	}
        	criteria.add(Restrictions.in("organization2id", family));
        }

        if(course.getLocationid() != null){
            criteria.add(Restrictions.eq("locationid", course.getLocationid()));
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

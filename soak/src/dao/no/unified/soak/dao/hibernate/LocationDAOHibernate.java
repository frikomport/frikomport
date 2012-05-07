/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * created 14. dec 2005
 */
package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.LocationDAO;
import no.unified.soak.model.Location;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Organization.Type;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Implementation of the LocationDAO
 *
 * @author hrj
 */
public class LocationDAOHibernate extends BaseDAOHibernate
    implements LocationDAO {
    /**
     * @see no.unified.soak.dao.LocationDAO#getLocations(no.unified.soak.model.Location)
     */
    public List getLocations(final Location location, final Boolean includeDisabled) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Location.class);

        // If the includeDisabled is not true, we only return enabled locations
        if (!includeDisabled.booleanValue()) {
            criteria.add(Restrictions.eq("selectable", new Boolean("true")));
        }

        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * @see no.unified.soak.dao.LocationDAO#getLocation(Long id)
     */
    public Location getLocation(final Long id) {
        Location location = (Location) getHibernateTemplate()
                                           .get(Location.class, id);

        if (location == null) {
            log.warn("uh oh, location with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Location.class, id);
        }

        return location;
    }

    /**
     * @see no.unified.soak.dao.LocationDAO#saveLocation(Location location)
     */
    public void saveLocation(final Location location) {
        getHibernateTemplate().saveOrUpdate(location);
    }

    /**
     * @see no.unified.soak.dao.LocationDAO#removeLocation(Long id)
     */
    public void removeLocation(final Long id) {
        getHibernateTemplate().delete(getLocation(id));
    }

    /**
     * @see no.unified.soak.dao.LocationDAO#searchLocations(no.unified.soak.model.Location)
     */
    public List searchLocations(Location location) {
        // Default search is "find all"
        DetachedCriteria criteria = DetachedCriteria.forClass(Location.class);

        // Check for parameteres (in other words - look for restrictions)
        if (location != null) {
            if (location.getOrganizationid() != null && location.getOrganizationid().longValue() != 0) {
                criteria.add(Restrictions.eq("organizationid", location.getOrganizationid()));
            }

            if (location.getOrganization2id() != null && location.getOrganization2id().longValue() != 0) {
            	List family = new ArrayList<Long>();
            	family.add(location.getOrganization2id());
            	
                DetachedCriteria subCriteria = DetachedCriteria.forClass(Organization.class);
            	subCriteria.add(Restrictions.eq("parentid", location.getOrganization2id()));
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

            if (location.getSelectable() != null) {
                criteria.add(Restrictions.eq("selectable", location.getSelectable()));
            }
        }

        criteria.addOrder(Order.asc("name"));

        // Perform serach
        return getHibernateTemplate().findByCriteria(criteria);
    }
}

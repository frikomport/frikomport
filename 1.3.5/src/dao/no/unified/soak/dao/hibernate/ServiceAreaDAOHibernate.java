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

import no.unified.soak.dao.ServiceAreaDAO;
import no.unified.soak.model.ServiceArea;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Implementation of the ServiceAreaDAO
 *
 * @author hrj
 */
public class ServiceAreaDAOHibernate extends BaseDAOHibernate
    implements ServiceAreaDAO {
    /**
     * @see no.unified.soak.dao.ServiceAreaDAO#getServiceAreas(no.unified.soak.model.ServiceArea)
     */
    public List getServiceAreas(final Boolean includeDisabled) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ServiceArea.class);

        // If the includeDisabled is not true, we only return enabled
        // organizations
        if (!includeDisabled.booleanValue()) {
            criteria.add(Restrictions.eq("selectable", new Boolean("true")));
        }

        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * @see no.unified.soak.dao.ServiceAreaDAO#getServiceArea(Long id)
     */
    public ServiceArea getServiceArea(final Long id) {
        ServiceArea serviceArea = (ServiceArea) getHibernateTemplate()
                                                    .get(ServiceArea.class, id);

        if (serviceArea == null) {
            log.warn("uh oh, serviceArea with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(ServiceArea.class, id);
        }

        return serviceArea;
    }

    /**
     * @see no.unified.soak.dao.ServiceAreaDAO#saveServiceArea(ServiceArea
     *      serviceArea)
     */
    public void saveServiceArea(final ServiceArea serviceArea) {
        getHibernateTemplate().saveOrUpdate(serviceArea);
    }

    /**
     * @see no.unified.soak.dao.ServiceAreaDAO#removeServiceArea(Long id)
     */
    public void removeServiceArea(final Long id) {
        getHibernateTemplate().delete(getServiceArea(id));
    }
}

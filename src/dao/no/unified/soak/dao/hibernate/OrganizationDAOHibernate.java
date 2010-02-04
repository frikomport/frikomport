/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 09.des.2005
 */
package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.OrganizationDAO;
import no.unified.soak.model.Organization;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Implementations of the OrganizationDAO
 *
 * @author hrj
 */
public class OrganizationDAOHibernate extends BaseDAOHibernate
    implements OrganizationDAO {
    /**
     * @see no.unified.soak.dao.OrganizationDAO#getOrganization(java.lang.Long)
     */
    public Organization getOrganization(Long organizationId) {
        Organization organization = (Organization) getHibernateTemplate()
                                                             .get(Organization.class,
                organizationId);

        if (organization == null) {
            throw new ObjectRetrievalFailureException(Organization.class,
                organizationId);
        }

        return organization;
    }

    /**
     * @see no.unified.soak.dao.OrganizationDAO#saveOrganization(no.unified.soak.model.Organization)
     */
    public void saveOrganization(Organization organization) {
        getHibernateTemplate().saveOrUpdate(organization);
    }

    /**
     * @see no.unified.soak.dao.OrganizationDAO#removeOrganization(java.lang.Long)
     */
    public void removeOrganization(Long id) {
        getHibernateTemplate().delete(getOrganization(id));
    }

    /**
     * @see no.unified.soak.dao.OrganizationDAO#getAll(no.unified.soak.model.Organization)
     */
    public List getAll(Boolean includeDisabled) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Organization.class);

        // If the includeDisabled is not true, we only return enabled
        // organizations
        if (!includeDisabled.booleanValue()) {
            criteria.add(Restrictions.eq("selectable", new Boolean("true")));
        }

        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }
}

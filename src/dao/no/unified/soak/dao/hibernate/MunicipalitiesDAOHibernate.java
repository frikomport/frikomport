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

import no.unified.soak.dao.MunicipalitiesDAO;
import no.unified.soak.model.Municipalities;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Implementations of the MunicipalitiesDAO
 *
 * @author hrj
 */
public class MunicipalitiesDAOHibernate extends BaseDAOHibernate
    implements MunicipalitiesDAO {
    /**
     * @see no.unified.soak.dao.MunicipalitiesDAO#getMunicipalities(java.lang.Long)
     */
    public Municipalities getMunicipalities(Long municipalitiesId) {
        Municipalities municipalities = (Municipalities) getHibernateTemplate()
                                                             .get(Municipalities.class,
                municipalitiesId);

        if (municipalities == null) {
            throw new ObjectRetrievalFailureException(Municipalities.class,
                municipalitiesId);
        }

        return municipalities;
    }

    /**
     * @see no.unified.soak.dao.MunicipalitiesDAO#saveMunicipalities(no.unified.soak.model.Municipalities)
     */
    public void saveMunicipalities(Municipalities municipalities) {
        getHibernateTemplate().saveOrUpdate(municipalities);
    }

    /**
     * @see no.unified.soak.dao.MunicipalitiesDAO#removeMunicipalities(java.lang.Long)
     */
    public void removeMunicipalities(Long id) {
        getHibernateTemplate().delete(getMunicipalities(id));
    }

    /**
     * @see no.unified.soak.dao.MunicipalitiesDAO#getAll(no.unified.soak.model.Municipalities)
     */
    public List getAll(Boolean includeDisabled) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Municipalities.class);

        // If the includeDisabled is not true, we only return enabled
        // municipalities
        if (!includeDisabled.booleanValue()) {
            criteria.add(Restrictions.eq("selectable", new Boolean("true")));
        }

        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }
}

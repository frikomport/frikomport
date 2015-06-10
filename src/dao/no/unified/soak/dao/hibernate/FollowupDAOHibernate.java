/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao.hibernate;

import java.util.List;
import no.unified.soak.dao.FollowupDAO;
import no.unified.soak.model.Followup;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;


/**
 * Implementation of the FollowupDAO
 */
public class FollowupDAOHibernate extends BaseDAOHibernate implements FollowupDAO {


	/**
     * Retrieves all Foolowups that uses the specified location.
     */
    public List<Followup> getFollowupsWhereLocation(Long locationid) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(Followup.class);
        criteria.add(Restrictions.eq("locationid", locationid));
        return getHibernateTemplate().findByCriteria(criteria);
    }

}
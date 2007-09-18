/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.LookupDAO;

import java.util.List;


/**
 * Hibernate implementation of LookupDAO.
 *
 * <p><a href="LookupDAOHibernate.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupDAOHibernate extends BaseDAOHibernate implements LookupDAO {
    /**
     * @see no.unified.soak.dao.LookupDAO#getRoles()
     */
    public List getRoles() {
        if (log.isDebugEnabled()) {
            log.debug("retrieving all role names...");
        }

        return getHibernateTemplate().find("from Role order by name");
    }
}

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.RoleDAO;
import no.unified.soak.model.Role;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * <p>
 * <a href="RoleDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
public class RoleDAOHibernate extends BaseDAOHibernate implements RoleDAO {
    private Log log = LogFactory.getLog(RoleDAOHibernate.class);

    public List getRoles(Role role) {
        return getHibernateTemplate().find("from Role");
    }

    public Role getRole(String rolename) {
        return (Role) getHibernateTemplate().get(Role.class, rolename);

        /*
        try {
            Hibernate.initialize(role);
        } catch (HibernateException e) {
            log.error(e);
        }
        return role;
        */
    }

    public void saveRole(Role role) {
        getHibernateTemplate().saveOrUpdate(role);
    }

    public void removeRole(String rolename) {
        Object role = getHibernateTemplate().load(Role.class, rolename);
        getHibernateTemplate().delete(role);
    }
}

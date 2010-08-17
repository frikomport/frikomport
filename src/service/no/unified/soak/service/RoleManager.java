/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * <p><a href="RoleManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
package no.unified.soak.service;

import no.unified.soak.dao.RoleDAO;
import no.unified.soak.model.Role;

import java.util.List;


public interface RoleManager {
    public void setRoleDAO(RoleDAO dao);

    public List getRoles(Role role);

    public Role getRole(String rolename);

    public Role findRole(String description);

    public void saveRole(Role role);

    public void removeRole(String rolename);
}

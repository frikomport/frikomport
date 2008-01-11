/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao;

import java.util.List;


/**
 * This class tests the current LookupDAO implementation class
 * @author mraible
 */
public class LookupDAOTest extends BaseDAOTestCase {
    private LookupDAO dao;

    public void setLookupDAO(LookupDAO dao) {
        this.dao = dao;
    }

    public void testGetRoles() {
        List roles = dao.getRoles();

        if (log.isDebugEnabled()) {
            log.debug(roles);
        }

        assertTrue(roles.size() > 0);
    }
}

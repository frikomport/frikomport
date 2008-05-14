/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import no.unified.soak.dao.LookupDAO;

import java.util.List;


/**
 * Business Service Interface to talk to persistence layer and
 * retrieve values for drop-down choice lists.
 *
 * <p>
 * <a href="LookupManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface LookupManager {
    public void setLookupDAO(LookupDAO dao);

    /**
     * Retrieves all possible roles from persistence layer
     * @return List
     */
    public List getAllRoles();
}

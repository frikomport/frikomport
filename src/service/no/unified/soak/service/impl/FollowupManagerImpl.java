/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 20. Dec 2005
 */
package no.unified.soak.service.impl;

import java.util.List;
import no.unified.soak.dao.FollowupDAO;
import no.unified.soak.model.Followup;
import no.unified.soak.service.FollowupManager;
import org.hibernate.StaleObjectStateException;


public class FollowupManagerImpl extends BaseManager implements FollowupManager {

    private FollowupDAO followupDAO;

    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setFollowupDAO(FollowupDAO followupDAO) {
    	this.followupDAO = followupDAO;
    }

    /**
     * Retrieves all Foolowups that uses the specified location.
     */
    public List<Followup> getFollowupsWhereLocation(Long locationid) {
    	try {
            return followupDAO.getFollowupsWhereLocation(locationid);
        }
        catch(StaleObjectStateException e){
            throw e;
        }
    }
}
/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import java.util.List;
import no.unified.soak.dao.FollowupDAO;
import no.unified.soak.model.Followup;


public interface FollowupManager extends Manager {
	/**
     * Setter for DAO, convenient for unit testing
     */
    public void setFollowupDAO(FollowupDAO followupDAO);

    /**
     * Retrieves all Foolowups that uses the specified location.
     */
    public List<Followup> getFollowupsWhereLocation(Long locationid);
}
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
package no.unified.soak.dao;

import java.util.List;
import no.unified.soak.model.Followup;


public interface FollowupDAO extends DAO {

	/**
     * Retrieves all Foolowups that uses the specified location.
     */
    public List<Followup> getFollowupsWhereLocation(final Long locationid);
}
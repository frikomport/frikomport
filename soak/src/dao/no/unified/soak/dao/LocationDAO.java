/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * created 14. dec 2005
 */
package no.unified.soak.dao;

import no.unified.soak.model.Location;

import java.util.List;


/**
 * User Data Access Object (DAO) interface.
 *
 * @author hrj
 */
public interface LocationDAO extends DAO {
    /**
     * Retrieves all of the locations
         * @param includeDisabled
         *            set to 'true' to include the disabled ones
     */
    public List getLocations(Location location, Boolean includeDisabled);

    /**
     * Gets location's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the location's id
     * @return location populated location object
     */
    public Location getLocation(final Long id);

    /**
     * Saves a location's information
     * @param location the object to be saved
     */
    public void saveLocation(Location location);

    /**
    * Removes a location from the database by id
    * @param id the location's id
    */
    public void removeLocation(final Long id);

    /**
     * Searches for all locations that applies to the conditions given
     * @param location contains the parameteres used for searching
     * @return list of all locations that applies to the given criteria
     */
    public List searchLocations(Location location);
}

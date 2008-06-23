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
package no.unified.soak.service;

import no.unified.soak.dao.LocationDAO;
import no.unified.soak.model.Location;

import java.util.List;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface LocationManager extends Manager {
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setLocationDAO(LocationDAO locationDAO);

    /**
     * Retrieves all of the locations
     */
    public List getLocations(Location location, Boolean includeDisabled);

    /**
     * Gets location's information based on id.
     * @param id the location's id
     * @return location populated location object
     */
    public Location getLocation(final String id);

    /**
     * Saves a location's information
     * @param location the object to be saved
     */
    public void saveLocation(Location location);

    /**
     * Removes a location from the database by id
     * @param id the location's id
     */
    public void removeLocation(final String id);

    /**
     * Searches for all locations that apples to the conditions given
     * @param location contains the parameteres used for searching
     * @return list of all locations that applies to the given criteria
     */
    public List searchLocations(Location location);
}

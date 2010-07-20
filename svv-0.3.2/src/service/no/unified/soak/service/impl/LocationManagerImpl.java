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
package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.List;

import no.unified.soak.dao.LocationDAO;
import no.unified.soak.model.Location;
import no.unified.soak.service.LocationManager;


/**
 * Implementation of LocationManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class LocationManagerImpl extends BaseManager implements LocationManager {
    private LocationDAO dao;

    /**
     * Set the DAO for communication with the data layer.
     *
     * @param dao
     */
    public void setLocationDAO(LocationDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.LocationManager#getLocations(no.unified.soak.model.Location)
     */
    public List getLocations(final Location location,
        final Boolean includeDisabled) {
        return dao.getLocations(location, includeDisabled);
    }

    /**
     * @see no.unified.soak.service.LocationManager#getLocation(String id)
     */
    public Location getLocation(final String id) {
        return dao.getLocation(new Long(id));
    }

    /**
     * @see no.unified.soak.service.LocationManager#saveLocation(Location
     *      location)
     */
    public void saveLocation(Location location) {
        dao.saveLocation(location);
    }

    /**
     * @see no.unified.soak.service.LocationManager#removeLocation(String id)
     */
    public void removeLocation(final String id) {
        dao.removeLocation(new Long(id));
    }

    /**
     * @see no.unified.soak.service.LocationManager#searchLocations(no.unified.soak.model.Location)
     */
    public List searchLocations(Location location) {
        return dao.searchLocations(location);
    }
    
    public List getAllIncludingDummy(Location location, Boolean includeDisabled, String dummy) {
        List locations = new ArrayList();
        Location locationDummy = new Location();
        locationDummy.setId(null);
        locationDummy.setName(dummy);
        locationDummy.setMaxAttendants(0);
        locations.add(locationDummy);
        locations.addAll(getLocations(location, includeDisabled));
        return locations;
    }
    
	/**
	 * Evict entity for hibernate sessions. This avoids automatic saving
	 * (flush) of the entity.
	 * 
	 * @param entity
	 */
	public void evict(Object entity) {
		dao.evict(entity);
	}

}

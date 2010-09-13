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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import no.unified.soak.dao.LocationDAO;
import no.unified.soak.dao.PostalCodeDistanceDAO;
import no.unified.soak.model.Location;
import no.unified.soak.model.PostalCodeCoordinate;
import no.unified.soak.model.PostalCodeDistance;
import no.unified.soak.service.LocationManager;
import no.unified.soak.util.GeoMathUtil;

import org.apache.commons.lang.StringUtils;

/**
 * Implementation of LocationManager interface to talk to the persistence layer.
 * 
 * @author hrj
 */
public class LocationManagerImpl extends BaseManager implements LocationManager {
	private LocationDAO dao;

	private PostalCodeDistanceDAO postalCodeDistanceDAO;

	/**
	 * @param postalCodeDistanceDAO
	 *            the postalCodeDistanceDAO to set
	 */
	public void setPostalCodeDistanceDAO(PostalCodeDistanceDAO postalCodeDistanceDAO) {
		this.postalCodeDistanceDAO = postalCodeDistanceDAO;
	}

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
	public List getLocations(final Location location, final Boolean includeDisabled) {
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
	 * Evict entity for hibernate sessions. This avoids automatic saving (flush)
	 * of the entity.
	 * 
	 * @param entity
	 */
	public void evict(Object entity) {
		dao.evict(entity);
	}

	public void createPostalCodeLocationDistancesInDatabase(List<PostalCodeCoordinate> pcCoordinates) {
		List<Location> locations = dao.getObjects(Location.class);
		List<Location> locationsCleaned = avoidLocationsWithoutPostalCode(locations);
		int iOuter = 1;
		log.info("Building content for table PostalCodeLocationDistance.");
		System.out.print("Teller antall postnummer opp til "+pcCoordinates.size()+": ");

		for (PostalCodeCoordinate pc1 : pcCoordinates) {

			String thePostalCode = pc1.getPostalCode();
			List<PostalCodeDistance> postalCodeDistanceList = postalCodeDistanceDAO.findDistancesByPostalCode(thePostalCode);
			Map<String, Integer> postalCodeDistanceMap = new HashMap<String, Integer>(4600);
			for (PostalCodeDistance postalCodeDistance : postalCodeDistanceList) {
				postalCodeDistanceMap.put(getOtherPostalCode(postalCodeDistance, thePostalCode), postalCodeDistance
						.getDistance());
			}

			for (Location location : locationsCleaned) {
				String locationPostalCode = location.getPostalCode();
				Long locationId = location.getId();

				Integer locationPostalCodeDistance = 0;
				if (!locationPostalCode.equals(thePostalCode)) {
					locationPostalCodeDistance = postalCodeDistanceMap.get(locationPostalCode);
				}
				if (locationPostalCodeDistance != null) {
					postalCodeDistanceDAO.savePostalCodeLocationDistance(thePostalCode, locationId, locationPostalCodeDistance);
				} else {
					log.warn("Could not calculate distance from postalcode "+ thePostalCode + " to location postalcode " + locationPostalCode+". Not saving in table PostalCodeLocationDistance.");
				}
			}
			System.out.print((iOuter++) + "-");
		}
		System.out.println();
	}

	private String getOtherPostalCode(PostalCodeDistance postalCodeDistance, String postalCode) {
		if (postalCodeDistance.getPostalCode1().equals(postalCode)) {
			return postalCodeDistance.getPostalCode2();
		} else if (postalCodeDistance.getPostalCode2().equals(postalCode)) {
			return postalCodeDistance.getPostalCode1();
		}
		throw new RuntimeException("Unable to find postalCode " + postalCode + " in object " + postalCodeDistance);
	}

	private List<Location> avoidLocationsWithoutPostalCode(List<Location> locations) {
		List<Location> retLocations = new ArrayList(locations.size());
		for (Location location : locations) {
			if (StringUtils.isBlank(location.getPostalCode())) {
				log.warn("Unable to calculate distances for location without postalCode: " + location);
				continue;
			} else  {
				retLocations.add(location);
			}
		}
		
		return retLocations;
	}

	/**
	 * Assumes list of PostalCodeCoordinate are ordered by
	 * {@link no.unified.soak.model.PostalCodeCoordinate#getPostalCode()} in
	 * ascending order.
	 * 
	 * @param pcCoordinates
	 */
	public void createPostalCodeDistancesInDatabase(List<PostalCodeCoordinate> pcCoordinates) {
		int iOuter = 1;
		log.info("Building content for table PostalCodeDistance.");
		for (Iterator iterator = pcCoordinates.iterator(); iterator.hasNext();) {
			PostalCodeCoordinate pc1 = (PostalCodeCoordinate) iterator.next();

			inner: for (Iterator iterator2 = pcCoordinates.iterator(); iterator2.hasNext();) {
				PostalCodeCoordinate pc2 = (PostalCodeCoordinate) iterator2.next();

				if (pc1.compareTo(pc2) >= 0 || avoidableByHeuristics(pc1, pc2)) {
					// Skip calculating distances both directions or between far
					// away places.
					continue inner;
				}

				PostalCodeDistance pcDistance = new PostalCodeDistance(pc1.getPostalCode(), pc2.getPostalCode());
				pcDistance.setDistance(new Double(1000 * GeoMathUtil.distanceDEG(pc1.getLatitude(), pc1.getLongitude(), pc2
						.getLatitude(), pc2.getLongitude())).intValue());

				postalCodeDistanceDAO.savePostalCodeDistance(pcDistance);
			}
			System.out.print(pc1.getPostalCode() + "(" + (iOuter++) + "/" + pcCoordinates.size()+ ") ");
		}
		System.out.println();
	}

	/**
	 * Decides if two postal codes are geographically distant enough to exclude
	 * them from distance calculation. <br/>
	 * Assumes pCoordinateA.getPostalCode() < pCoordinateB.getPostalCode()
	 * because those cases are not checked.
	 * <p/>
	 * The heuristics is based on this map of postal codes: <a
	 * href="http://epab.posten.no/Norsk/Nedlasting/_files/Postnummerkart.pdf"
	 * >http://epab.posten.no/Norsk/Nedlasting/_files/Postnummerkart.pdf</a>
	 * 
	 * @param pCoordinateA
	 * @param pCoordinateB
	 * @return
	 */
	private static boolean avoidableByHeuristics(PostalCodeCoordinate pCoordinateA, PostalCodeCoordinate pCoordinateB) {
		char[] omradeA = { pCoordinateA.getPostalCode().charAt(0), pCoordinateA.getPostalCode().charAt(1) };
		char[] omradeB = { pCoordinateB.getPostalCode().charAt(0), pCoordinateB.getPostalCode().charAt(1) };

		char b0 = omradeB[0];
		char a0 = omradeA[0];
		switch (a0) {

		case 6:
			if (b0 == '9') {
				return true;
			}
			break;

		case 5:
			if (b0 == '9') {
				return true;
			}
			break;

		case 4:
			if (b0 > '6') {
				return true;
			}
			break;

		case 3:
			if (b0 == '9') {
				return true;
			}
			break;

		case 2:
			if (b0 == '9') {
				return true;
			}
			break;

		case 1:
			if (b0 > '7') {
				return true;
			}
			break;

		case 0:
			if (b0 > '7') {
				return true;
			}
			break;
		}

		return false;
	}
}

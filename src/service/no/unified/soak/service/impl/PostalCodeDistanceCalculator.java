package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;

import no.unified.soak.model.PostalCodeCoordinate;
import no.unified.soak.model.PostalCodeDistance;

/**
 * @author extkla
 * 
 */
public class PostalCodeDistanceCalculator {
	/** Number of radians to a degree */
	static double RAD_PER_DEG = Math.PI / 180.0;

	/** The radius of the Earth in km */
	static double R = 6371.0; // km

	/**
	 * Return distance in km between two points on the Earth's surface. The
	 * coordinates of the points are given as latitude and longitude in radians.
	 * Negative is west, resp. south.
	 */

	public static double distanceRAD(double lat1, double lon1, double lat2, double lon2) {
		return R * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
	}

	/**
	 * Return distance in km between two points on the Earth's surface. The
	 * coordinates of the points are given as latitude and longitude in degrees.
	 * Negative is west, resp. south.
	 */
	public static double distanceDEG(double lat1, double lon1, double lat2, double lon2) {
		return distanceRAD(RAD_PER_DEG * lat1, RAD_PER_DEG * lon1, RAD_PER_DEG * lat2, RAD_PER_DEG * lon2);
	}

	/**
	 * 
	 * This routine calculates the distance between two points (given the
	 * latitude/longitude of those points). It is being used to calculate the
	 * distance between two ZIP Codes or Postal Codes using our ZIPCodeWorld(TM)
	 * and PostalCodeWorld(TM) products.
	 * <p/>
	 * Definitions: South latitudes are negative, east longitudes are positive
	 * <p/>
	 * Passed to function:<br/>
	 * lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)<br/>
	 * lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)<br/>
	 * unit = the unit you desire for results <br/>
	 * where: <br/>
	 *'M' is statute miles (default)<br/>
	 *'K' is kilometers <br/>
	 *'E' is meters <br/>
	 *'N' is nautical miles <br/>
	 * <p/>
	 * United States ZIP Code/ Canadian Postal Code databases with latitude &
	 * longitude are available at http://www.zipcodeworld.com For enquiries,
	 * please contact sales@zipcodeworld.com Official Web site:
	 * http://www.zipcodeworld.com Hexa Software Development Center © All Rights
	 * Reserved 2004
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'E') {
			dist = dist * 1609.344;
		} else if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	/**
	 * This function converts decimal degrees to radians
	 * 
	 * @param deg
	 * @return
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/**
	 * This function converts radians to decimal degrees
	 * 
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static void main(String[] args) {
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'M') + " Miles\n");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'K') + " Kilometers\n");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'N') + " Nautical Miles\n");
	}

	/**
	 * Assumes list of PostalCodeCoordinate are ordered by
	 * {@link no.unified.soak.model.PostalCodeCoordinate#getPostalCode()} in
	 * ascending order.
	 * 
	 * @param pcCoordinates
	 */
	public static void makeDistancesInDatabase(List<PostalCodeCoordinate> pcCoordinates) {
		for (Iterator iterator = pcCoordinates.iterator(); iterator.hasNext();) {
			PostalCodeCoordinate pc1 = (PostalCodeCoordinate) iterator.next();

			inner: for (Iterator iterator2 = pcCoordinates.iterator(); iterator2.hasNext();) {
				PostalCodeCoordinate pc2 = (PostalCodeCoordinate) iterator2.next();

				if (pc1.compareTo(pc2) >= 0 || avoidableByHeuristics(pc1, pc2)) {
					continue inner;
				}

				PostalCodeDistance pcDistance = new PostalCodeDistance(pc1.getPostalCode(), pc2.getPostalCode());
				pcDistance.setDistance(new Double(1000 * distanceDEG(pc1.getLatitude(), pc1.getLongitude(), pc2.getLatitude(), pc2
						.getLongitude())).intValue());

				// TODO: Save pcDistance
			}

		}
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

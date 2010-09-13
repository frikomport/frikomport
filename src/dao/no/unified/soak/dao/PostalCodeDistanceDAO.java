package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.model.PostalCodeDistance;

public interface PostalCodeDistanceDAO extends DAO {

	/**
	 * Insert or update a row in table PostalCodeDistance.
	 * 
	 * @param distance
	 */
	public void savePostalCodeDistance(PostalCodeDistance distance);
	public void insertPostalCodeDistance(PostalCodeDistance distance);
	
	/**
	 * Finds all distances for postalCode from table PostalCodeDistance,
	 * regardless if postalCode is in field postalCode1 or postalCode2.
	 * 
	 * @param postalCode
	 * @return
	 */
	public List<PostalCodeDistance> findDistancesByPostalCode(String postalCode);

	/**
	 * Insert or update a row in table PostalCodeLocationDistance.
	 * 
	 * @param postalCode
	 * @param locationid
	 * @param distance
	 */
	public void savePostalCodeLocationDistance(String postalCode, Long locationid, int distance);
	public void insertPostalCodeLocationDistance(String postalCode, Long locationid, int distance);
	
	public void removePostalCodeLocationDistance(Long locationid);
	
	public void createIndexes();
}

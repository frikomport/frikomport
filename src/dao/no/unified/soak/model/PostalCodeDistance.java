package no.unified.soak.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for holding the distance between two postalCodes.
 * 
 * <p>
 * <a href="PostalCodeDistance.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author extkla
 */
public class PostalCodeDistance extends BaseObject implements Serializable, Comparable<Object> {
	private static final long serialVersionUID = 3862426132173359411L;
	private String postalCode1;
	private String postalCode2;
	private Integer distance;
	
	public PostalCodeDistance() {
	}

	public PostalCodeDistance(String postalCode1, String postalCode2) {
		setPostalcode1(postalCode1);
		setPostalcode2(postalCode2);
	}

	/**
	 * Returns the postalCode1.
	 * 
	 * @return postalCode1 string
	 */
	public String getPostalCode1() {
		return postalCode1;
	}

	/**
	 * @param postalCode1
	 *            the postalCode1 to set
	 */
	public void setPostalcode1(String postalCode1) {
		this.postalCode1 = postalCode1;
	}

	/**
	 * Returns the postalCode2.
	 * 
	 * @return postalCode2 string
	 */
	public String getPostalCode2() {
		return postalCode2;
	}

	/**
	 * @param postalCode2
	 *            the postalCode2 to set
	 */
	public void setPostalcode2(String postalCode2) {
		this.postalCode2 = postalCode2;
	}

	/**
	 * @return the distance in meters
	 */
	public Integer getDistance() {
		return distance;
	}

	/**
	 * Sets the distance in meters
	 * 
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(postalCode1).append(postalCode2).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PostalCodeDistance)) {
			return false;
		}
		PostalCodeDistance distance = (PostalCodeDistance) o;
		return new EqualsBuilder().append(postalCode1, distance.getPostalCode1()).append(getPostalCode2(),
				distance.getPostalCode2()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getPostalCode1()).append(getPostalCode2()).append(getDistance()).toString();
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return 1;
		}
		PostalCodeDistance postalCodeDistance = (PostalCodeDistance) obj;
		return new CompareToBuilder().append(getPostalCode1(), postalCodeDistance.getPostalCode1()).append(getPostalCode2(),
				postalCodeDistance.getPostalCode2()).append(getDistance(), postalCodeDistance.getDistance()).toComparison();
	}
}

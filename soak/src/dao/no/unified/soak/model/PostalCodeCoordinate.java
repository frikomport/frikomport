package no.unified.soak.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PostalCodeCoordinate implements Serializable, Comparable<Object> {
	private static final long serialVersionUID = -5235613381295631246L;
	private String postalCode;
	private double longitude;
	private double latitude;

	public PostalCodeCoordinate(String postalCode, double longitude, double latitude) {
		this.postalCode = postalCode;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public PostalCodeCoordinate() {
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getPostalCode()).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PostalCodeCoordinate)) {
			return false;
		}
		PostalCodeCoordinate postalCodeCoordinate = (PostalCodeCoordinate) obj;
		return new EqualsBuilder().append(getPostalCode(), postalCodeCoordinate.getPostalCode()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getPostalCode()).toHashCode();
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return 1;
		}
		PostalCodeCoordinate poCoordinate = (PostalCodeCoordinate) obj;
		return new CompareToBuilder().append(getPostalCode(), poCoordinate.getPostalCode()).toComparison();
	}

}

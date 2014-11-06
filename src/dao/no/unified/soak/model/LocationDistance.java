package no.unified.soak.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for holding the distance to a location identified by locationid.
 * 
 * @author extkla
 */
public class LocationDistance implements Comparable<Object> {
	private static final long serialVersionUID = 3862426132173359411L;
	private Long locationid;
	private Integer distance;
	
	public LocationDistance() {
	}

	public LocationDistance(Long locationid, Integer distance) {
		this.locationid = locationid;
		this.distance = distance;
	}

	/**
	 * @return the locationid
	 */
	public Long getLocationid() {
		return locationid;
	}

	/**
	 * @param locationid the locationid to set
	 */
	public void setLocationid(Long locationid) {
		this.locationid = locationid;
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
		return new HashCodeBuilder().append(distance).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof LocationDistance)) {
			return false;
		}
		LocationDistance distance = (LocationDistance) o;
		return new EqualsBuilder().append(distance, distance.getDistance()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getLocationid()).append(getDistance()).toString();
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return 1;
		}
		LocationDistance locationDistance = (LocationDistance) obj;
		return new CompareToBuilder().append(getDistance(), locationDistance.getDistance()).toComparison();
	}
}

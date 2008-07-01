/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;


/**
 * This class is used to represent an address.</p>
 *
 * <p><a href="Address.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 */
public class Address extends BaseObject implements Serializable {
    protected String address;
    protected String city;
    protected String province;
    protected String country;
    protected String postalCode;
    
    public Address() {
		address = "";
		city = "";
		province = "";
		country = "";
		postalCode = "";
	}
    
    /**
     * Returns the address.
     * @return String
     *
     * @hibernate.property not-null="false" length="150"
     * @hibernate.column name="address"
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the city.
     * @return String
     *
     * @hibernate.property not-null="false" length="50"
     * @hibernate.column name="city"
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the province.
     * @return String
     *
    
     * @hibernate.property length="100" not-null="false"
     * @hibernate.column name="province" 
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the country.
     * @return String
     *
     * @hibernate.property length="100" 
     * @hibernate.column name="country"
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the postalCode.
     * @return String
     *
     * @hibernate.property not-null="false" length="15"
     * @hibernate.column name="postal_code"
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the address.
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the city.
     * @param city The city to set
     *
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the country.
     * @param country The country to set
     *
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the postalCode.
     * @param postalCode The postalCode to set
     *
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the province.
     * @param province The province to set
     *
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public boolean equals(Object object) {
        if (!(object instanceof Address)) {
            return false;
        }

        Address rhs = (Address) object;

        return new EqualsBuilder().append(this.postalCode, rhs.postalCode)
                                  .append(this.country, rhs.country)
                                  .append(this.address, rhs.address)
                                  .append(this.province, rhs.province)
                                  .append(this.city, rhs.city).isEquals();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public int hashCode() {
        return new HashCodeBuilder(-426830461, 631494429).append(this.postalCode)
                                                         .append(this.country)
                                                         .append(this.address)
                                                         .append(this.province)
                                                         .append(this.city)
                                                         .toHashCode();
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("country",
            this.country).append("address", this.address)
                                                                        .append("province",
            this.province).append("postalCode", this.postalCode)
                                                                        .append("city",
            this.city).toString();
    }
}

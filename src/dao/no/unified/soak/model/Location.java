/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 09.des.2005
 */
package no.unified.soak.model;

import no.unified.soak.validation.MinValue;
import no.unified.soak.validation.Required;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * This class is used to represent a location on which a course can be held
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="location" lazy="false"
 */
public class Location extends BaseObject implements Serializable {
    /**
         * Eclipse generated UID
         */
    private static final long serialVersionUID = -914547049154205712L;
    private Long id;
    private String name;
    private String address;
    private String mailAddress;
    private String contactName;
    private String email;
    private String phone;
    private String detailURL;
    private String mapURL;
    private String owner;
    private Integer maxAttendants;
    private Double feePerDay;
    private String description;
    private Long organizationid;
    private Long organization2id;
    private Organization organization;
    private Organization organization2;
    private Boolean selectable;

    /**
     * @return Returns the selectable.
     * @hibernate.property column="selectable" not-null="true"
     */
    public Boolean getSelectable() {
        return selectable;
    }

    /**
     * @spring.validator type="required"
     * @param selectable
     *            The selectable to set.
     */
    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * @return Returns the address.
     * @hibernate.property column="address" length="50" not-null="true"
     */
    public String getAddress() {
        return address;
    }

    /**
     * @spring.validator type="required"
     * @param address
     *            The address to set.
     */
    @Required
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Returns the mailAdress.
     * @hibernate.property column="mailaddress" length="100" not-null="false"
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * @param mailAdress The mailAdress to set.
     */
    public void setMailAddress(String mailAdress) {
        this.mailAddress = mailAdress;
    }

    /**
     * @return Returns the contactName.
     * @hibernate.property column="contactname" length="50"
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName
     *            The contactName to set.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return Returns the email.
     * @hibernate.property column="email" length="50" not-null="false"
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
      * @return Returns the detailURL.
      * @hibernate.property column="detailurl" length="350"
      */
    public String getDetailURL() {
        return detailURL;
    }

    /**
     * @param detailURL
     *            The detailURL to set.
     */
    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }

    /**
     * @return Returns the mapURL.
     * @hibernate.property column="mapurl" length="350"
     */
    public String getmapURL() {
        return mapURL;
    }

    /**
     * @param mapURL The mapURL to set.
     */
    public void setMapURL(String mapURL) {
        this.mapURL = mapURL;
    }

    /**
     * @return Returns the id.
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the name.
     * @hibernate.property column="name" length="50" not-null="true"
     */
    public String getName() {
        return name;
    }

    /**
     * @spring.validator type="required"
     * @param name
     *            The name to set.
     */
    @Required
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the phone.
     * @hibernate.property column="phone" length="30"
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     *            The phone to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return Returns the owner.
     * @hibernate.property column="owner" length="50" not-null="false"
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner The owner to set.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return Returns the maxAttendants.
     * @hibernate.property column="maxattendants" not-null="false"
     */
    public Integer getMaxAttendants() {
        return maxAttendants;
    }

    /**
     * @param maxAttendants The maxAttendants to set.
     */
    @Required
    @MinValue("0")
    public void setMaxAttendants(Integer maxAttendants) {
        this.maxAttendants = maxAttendants;
    }

    /**
     * @return Returns the feePerDay.
     * @hibernate.property column="feeperday" not-null="false"
     */
    public Double getFeePerDay() {
        return feePerDay;
    }

    /**
     * @param feePerDay The feePerDay to set.
     */
    public void setFeePerDay(Double feePerDay) {
        this.feePerDay = feePerDay;
    }

    /**
     * @return Returns the description.
     * @hibernate.property column="description" length="1000" not-null="false"
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the organization.
     * @hibernate.many-to-one not-null="true" column="organizationid" update="false" cascade="none" insert="false"
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization The organization to set.
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
 
    /**
     * @return Returns the organization.
     * @hibernate.many-to-one not-null="false" column="organization2id" update="false" cascade="none" insert="false"
     */
    public Organization getOrganization2() {
        return organization2;
    }

    public void setOrganization2(Organization organization2) {
        this.organization2 = organization2;
    }

    /**
     * @return Returns the organizationid.
     * @hibernate.property column="organizationid" not-null="true"
     */
    public Long getOrganizationid() {
        return organizationid;
    }

    /**
     * @param organizationid The organizationid to set.
     * @spring.validator type="required"
     */
    @Required
    public void setOrganizationid(Long organizationid) {
    	this.organizationid = organizationid;
    }

    /**
     * @return Returns the organization2id.
     * @hibernate.property column="organization2id" not-null="false"
     */
    public Long getOrganization2id() {
        return organization2id;
    }

    /**
     * @param organization2id The organization2id to set.
     * @spring.validator type="required"
     */
    @Required
    public void setOrganization2id(Long organization2id) {
    	this.organization2id = organization2id;
    }
    


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name)
                                        .append("address", address).toString();
    }

    /**
     * @see java.lang.Object#equals(Object) ()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Location)) {
            return false;
        }

        Location castOther = (Location) other;

        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }
}

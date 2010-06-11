/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 11.des.2005
 */
package no.unified.soak.model;

import no.unified.soak.validation.Email;
import no.unified.soak.validation.Required;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * This class is used to represent a person
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="person" lazy="false" 
 */
public class Person extends BaseObject implements Serializable {
    private static final long serialVersionUID = 8470485480268638081L;
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String mobilePhone;
    private String mailAddress;
    private String detailURL;
    private String description;
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
     * @return Returns the detailURL.
     * @hibernate.property column="detailurl" length="200"
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
     * @return Returns the email.
     * @hibernate.property column="email" length="50" not-null="true"
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            The email to set.
     * @spring.validator type="required"
     * @spring.validator type="email"
     */
    @Required
    @Email
    public void setEmail(String email) {
        this.email = email;
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
     * @return Returns the mailAdress.
     * @hibernate.property column="mailaddress" length="100"
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * @param mailAdress
     *            The mailAdress to set.
     */
    public void setMailAddress(String mailAdress) {
        this.mailAddress = mailAdress;
    }

    /**
     * @return Returns the name.
     * @hibernate.property column="name" length="50" not-null="true"
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     * @spring.validator type="required"
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
     * @param phone The phone to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return Returns the mobilePhone.
     * @hibernate.property column="mobilephone" length="30"
     *
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * @param mobilePhone The mobilePhone to set.
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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
      * @see java.lang.Object#toString()
      */
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name)
                                        .append("email", email).toString();
    }

    /**
     * @see java.lang.Object#equals()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Person)) {
            return false;
        }

        Person castOther = (Person) other;

        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }
}

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created on 08.des.2005
 */
package no.unified.soak.model;

import java.io.Serializable;

import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents the organization
 * 
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 * 
 * @author hrj
 * @hibernate.class table="organization" lazy="false"
 */
public class Organization extends BaseObject implements Serializable {
    /**
     * Eclipse generated UID
     */
    private static final long serialVersionUID = 6036269168761931708L;
    private Long id;
    private Long number;
    private String Name;
    private Boolean selectable;
    private String invoiceName;
    private Address invoiceAddress = new Address();
    private Organization parent;
    private Type type;

    public enum Type {
        MUNICIPALITY(1, 1), COUNTY(2, 1), AREA(3, 1), REGION(4, 2);
        private Integer typeDBValue;
        private Integer level;
        private String displayName;

        Type(int type, Integer level) {
            this.typeDBValue = type;
            this.level = level;
        }

        public static Type getTypeFromDBValue(Integer dbValue) {
            for (Type type : values()) {
                if (type.getTypeDBValue().equals(dbValue)) {
                    return type;
                }
            }
            return null;
        }

        public Integer getTypeDBValue() {
            return typeDBValue;
        }

        public Integer getLevel() {
            return level;
        }

        public void setDisplayName(String name) {
            this.displayName = name;
        }

        public String getDisplayName() {
            if (displayName == null) {
                displayName = ApplicationResourcesUtil.getText("organization.type.displayname." + getTypeDBValue());
            }
            return displayName;
        }
        
        @Override
        public String toString() {
            return new ToStringBuilder(this).append(getDisplayName()).toString();
        }

    }

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
        return Name;
    }

    /**
     * @spring.validator type="required"
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * @return Returns the number.
     * @hibernate.property column="number" not-null="true"
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @spring.validator type="required"
     * @param number
     *            The number to set.
     */
    public void setNumber(Long number) {
        this.number = number;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("number", this.number).append("name", this.getName()).append("type",
                this.getTypeAsEnum()).append("id", this.id).toString();
    }

    /**
     * @see java.lang.Object#equals()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Organization)) {
            return false;
        }

        Organization castOther = (Organization) other;

        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    /**
     * Returns the invoice address.
     * 
     * @return Address
     * 
     * @hibernate.component not-null="false" prefix="invoice_"
     */
    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    /**
     * Sets the invoice address.
     * 
     * @param invoiceAddress
     *            The invoice address to set
     * 
     */
    public void setInvoiceAddress(Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    /**
     * @return Returns the invoice name.
     * @hibernate.property column="invoice_name" not-null="false"
     */
    public String getInvoiceName() {
        return invoiceName;
    }

    /**
     * @param invoiceName
     *            The invoice name to set.
     */
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    /**
     * @return Returns the course.
     * @hibernate.many-to-one not-null="false" column="parentid" insert="false"
     *                        update="false" cascade="none"
     */
    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    /**
     * Gets the status of the registration.
     * 
     * @return The registration status
     * @hibernate.property column="type" not-null="true" type="integer"
     */
    public Integer getType() {
        if (type == null) {
            return null;
        }
        return type.getTypeDBValue();
    }

    public Type getTypeAsEnum() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(Integer type) {
        this.type = Type.getTypeFromDBValue(type);
    }

}

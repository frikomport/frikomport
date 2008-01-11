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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * This class represents the municipalities
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="municipalities"
 */
public class Municipalities extends BaseObject implements Serializable {
    /**
         * Eclipse generated UID
         */
    private static final long serialVersionUID = 6036269168761931708L;
    private Long id;
    private Long number;
    private String Name;
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
        return new ToStringBuilder(this).append("number", this.number)
                                        .append("name", this.getName())
                                        .append("id", this.id).toString();
    }

    /**
     * @see java.lang.Object#equals()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Municipalities)) {
            return false;
        }

        Municipalities castOther = (Municipalities) other;

        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }
}

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * This class is used to hold service areas
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="servicearea"
 */
public class ServiceArea extends BaseObject implements Serializable {
    /**
         * Eclipse generated UID
         */
    private static final long serialVersionUID = -681515519984791675L;
    private Long Id;
    private String name;
    private Boolean selectable;

    /**
     * @return Returns the id.
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return Id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        Id = id;
    }

    /**
     * @return Returns the name.
     * @hibernate.property column="name" length="30" not-null="true"
     */
    public String getName() {
        return name;
    }

    /**
     * @spring.validator type="required"
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
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
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("Id", Id).append("name", name)
                                        .toString();
    }

    /**
     * @see java.lang.Object#equals()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof ServiceArea)) {
            return false;
        }

        ServiceArea castOther = (ServiceArea) other;

        return new EqualsBuilder().append(Id, castOther.Id).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(Id).toHashCode();
    }
}

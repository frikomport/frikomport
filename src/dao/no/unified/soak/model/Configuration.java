package no.unified.soak.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @hibernate.class table="configuration" lazy="false"
 */
public class Configuration extends BaseObject implements Serializable {

    private static final long serialVersionUID = 2027089783395343906L;    
    private Long id;
    private String name;
    private Boolean active;
    private String value;

    /**
     * @hibernate.id column="id" not-null="true" generator-class="native"
     * @return The key
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @hibernate.property column="name" not-null="true" length="100"
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The configuration name to set
     */
    public void setName(String name) {
        this.name = name;
    }

	/**
	 * @hibernate.property column="active" not-null="true"
	 * @return Returns active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active
	 * The active to set.
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
    
    /**
     * @hibernate.property column="value" not-null="false" length="100"
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The configurationvalue to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    

    /**
     * @see java.lang.Object#equals()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Configuration)) {
            return false;
        }

        Configuration castOther = (Configuration) other;

        return new EqualsBuilder().append(id, castOther.id)
        							.append(name, castOther.name)
                                    .append(value, castOther.value)
                                    .isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id",id)
        								.append("name",name)
                                        .append("value",value)
                                        .toString();
    }
}

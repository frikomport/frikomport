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
    private String key;
    private Boolean active;
    private String value;

    /**
     * @hibernate.id column="cfg_key" not-null="true" length="100" generator-class="assigned"
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The konfigurationkey to set
     */
    public void setKey(String key) {
        this.key = key;
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

        return new EqualsBuilder().append(key, castOther.key)
                                    .append(value, castOther.value)
                                    .isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(key).toHashCode();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("key",key)
                                        .append("value",value)
                                        .toString();
    }
}

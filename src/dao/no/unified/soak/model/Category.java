package no.unified.soak.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 16.des.2008
 * 
 * @author gv
 * @hibernate.class table="category" lazy="false"
 */
public class Category extends BaseObject implements Serializable {
    
    private static final long serialVersionUID = 1351020199381038808L;
    private Long id;
    private String name;
    private Boolean selectable;

    public enum Name {
        HENDELSE("Hendelse");
        private String nameDBValue;
        
        Name(String status) {
            this.nameDBValue = status; 
        }
        
        public static Name getNameFromDBValue(String dbValue) {
            for (Name name : values()) {
                if (name.getDBValue().equals(dbValue)) {
                    return name;
                }
            }
            return null;
        }
        
        public String getDBValue() {
            return nameDBValue;
        }
    }

    /**
     * @return The id
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The name
     * @hibernate.property column="name" length="100" not-null="true" unique="true"
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name
     * @spring.validator type="required"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return If the category is selectable
     * @hibernate.property column="selectable"
     */
    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String toString() {
         return new ToStringBuilder(this).append("id", id)
                                        .append("name", name)
                                        .append("selectable", selectable)
                                        .toString();
    }

    public boolean equals(Object other) {
        if(!(other instanceof Category))
            return false;

        Category castOther = (Category) other;
        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }
}

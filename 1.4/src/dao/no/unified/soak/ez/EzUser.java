/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/**
 *
 */
package no.unified.soak.ez;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.unified.soak.model.BaseObject;


/**
 * @author kst
 *
 */
public class EzUser extends BaseObject implements Serializable {
    private Integer id;
    private String name;
    private String first_name;
    private String last_name;
    private String username;
    private Integer kommune;
    private String email;
    private List<String> rolenames = new ArrayList<String>();

    public String toString() {
        if ((getFirst_name() != null) || (getLast_name() != null)) {
            return getFirst_name() + " " + getLast_name();
        }

        return getName();
    }

    public boolean equals(Object o) {
        if (!(o instanceof EzUser) || (o == null)) {
            return false;
        }

        EzUser user = (EzUser) o;

        if (user.getId() == getId()) {
            return true;
        }

        return false;
    }

    public int hashCode() {
        return getId();
    }

    public void addRolename(String rolename) {
        rolenames.add(rolename);
    }

    public boolean hasRolename(String rolename) {
        return rolenames.contains(rolename);
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getUrl_name() {
        StringBuffer retStr = new StringBuffer();

        if (first_name != null) {
            retStr.append(first_name.toLowerCase());
        }

        if (first_name != null) {
            if (retStr.length() > 0) {
                retStr.append("_");
                retStr.append(last_name.toLowerCase());
            } else {
                retStr.append(last_name.toLowerCase());
            }
        }
        // Dersom resultatstrengen inneheld " " må dette fjernes før returnering
        
        return retStr.toString().replace(' ', '_');
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKommune() {
        return kommune;
    }

    public void setKommune(Integer kommune) {
        this.kommune = kommune;
    }
    
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        
    }

    public List<String> getRolenames() {
        return rolenames;
    }

    public void setRolenames(List<String> rolenames) {
        this.rolenames = rolenames;
    }
}

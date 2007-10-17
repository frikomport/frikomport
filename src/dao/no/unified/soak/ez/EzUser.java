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

import no.unified.soak.model.BaseObject;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * @author kst
 *
 */
public class EzUser extends BaseObject implements Serializable {
    private Integer id;
    private String name;
    private String first_name;
    private String last_name;
    private String user_name;
    private Integer kommune;
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
        if (rolenames.contains(rolename)) {
            return true;
        }

        return false;
    }

    /**
     * @return Returns the first_name.
     *
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @return Returns the url_name.
     *
     */
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

    /**
     * @param first_name
     *            The first_name to set.
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the kommune.
     */
    public Integer getKommune() {
        return kommune;
    }

    /**
     * @param kommune
     *            The kommune to set.
     */
    public void setKommune(Integer kommune) {
        this.kommune = kommune;
    }

    /**
     * @return Returns the last_name.
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name
     *            The last_name to set.
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return Returns the user_name.
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * @param user_name
     *            The user_name to set.
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the rolenames.
     */
    public List<String> getRolenames() {
        return rolenames;
    }

    /**
     * @param rolenames
     *            The rolenames to set.
     */
    public void setRolenames(List<String> rolenames) {
        this.rolenames = rolenames;
    }
}

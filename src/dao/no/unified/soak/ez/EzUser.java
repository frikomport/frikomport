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

import no.unified.soak.ext.IUser;
import no.unified.soak.model.BaseObject;


/**
 * @author kst
 *
 */
public class EzUser extends BaseObject implements Serializable, IUser {
    private Integer id;
    private String name;
    private String first_name;
    private String last_name;
    private String user_name;
    private Integer kommune;
    private String email;
    private List<String> rolenames = new ArrayList<String>();

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#toString()
     */
    public String toString() {
        if ((getFirst_name() != null) || (getLast_name() != null)) {
            return getFirst_name() + " " + getLast_name();
        }

        return getName();
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof EzUser) || (o == null)) {
            return false;
        }

        IUser user = (IUser) o;

        if (user.getId() == getId()) {
            return true;
        }

        return false;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#hashCode()
     */
    public int hashCode() {
        return getId();
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#addRolename(java.lang.String)
     */
    public void addRolename(String rolename) {
        rolenames.add(rolename);
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#hasRolename(java.lang.String)
     */
    public boolean hasRolename(String rolename) {
        return rolenames.contains(rolename);
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getFirst_name()
     */
    public String getFirst_name() {
        return first_name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getUrl_name()
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

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setFirst_name(java.lang.String)
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getId()
     */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getKommune()
     */
    public Integer getKommune() {
        return kommune;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setKommune(java.lang.Integer)
     */
    public void setKommune(Integer kommune) {
        this.kommune = kommune;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getLast_name()
     */
    public String getLast_name() {
        return last_name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setLast_name(java.lang.String)
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getUser_name()
     */
    public String getUser_name() {
        return user_name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setUser_name(java.lang.String)
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#getRolenames()
     */
    public List<String> getRolenames() {
        return rolenames;
    }

    /* (non-Javadoc)
     * @see no.unified.soak.ez.IUser#setRolenames(java.util.List)
     */
    public void setRolenames(List<String> rolenames) {
        this.rolenames = rolenames;
    }
}

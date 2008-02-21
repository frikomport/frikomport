package no.unified.soak.ext;

import java.util.List;

public interface IUser
{

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public abstract void addRolename(String rolename);

    public abstract boolean hasRolename(String rolename);
    
    /**
     * @return Returns the url_name.
     *
     */
    public abstract String getUrl_name();

    /**
     * @return Returns the first_name.
     *
     */
    public abstract String getFirst_name();

    /**
     * @param first_name
     *            The first_name to set.
     */
    public abstract void setFirst_name(String first_name);

    /**
     * @return Returns the id.
     */
    public abstract Integer getId();

    /**
     * @param id
     *            The id to set.
     */
    public abstract void setId(Integer id);

    /**
     * @return Returns the kommune.
     */
    public abstract Integer getKommune();

    /**
     * @param kommune
     *            The kommune to set.
     */
    public abstract void setKommune(Integer kommune);

    /**
     * @return Returns the last_name.
     */
    public abstract String getLast_name();

    /**
     * @param last_name
     *            The last_name to set.
     */
    public abstract void setLast_name(String last_name);

    /**
     * @return Returns the user_name.
     */
    public abstract String getUser_name();

    /**
     * @param user_name
     *            The user_name to set.
     */
    public abstract void setUser_name(String user_name);

    /**
     * @return Returns the name.
     */
    public abstract String getName();

    /**
     * @param name
     *            The name to set.
     */
    public abstract void setName(String name);
    
    /**
     * @return Returns the email.
     */
    public abstract String getEmail();

    /**
     * @param name
     *            The email to set.
     */
    public abstract void setEmail(String email);

    /**
     * @return Returns the rolenames.
     */
    public abstract List<String> getRolenames();

    /**
     * @param rolenames
     *            The rolenames to set.
     */
    public abstract void setRolenames(List<String> rolenames);

}
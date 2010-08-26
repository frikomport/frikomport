/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;


/**
 * User Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="UserDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface UserDAO extends DAO {
    /**
     * Gets users information based on login name.
     * @param username the current username
     * @return user populated user object
     */
    public User getUser(String username);

    /**
     * Gets users information based on email address.
     * @param email the current email
     * @return user populated user object
     */
    public User findUserByEmail(String email);

    /**
     * Gets a list of users based on parameters passed in.
     * @return List populated list of users
     */
    public List getUsers(User user, boolean hashuserFilter);

    /**
     * Gets users information based on hash.
     * @param hash the current hash
     * @return user populated user object
     */
    public User getUserByHash(String hash);

    /**
     * Saves a user's information
     * @param user the object to be saved
     */
    public void saveUser(User user);

    /**
     * Updates a user's information
     * @param user the object to be updated
     */
    public void updateUser(User user);

    /**
     * Removes a user from the database by id
     * @param username the user's username
     */
    public void removeUser(String username);

    /**
     * Gets a userCookie object from the database,
     * based on username and password
     * @param cookie with username and password
     */
    public UserCookie getUserCookie(UserCookie cookie);

    /**
     * Saves a userCookie object to the database
     * @param cookie
     */
    public void saveUserCookie(UserCookie cookie);

    /**
     * Removes all cookies for a specified username
     * @param username
     */
    public void removeUserCookies(String username);

    /**
     * Gets a list of users based on a rolename
     * @return List populated list of users
     */
    public List getUsersByRole(String rolename);

    /**
     * Gets a list of users based on a list of rolenames
     * @return List populated list of users
     */
    public List getUsersByRoles(List<String> rolenames);

}

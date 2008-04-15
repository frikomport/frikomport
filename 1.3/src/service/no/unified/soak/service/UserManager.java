/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service;

import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.User;

import java.util.List;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * <p><a href="UserManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
public interface UserManager {
    public void setUserDAO(UserDAO dao);

    /**
     * Retrieves a user by username.  An exception is thrown if now user
     * is found.
     *
     * @param username
     * @return User
     */
    public User getUser(String username);

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     * @param user parameters to filter on
     * @return List
     */
    public List getUsers(User user);

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @throws UserExistsException
     */
    public void saveUser(User user) throws UserExistsException;

    /**
     * Removes a user from the database by their username
     *
     * @param username the user's username
     */
    public void removeUser(String username);

    /**
     * Validates a user based on a cookie value.  If successful, it returns
     * a new cookie String.  If not, then it returns null.
     *
     * @param value (in format username|guid)
     * @return indicator that this is a valid login (null == invalid)
     */
    public String checkLoginCookie(String value);

    /**
     * Creates a cookie string using a username - designed for use when
     * a user logs in and wants to be remembered.
     *
     * @param username
     * @return String to put in a cookie for remembering user
     */
    public String createLoginCookie(String username);

    /**
     * Deletes all cookies for user.
     * @param username
     */
    public void removeLoginCookies(String username);
}

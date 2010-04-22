/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.service;

import java.util.List;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.User;
import no.unified.soak.model.Registration;

import org.springframework.context.MessageSource;

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

	public void setExtUserDAO(ExtUserDAO userEzDAO);

	public void setRoleManager(RoleManager roleManager);

	public void setMessageSource(MessageSource messageSource);

	/**
	 * Retrieves a user by username.  An exception is thrown if no user
	 * is found.
	 *
	 * @param username
	 * @return User
	 */
	public User getUser(String username);

    /**
	 * Retrieves a user by email.  An exception is thrown if no user
	 * is found.
	 *
	 * @param username
	 * @return User
	 */
    public User findUserByEmail(String email);

    /**
	 * Retrieves a list of users, filtering with parameters on a user object
	 * @param user parameters to filter on
	 * @return List
	 */
	public List getUsers(User user);

	/**
	 * Retrieves a user by hash.  An exception is thrown if no user
	 * is found.
	 *
	 * @param hash
	 * @return User
	 */
	public User getUserByHash(String hash);
	
	/**
	 * Saves a user's information
	 *
	 * @param user the user's information
	 * @throws UserExistsException
	 */
	public void saveUser(User user) throws UserExistsException;

    /**
     * Updates a user's information
     * @param user the user to be updated
     */
    public void updateUser(User user);

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

	/**
	 * Retrieves all responsibles from eZ publish.
	 * @return
	 */
	public List getResponsibles();

	/**
	 * Retrieves all roles.
	 * @return
	 */
	public List getRoles();

	/**
	 * Add new user 
	 * @param username username for user
	 * @param firstName firstname for user
	 * @param lastName lastname for user
	 * @param email email for user
	 * @param id id for user
	 * @param rolenames rolenames for roles to be added to user
	 * @param kommune kommune for user
	 * @param mobilePhone 
	 * @param phoneNumber 
	 * @return user
	 * @return null if the user could not be saved.
	 */
	public User addUser(String username, String firstName, String lastName, String email, Integer id,
			List<String> rolenames, Integer kommune, String mobilePhone, String phoneNumber);

	/**
	 * Update user 
	 * @param user
	 * @param firstName firstname for user
	 * @param lastName lastname for user
	 * @param email email for user
	 * @param id id for user
	 * @param rolenames rolenames for roles to be added to user
	 * @param kommune kommune for user
	 * @param phoneNumber 
	 * @param mobilePhone 
	 * @return 
	 */
	public void updateUser(User user, String firstName, String lastName, String email, Integer id,
			List<String> rolenames, Integer kommune, String mobilePhone, String phoneNumber);

    /**
     * Adds new user based on a registration
     * @param registration
     */
    public User addUser(Registration registration);

    /**
     * Sets the enabled flag to false
     * @param user
     * @return
     */
    public void disableUser(User user);

    /**
     * Sets the enabled flag to true
     * @param user
     * @return
     */
    public void enableUser(User user);
}

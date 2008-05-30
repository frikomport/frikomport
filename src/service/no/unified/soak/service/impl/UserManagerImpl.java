/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import no.unified.soak.Constants;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.dao.jdbc.UserEzDaoJdbc;
import no.unified.soak.ez.EzUser;
import no.unified.soak.model.Address;
import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.UserExistsException;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.RandomGUID;
import no.unified.soak.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;


/**
 * Implementation of UserManager interface.</p>
 *
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserManagerImpl extends BaseManager implements UserManager {
    private UserDAO dao;
    
    private UserEzDaoJdbc userEzDaoJdbc;
    private RoleManager roleManager;
    private MessageSource messageSource;

    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setUserDAO(UserDAO dao) {
        this.dao = dao;
    }
    
	public void setUserEzDaoJdbc(UserEzDaoJdbc userEzDaoJdbc) {
		this.userEzDaoJdbc = userEzDaoJdbc;
	}

    public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	/**
     * @see no.unified.soak.service.UserManager#getUser(java.lang.String)
     */
    public User getUser(String username) {
        return dao.getUser(username);
    }

    /**
     * @see no.unified.soak.service.UserManager#getUsers(no.unified.soak.model.User)
     */
    public List getUsers(User user) {
        return dao.getUsers(user);
    }

    /**
     * @see no.unified.soak.service.UserManager#saveUser(no.unified.soak.model.User)
     */
    public void saveUser(User user) throws UserExistsException {
        try {
            dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * @see no.unified.soak.service.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String username) {
        if (log.isDebugEnabled()) {
            log.debug("removing user: " + username);
        }

        dao.removeUser(username);
    }

    /**
     * @see no.unified.soak.service.UserManager#checkLoginCookie(java.lang.String)
     */
    public String checkLoginCookie(String value) {
        value = StringUtil.decodeString(value);

        String[] values = StringUtils.split(value, "|");

        // in case of empty username in cookie, return null
        if (values.length == 1) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("looking up cookieId: " + values[1]);
        }

        UserCookie cookie = new UserCookie();
        cookie.setUsername(values[0]);
        cookie.setCookieId(values[1]);
        cookie = dao.getUserCookie(cookie);

        if (cookie != null) {
            if (log.isDebugEnabled()) {
                log.debug("cookieId lookup succeeded, generating new cookieId");
            }

            return saveLoginCookie(cookie);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("cookieId lookup failed, returning null");
            }

            return null;
        }
    }

    /**
     * @see no.unified.soak.service.UserManager#createLoginCookie(java.lang.String)
     */
    public String createLoginCookie(String username) {
        UserCookie cookie = new UserCookie();
        cookie.setUsername(username);

        return saveLoginCookie(cookie);
    }

    /**
     * Convenience method to set a unique cookie id and save to database
     * @param cookie
     * @return
     */
    private String saveLoginCookie(UserCookie cookie) {
        cookie.setCookieId(new RandomGUID().toString());
        dao.saveUserCookie(cookie);

        return StringUtil.encodeString(cookie.getUsername() + "|" +
            cookie.getCookieId());
    }

    /**
     * @see no.unified.soak.service.UserManager#removeLoginCookies(java.lang.String)
     */
    public void removeLoginCookies(String username) {
        dao.removeUserCookies(username);
    }
    
    public List getRoles() {
    	List roles = roleManager.getRoles(null);
    	return roles;
    }
    
    public List getResponsibles() {
		List ezUsers = getEZResponsibles(null);
		List users = new ArrayList();
		for (Iterator iter = ezUsers.iterator(); iter.hasNext();) {
			EzUser ezUser = (EzUser) iter.next();
			try {
				users.add(dao.getUser(ezUser.getUsername()));
			} catch (ObjectRetrievalFailureException objectRetrievalFailureException) {
				User user = addUser(ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser.getEmail(), ezUser.getId(), ezUser.getRolenames());
				users.add(user);
			}
		}
		return users;
	}

	public User addUser(String username, String firstName, String lastName, String email, Integer id, List<String> rolenames) {
		User user = new User(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setId(id);
		setRoles(rolenames, user);
		user.setEnabled(true);
		Address address = new Address();
		address.setPostalCode("0");
		user.setAddress(address);
		try {
			saveUser(user);
			return user;
		} catch (Exception e) {
			log.error("Exception: " + e);
			return null;
		}

	}
	
	public void updateUser(User user, String firstName, String lastName, String email, Integer id, List<String> rolenames) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setId(id);
		user.setEnabled(true);
		setRoles(rolenames, user);
		try {
			saveUser(user);
		} catch (Exception e) {
			log.error("Exception: " + e);
		}
	}
	
	private void setRoles(List<String> rolenames, User user) {
		// remove existing roles before new ones are added.
		user.removeAllRoles();
		Locale locale = LocaleContextHolder.getLocale();

		for (Iterator iter = rolenames.iterator(); iter.hasNext();) {
			String rolename = (String) iter.next();
			if (rolename.equals(messageSource.getMessage("role.employee", null, locale))) {
				user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
			} else if (rolename.equals(messageSource.getMessage("role.anonymous", null, locale))) {
				user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
			} else if (rolename.equals(messageSource.getMessage("role.editor", null, locale))) {
				user.addRole(roleManager.getRole(Constants.EDITOR_ROLE));
			} else if (rolename.equals(messageSource.getMessage("role.admin", null, locale))) {
				user.addRole(roleManager.getRole(Constants.ADMIN_ROLE));
			} else if (rolename.equals(messageSource.getMessage("role.instructor", null, locale))) {
				user.addRole(roleManager.getRole(Constants.INSTRUCTOR_ROLE));
			} else if (roleManager.getRole(rolename) != null) {
				user.addRole(roleManager.getRole(rolename));
			} else {
				no.unified.soak.model.Role role = new no.unified.soak.model.Role(rolename);
				role.setDescription(rolename);
				roleManager.saveRole(role);
				user.addRole(role);
			}
		}
	}

	private List getEZResponsibles(EzUser user) {
		List users = userEzDaoJdbc.findKursansvarligeUser();
		return users;
	}

}

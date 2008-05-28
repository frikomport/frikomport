/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service.impl;

import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;
import no.unified.soak.service.UserExistsException;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.util.RandomGUID;
import no.unified.soak.util.StringUtil;

import org.apache.commons.lang.StringUtils;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;


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

    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setUserDAO(UserDAO dao) {
        this.dao = dao;
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
}

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.dao.hibernate;

import java.util.List;

import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;

import org.springframework.orm.ObjectRetrievalFailureException;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * <p>
 * <a href="UserDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
  *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a>
*/
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {
    /**
     * @see no.unified.soak.dao.UserDAO#getUser(java.lang.String)
     */
    public User getUser(String username) {
        User user = (User) getHibernateTemplate().get(User.class, username);

        if (user == null) {
            log.warn("uh oh, user '" + username + "' not found...");
            throw new ObjectRetrievalFailureException(User.class, username);
        }

        return user;
    }

    /**
     * @see no.unified.soak.dao.UserDAO#getUsers(no.unified.soak.model.User)
     */
    public List getUsers(User user) {
        return getHibernateTemplate().find("from User u order by upper(u.username)");
    }

    
    /**
     * @see no.unified.soak.dao.UserDAO#getUsers(no.unified.soak.model.User)
     */
    public User getUserByHash(String hash) {
        List users = getHibernateTemplate().find("from User u where u.hash = hash)");
        return (User) users.get(0);
    }
    
    /**
     * @see no.unified.soak.dao.UserDAO#saveUser(no.unified.soak.model.User)
     */
    public void saveUser(final User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getUsername());
        }

        getHibernateTemplate().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getHibernateTemplate().flush();
    }

    /**
     * @see no.unified.soak.dao.UserDAO#removeUser(java.lang.String)
     */
    public void removeUser(String username) {
        removeUserCookies(username);

        User user = getUser(username);
        getHibernateTemplate().delete(user);
    }

    /**
     * @see no.unified.soak.dao.UserDAO#getUserCookie(no.unified.soak.model.UserCookie)
     */
    public UserCookie getUserCookie(final UserCookie cookie) {
        List cookies = getHibernateTemplate().find("from UserCookie c where c.username=? and c.cookieId=?",
                new Object[] { cookie.getUsername(), cookie.getCookieId() });

        if (cookies.size() == 0) {
            return null;
        }

        return (UserCookie) cookies.get(0);
    }

    /**
     * @see no.unified.soak.dao.UserDAO#removeUserCookies(java.lang.String)
     */
    public void removeUserCookies(String username) {
        // delete any cookies associated with this user
        List cookies = getHibernateTemplate().find("from UserCookie c where c.username=?", username);

        if ((cookies.size() > 0) && log.isDebugEnabled()) {
            log.debug("deleting " + cookies.size() + " cookies for user '" +
                username + "'");
        }

        getHibernateTemplate().deleteAll(cookies);
    }

    /**
     * @see no.unified.soak.dao.UserDAO#saveUserCookie(no.unified.soak.model.UserCookie)
     */
    public void saveUserCookie(UserCookie cookie) {
        getHibernateTemplate().saveOrUpdate(cookie);
    }

}

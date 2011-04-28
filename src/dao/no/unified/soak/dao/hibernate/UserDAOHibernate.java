/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.dao.hibernate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import no.unified.soak.dao.UserDAO;
import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;

import org.hibernate.Query;
import org.hibernate.StaleObjectStateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 * 
 * <p>
 * <a href="UserDAOHibernate.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {
	/**
	 * @see no.unified.soak.dao.UserDAO#getUser(java.lang.String)
	 */
	public User getUser(String username) {
		User user = getUserSilent(username);
		if (user == null)
			throw new ObjectRetrievalFailureException(User.class, username);
		// evict(user); // to avoid StaleObjectException /
		// OptimisticLockingFailed
		return user;
	}
	
	RetryMechanism.OperationInterface<User, String> getUserOperation = new RetryMechanism.OperationInterface<User, String>() {
		public User operate(String param) {
			return (User) getHibernateTemplate().get(User.class, param);
		}
	};

	/**
	 * @see no.unified.soak.dao.UserDAO#getUserSilent(java.lang.String)
	 */
	public User getUserSilent(String username) {
    	User user = RetryMechanism.executeOperationWithRetriesOnStaleObjectState(getUserOperation, username);
    	
//        if (user != null) evict(user); // to avoid StaleObjectException / OptimisticLockingFailed
    	return user;
    }

	public User findUserByEmail(String email) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("email", email));
		List<User> result = findUsersByCriteria(criteria);
		if (result == null || result.size() != 1) {
			return null;
		}
		User user = result.get(0);
		// if (user != null) evict(user); // to avoid StaleObjectException /
		// OptimisticLockingFailed
		return user;
	}

	RetryMechanism.OperationInterface<List<User>, DetachedCriteria> findUsersOperation = new RetryMechanism.OperationInterface<List<User>, DetachedCriteria>() {
		public List<User> operate(DetachedCriteria param) {
			return getHibernateTemplate().findByCriteria(param);
		}
	};
	
	public List<User> findUsersByCriteria(DetachedCriteria criteria) {
		List<User> result = RetryMechanism.executeOperationWithRetriesOnStaleObjectState(findUsersOperation, criteria);
		return result;
	}

	/**
	 * @see no.unified.soak.dao.UserDAO#getUsers(no.unified.soak.model.User)
	 */
	public List getUsers(User user, boolean hashuserFilter) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);

		if (user != null) {
			if (user.getUsername() != null && !"".equals(user.getUsername())) {
				criteria.add(Restrictions.like("username",
						"%" + user.getUsername() + "%").ignoreCase());
			}
			if (user.getFirstName() != null && !"".equals(user.getFirstName())) {
				criteria.add(Restrictions.like("firstName",
						"%" + user.getFirstName() + "%").ignoreCase());
			}
			if (user.getLastName() != null && !"".equals(user.getLastName())) {
				criteria.add(Restrictions.like("lastName",
						"%" + user.getLastName() + "%").ignoreCase());
			}
			if (user.getEmail() != null && !"".equals(user.getEmail())) {
				criteria.add(Restrictions.like("email",
						"%" + user.getEmail() + "%").ignoreCase());
			}

			if (hashuserFilter) {
				criteria.add(Restrictions.eq("hashuser", user.getHashuser()));
			}
		}

		criteria.addOrder(Order.asc("username"));

		List<User> result = findUsersByCriteria(criteria);
		// Iterator<User> it = result.iterator();
		// while(it.hasNext()){
		// evict(it.next()); // to avoid StaleObjectException /
		// OptimisticLockingFailed
		// }
		return result;
	}

	/**
	 * @see no.unified.soak.dao.UserDAO#getUsersByRole(java.lang.String)
	 */
	public List getUsersByRole(String rolename) {
		if (log.isDebugEnabled())
			log.debug("getUsersByRole: " + rolename);
		String hql = "select distinct a from User a join a.roles t where t.name = '"
				+ rolename + "'";
		Query query = getSession().createQuery(hql);
		List<User> users = query.list();

		if (users != null && !users.isEmpty()) {
			Iterator i = users.iterator();
			while (i.hasNext()) {
				User u = (User) i.next();
				// evict(u); // to avoid StaleObjectException /
				// OptimisticLockingFailed
				List tmpRoles = u.getRoleNameList();
				if (log.isDebugEnabled())
					log.debug("Found: " + u.getFullName() + "(" + u.getEmail()
							+ ")" + ", " + tmpRoles.toString());
			}
		}
		return users;
	}

	/**
	 * @see no.unified.soak.dao.UserDAO#getUsersByRoles(java.util.List<String>)
	 */
	public List getUsersByRoles(List<String> rolenames) {
		Hashtable<String, User> users = new Hashtable<String, User>();
		Iterator r = rolenames.iterator();
		while (r.hasNext()) {
			String rolename = (String) r.next();
			List tmp = getUsersByRole(rolename);
			Iterator u = tmp.iterator();
			while (u.hasNext()) {
				User user = (User) u.next();
				// evict(user); // to avoid StaleObjectException /
				// OptimisticLockingFailed
				// prevents duplicates
				users.put(user.getUsername(), user);
			}
		}

		List<User> result = new ArrayList<User>(users.size());
		Enumeration all = users.elements();
		while (all.hasMoreElements()) {
			result.add((User) all.nextElement());
		}

		return result;
	}

	/**
	 * @see no.unified.soak.dao.UserDAO#getUsers(no.unified.soak.model.User)
	 */
	public User getUserByHash(String hash) {
		hash = removeTrailingEquals(hash); // FKM-546 Removing trailing equals
		// signs
		List users = getHibernateTemplate().find(
				"from User u where u.hash like '" + hash
						+ "%' and u.enabled = 1");

		if (users.size() == 0) {
			return null;
		}
		User user = (User) users.get(0);
		// if(user != null){
		// evict(user); // to avoid StaleObjectException /
		// OptimisticLockingFailed
		// }
		return user;
	}

	private String removeTrailingEquals(String hash) {
		while (hash.endsWith("=")) {
			hash = hash.substring(0, hash.length() - 2);
		}
		return hash;
	}

	/**
	 * @see no.unified.soak.dao.UserDAO#saveUser(no.unified.soak.model.User)
	 */
	public void saveUser(final User user) {
		if (log.isDebugEnabled()) {
			// log.debug("user's id: " + user.getUsername());
		}
		int retryCount = 0;
		while (retryCount < 3) {
			try {
				getHibernateTemplate().saveOrUpdate(user);
				getHibernateTemplate().flush();
				break; // lagring ok
			} catch (StaleObjectStateException e) {
				retryCount++;
				if (retryCount == 3) {
					log
							.warn("StaleObjectStateException - saveUser gir opp etter 3 forsøk med samme feilresultat for ["
									+ user.getUsername() + "]");
					break; // give up
				}
				try {
					Thread.sleep(125);
				} catch (InterruptedException ie) { /* do nothing */
				}
				log.info("Gjentakelse nr: " + retryCount + " for ["
						+ user.getUsername()
						+ "] i saveUser pga. StaleObjectStateException");
			}
		}
	}

	public void updateUser(User user) {
		if (log.isDebugEnabled()) {
			// log.debug("user's id: " + user.getUsername());
		}
		int retryCount = 0;
		while (retryCount < 3) {
			try {
				getHibernateTemplate().update(user);
				getHibernateTemplate().flush();
				break; // oppdatering ok
			} catch (StaleObjectStateException e) {
				retryCount++;
				if (retryCount == 3) {
					log
							.warn("StaleObjectStateException - updateUser gir opp etter 3 forsøk med samme feilresultat for ["
									+ user.getUsername() + "]");
					break; // give up
				}
				try {
					Thread.sleep(125);
				} catch (InterruptedException ie) { /* do nothing */
				}
				log.info("Gjentakelse nr: " + retryCount + " for ["
						+ user.getUsername()
						+ "] i updateUser pga. StaleObjectStateException");
			}
		}
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
		List cookies = getHibernateTemplate().find(
				"from UserCookie c where c.username=? and c.cookieId=?",
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
		List cookies = getHibernateTemplate().find(
				"from UserCookie c where c.username=?", username);

		if ((cookies.size() > 0) && log.isDebugEnabled()) {
			log.debug("deleting " + cookies.size() + " cookies for user '"
					+ username + "'");
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

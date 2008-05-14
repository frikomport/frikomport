package no.unified.soak.dao.hibernate;

import java.util.List;

import no.unified.soak.model.Notification;
import no.unified.soak.model.Registration;
import no.unified.soak.dao.NotificationDao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;

public class NotificationDaoHibernate extends BaseDAOHibernate implements
		NotificationDao {

	/**
	 * @see no.unified.soak.dao.NotificationDao#getNotifications(no.unified.soak.model.Notification)
	 */
	public List getNotifications(final Notification notification) {
		return getHibernateTemplate().find("from Notification");

		/*
		 * Remove the line above and uncomment this code block if you want to
		 * use Hibernate's Query by Example API. if (notification == null) {
		 * return getHibernateTemplate().find("from Notification"); } else { //
		 * filter on properties set in the notification HibernateCallback
		 * callback = new HibernateCallback() { public Object
		 * doInHibernate(Session session) throws HibernateException { Example ex =
		 * Example.create(notification).ignoreCase().enableLike(MatchMode.ANYWHERE);
		 * return session.createCriteria(Notification.class).add(ex).list(); } };
		 * return (List) getHibernateTemplate().execute(callback); }
		 */
	}

	/**
	 * @see no.unified.soak.dao.NotificationDao#getUnsentNotifications()
	 */
	public List<Notification> getUnsentNotifications() {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Notification.class);
		criteria.add(Restrictions.eq("reminderSent", false));
		List test = getHibernateTemplate().findByCriteria(criteria);
		return test;
	}

	/**
	 * @see no.unified.soak.dao.NotificationDao#getNotification(Long id)
	 */
	public Notification getNotification(final Long id) {
		Notification notification = (Notification) getHibernateTemplate().get(
				Notification.class, id);
		if (notification == null) {
			log.warn("uh oh, notification with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Notification.class, id);
		}

		return notification;
	}

	/**
	 * @see no.unified.soak.dao.NotificationDao#saveNotification(Notification
	 *      notification)
	 */
	public void saveNotification(final Notification notification) {
		log.debug("NotificationDaoHibernate: saveNotification");
		getHibernateTemplate().saveOrUpdate(notification);
	}

	/**
	 * @see no.unified.soak.dao.NotificationDao#removeNotification(Long id)
	 */
	public void removeNotification(final Long id) {
		getHibernateTemplate().delete(getNotification(id));
	}
	
	public Notification getNotificationOrNew(Long registrationId) {
		Notification result = null;
		if (registrationId != null) {
			DetachedCriteria criteria = DetachedCriteria
			.forClass(Notification.class);
			
			criteria.add(Restrictions.eq("registrationid", registrationId));
			List returned = getHibernateTemplate().findByCriteria(criteria);
			if (returned != null && returned.size() > 0) {
				result = (Notification) returned.get(0);
			}
		}
		if (result == null) {
			result = new Notification();
		}
		return result;
	}
}

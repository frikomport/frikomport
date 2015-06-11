package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.model.Notification;

public interface NotificationDao extends DAO {

	/**
	 * Retrieves all of the notifications
	 */
	public List getNotifications(Notification notification);

	/**
	 * Gets notification's information based on primary key. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 * 
	 * @param id
	 *            the notification's id
	 * @return notification populated notification object
	 */
	public Notification getNotification(final Long id);

	/**
	 * Saves a notification's information
	 * 
	 * @param notification
	 *            the object to be saved
	 */
	public void saveNotification(Notification notification);

	/**
	 * Removes a notification from the database by id
	 * 
	 * @param id
	 *            the notification's id
	 */
	public void removeNotification(final Long id);

	/**
	 * Returns all notifications where the reminder has not been sent
	 * 
	 * @return a list of all notification where the reminder has not yet been sent, but that are due for sending
	 */
	public List<Notification> getUnsentNotifications();
	
	/**
	 * Finds the notifications by the registration id, or returns a empty Notifications if this does not exist.
	 * 
	 * @param regId The registrationId we want to find the notification for
	 * @return The notification for the given registration if it exists, if not - an empty Notification
	 */
	public List<Notification> getNotificationsOrNew(Long registrationid, boolean courseHasFollowup);
}

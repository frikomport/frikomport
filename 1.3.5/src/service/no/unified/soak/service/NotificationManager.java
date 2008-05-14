package no.unified.soak.service;

import java.util.List;

import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;

public interface NotificationManager extends Manager {
	/**
	 * Retrieves all of the notifications
	 */
	public List getNotifications(Notification notification);

	/**
	 * Gets notification's information based on id.
	 * 
	 * @param id
	 *            the notification's id
	 * @return notification populated notification object
	 */
	public Notification getNotification(final String id);

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
	public void removeNotification(final String id);

	/**
	 * Returns all notifications where the reminder has not been sent
	 * 
	 * @return a list of all notification where the reminder has not yet been sent, but that are due for sending
	 */
	public List<Notification> getUnsentNotifications();

	/**
	 * Sends reminder to all that should have reminders
	 */
	public void sendReminders();

	/**
	 * Sets all the notification for the given course as not sent.
	 * 
	 * @param course
	 *            The course that we want to reset the notifications for
	 */
	public void resetCourse(Course course);

}

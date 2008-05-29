package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;
import no.unified.soak.model.Registration;
import no.unified.soak.dao.NotificationDao;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.util.MailUtil;

public class NotificationManagerImpl extends BaseManager implements
		NotificationManager {
	private NotificationDao dao;

	protected MailEngine mailEngine = null;

	private MessageSource messageSource = null;

	private RegistrationManager registrationManager = null;
	
	private SimpleMailMessage message = null;

	private Locale locale = null;

	/**
	 * @param mailEngine
	 *            the mailEngine to set
	 */
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}


	/**
	 * @param registrationManager the registrationManager to set
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	/**
	 * @param messageSource
	 *            the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Set the Dao for communication with the data layer.
	 * 
	 * @param dao
	 */
	public void setNotificationDao(NotificationDao dao) {
		this.dao = dao;
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#getNotifications(no.unified.soak.model.Notification)
	 */
	public List getNotifications(final Notification notification) {
		return dao.getNotifications(notification);
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#getNotification(String
	 *      id)
	 */
	public Notification getNotification(final String id) {
		return dao.getNotification(new Long(id));
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#saveNotification(Notification
	 *      notification)
	 */
	public void saveNotification(Notification notification) {
		dao.saveNotification(notification);
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#removeNotification(String
	 *      id)
	 */
	public void removeNotification(final String id) {
		dao.removeNotification(new Long(id));
	}

	/**
	 * Returns all notifications where the reminder has not been sent
	 * 
	 * @return a list of all notification where the reminder has not yet been
	 *         sent, but that are due for sending
	 */
	public List<Notification> getUnsentNotifications() {
		return dao.getUnsentNotifications();
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#sendReminders()
	 */
	public void sendReminders() {
//		log.debug("sendReminders");
		// Fetch all the Notifications that does not have the sent-flag set.
		List<Notification> notifications = this.getUnsentNotifications();
		ArrayList<SimpleMailMessage> emails = new ArrayList<SimpleMailMessage>();
		if (notifications != null && notifications.size() > 0) {
			for (int i = 0; i < notifications.size(); i++) {
				Date today = new Date();
				Notification notification = notifications.get(i);

				// The if-test is added mostly because of bad sample data (lazy
				// programmer)
				if (notification.getRegistration() != null
						&& notification.getRegistration().getCourse() != null) {
					Course course = notification.getRegistration().getCourse();
					// Are we after the time of notification?
					if (course.getReminder() != null && course.getReminder().before(today)) {
						// Yupp, we are: Send notification email
//						log
//								.debug("We've got one: "
//										+ notification.getRegistration()
//												.getCourseid()
//										+ " for "
//										+ notification.getRegistration()
//												.getFirstName() + " " + notification.getRegistration().getLastName());

						// Store that it has been successfully sent - cleanup by
						// cleanup manager
						String localeLanguage = notification.getRegistration()
								.getLocale();
						Locale locale = new Locale(localeLanguage);
						StringBuffer msg = MailUtil.createStandardBody(course,
								Constants.EMAIL_EVENT_NOTIFICATION, locale,
								messageSource, null, false);
						ArrayList<Registration> registrations = new ArrayList<Registration>();
						registrations.add(notification.getRegistration());
						ArrayList<SimpleMailMessage> newEmails = MailUtil
								.setMailInfo(registrations,
										Constants.EMAIL_EVENT_NOTIFICATION,
										course, msg, messageSource, locale, null);
						emails.addAll(newEmails);
						notification.setReminderSent(true);
						dao.saveNotification(notification);
					}
				}
			}
		}
		if (emails != null && emails.size() > 0) {
			MailUtil.sendMails(emails, mailEngine);
		}
	}
	
	public void resetCourse(Course course) {
		List<Registration> result = registrationManager.getCourseRegistrations(course.getId());
		if (result != null && result.size() > 0) {
			for (int i=0; i < result.size(); i++) {
				Notification notification = dao.getNotificationOrNew(result.get(i).getId());
				notification.setRegistrationid(result.get(i).getId());
				notification.setReminderSent(false);
				log.debug("Saving notification with regId: " + notification.getRegistrationid() );
				this.saveNotification(notification);
			}
		}	
	}
	
	private String getText(String key, String attribute, Locale locale) {
		return MailUtil.getText(key, attribute, locale, messageSource);
	}

	private String getText(String key, Locale locale) {
		return MailUtil.getText(key, locale, messageSource);
	}
}

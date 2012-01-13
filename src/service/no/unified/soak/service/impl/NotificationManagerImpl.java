package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.mail.internet.MimeMessage;

import no.unified.soak.Constants;
import no.unified.soak.dao.NotificationDao;
import no.unified.soak.model.Course;
import no.unified.soak.model.Notification;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.MailUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.StaleObjectStateException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailSender;

public class NotificationManagerImpl extends BaseManager implements NotificationManager {
	private NotificationDao dao;

	protected MailEngine mailEngine = null;

	private RegistrationManager registrationManager = null;
	private ConfigurationManager configurationManager = null;
	private UserManager userManager = null;
	
    private MailSender mailSender = null;

    public void executeTask() {
        log.info("running NotificationManager");
        sendReminders();
    }

	public void setLocale(Locale locale) {}
	public void setMessageSource(MessageSource messageSource) {}

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
	 * @param mailEngine
	 *            the mailEngine to set
	 */
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	/**
	 * @param registrationManager the registrationManager to set
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	/**
	 * @param configurationManager the registrationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
    	try {
    		return dao.getNotifications(notification);
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    			return dao.getNotifications(notification);
    		}
    		throw e;
    	}
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#getNotification(String
	 *      id)
	 */
	public Notification getNotification(final String id) {
    	try {
    		return dao.getNotification(new Long(id));
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    			return dao.getNotification(new Long(id));
    		}
    		throw e;
    	}
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
    	try {
    		return dao.getUnsentNotifications();
    	}
    	catch(StaleObjectStateException e){
    		if(handleStaleObjectStateExceptionForUserObject(e, userManager)){
    			return dao.getUnsentNotifications();
    		}
    		throw e;
    	}
	}

	/**
	 * @see no.unified.soak.service.NotificationManager#sendReminders()
	 */
	public void sendReminders() {
		
		LocaleContextHolder.setLocale(ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(null));
		
		NotificationSummary notificationSummary = new NotificationSummary();

		// Fetch all the Notifications that does not have the sent-flag set.
		List<Notification> notifications = this.getUnsentNotifications();

		ArrayList<MimeMessage> emails = new ArrayList<MimeMessage>();

		if (notifications != null && notifications.size() > 0) {
			// order notifications by lastname, firstname
			NotificationComparator nc = new NotificationComparator();
			Collections.sort(notifications, nc);
			
			for (int i = 0; i < notifications.size(); i++) {
				Date today = new Date();
				Notification notification = notifications.get(i);
				Registration registration = notification.getRegistration();
				// The if-test is added mostly because of bad sample data (lazy programmer)
				if (registration != null && registration.getCourse() != null) {

					if(registration.getStatusAsEnum() == Status.CANCELED || registration.getStatusAsEnum() == Status.INVITED){
						// notification is only to be sent to reserved/waiting registrations
						dao.removeNotification(notification.getId());
						continue;
					}
					
					Course course = notification.getRegistration().getCourse();
					// Are we after the time of notification?
					if (course.getStatus().equals(CourseStatus.COURSE_PUBLISHED) 
					        && course.getReminder() != null 
					        && course.getReminder().before(today)
					        && registration.getRegistered().before(course.getReminder())) {
						// Store that it has been successfully sent - cleanup by
						// cleanup manager
						boolean isReserved = notification.getRegistration().getStatusAsEnum() == Status.RESERVED;
                        StringBuffer msg = MailUtil.create_EMAIL_EVENT_NOTIFICATION_body(course, registration, null, isReserved, configurationManager.getConfigurationsMap());
						ArrayList<Registration> registrations = new ArrayList<Registration>();
						registrations.add(registration);
						ArrayList<MimeMessage> newEmails = MailUtil.getMailMessages(registrations, Constants.EMAIL_EVENT_NOTIFICATION, course, msg, null, mailSender, false);
						emails.addAll(newEmails);
						
						notification.setReminderSent(true);
						
						dao.saveNotification(notification);
						
						notificationSummary.add(notification);
					}
				}
			}
		}
		if (emails != null && emails.size() > 0) {
			MailUtil.sendMimeMails(emails, mailEngine);

			// notifies instructor/responsible
			notificationSummary.send();
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
	
	
	
	/**
	 * Creates a summary of notifications and sends it to instructor /responsible
	 * @author sa
	 */
	private class NotificationSummary {
		
		Hashtable<Course, Vector<String>> summary = new Hashtable<Course, Vector<String>>();
		
		public NotificationSummary() {}
		
		/**
		 * Creates a notification summary to instructor/responsible
		 */
		private void add(Notification notification) {
			Registration r = notification.getRegistration();
			Course course = r.getCourse();
			
			String waitlist = "";
			if(r.getStatusAsEnum() == Status.WAITING) waitlist = "(" + StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.waitlist")) + ")";
			String name = r.getFirstName() + " " + r.getLastName() + "  <" + r.getEmail() + "> " + waitlist + "\n";
			addToSummary(course, name);
		}

		/**
		 * Sends summary mail(s) to the responsible(s) and the instructor(s) to
		 * be notified with a list of the persons that received notifications
		 */
		public void send() {
			Enumeration<Course> courses = summary.keys();
			while(courses.hasMoreElements()) {
				Course course = courses.nextElement();
				StringBuffer listOfNames = new StringBuffer();
				Vector<String> names = summary.get(course);
				for(int v=0; v<names.size();v++) {
					listOfNames.append(names.get(v));
				}
				
				if(log.isDebugEnabled()) log.debug(listOfNames.toString());
				
				StringBuffer msg = MailUtil.create_EMAIL_EVENT_NOTIFICATION_SUMMARY_body(course, listOfNames.toString(), configurationManager.getConfigurationsMap());
				String subject = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("courseNotification.summarysubject", course.getName()));
				MimeMessage mail = MailUtil.getMailMessage(new String[]{course.getInstructor().getEmail()}, new String[]{course.getResponsible().getEmail()}, null, null, subject, msg, null, null, mailSender);
				mailEngine.send(mail);
			}
		}
		
		private void addToSummary(Course course, String name) {
			Vector<String> notified = summary.get(course);
			if(notified == null) {
				notified = new Vector<String>();
				summary.put(course, notified);
			}
			notified.add(name);
			if(log.isDebugEnabled()) log.debug(name + " added to courseid=" + course.getId() + " in summary");
		}
	}

	
	/**
	 * Comparator for ordering notifications by lastname, firstname
	 * @author sa
	 */
	private class NotificationComparator implements Comparator<Notification> {

		public NotificationComparator() {
			super();
		}

		// order by lastname, firstname
		public int compare(Notification not1, Notification not2) {
			String n1 = not1.getRegistration().getLastName() + not1.getRegistration().getFirstName();
			String n2 = not2.getRegistration().getLastName() + not2.getRegistration().getFirstName();
			return n1.compareTo(n2);
		}
	}
	
}

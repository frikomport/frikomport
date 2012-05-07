/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created January 31st 2006
 */
package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.MailUtil;

import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;


/**
 * Handles everything that has to do with waitinglists
 *
 * @author hrj
 */
public class WaitingListManagerImpl extends BaseManager implements WaitingListManager {
    private RegistrationManager registrationManager;
    private ConfigurationManager configurationManager;
    private CourseManager courseManager;
    protected MailEngine mailEngine = null;
    protected MailSender mailSender = null;
    private MessageSource messageSource = null;
    private Locale locale = null;

    public void executeTask() {
        log.info("running WaitingListManager");
        processEntireWaitingList();
    }

    public void setLocale(Locale locale) {
    	log.debug("Locale: " + locale);
    	if(locale == null) {
    		locale = ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(null);
    	}
        this.locale = locale;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @see no.unified.soak.service.WaitingListManager#setCourseManager(no.unified.soak.service.CourseManager)
     */
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    /**
     * @see no.unified.soak.service.WaitingListManager#setRegistrationManager(no.unified.soak.service.RegistrationManager)
     */
    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
    	this.configurationManager = configurationManager;
    }
    
    /**
     * @see no.unified.soak.service.WaitingListManager#setMailEngine(no.unified.soak.service.MailEngine)
     */
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @see no.unified.soak.service.WaitingListManager#processEntireWaitingList()
     */
    public void processEntireWaitingList() {
        processSingleWaitingList(null, new Boolean(false));
    }

    /**
     * @see no.unified.soak.service.WaitingListManager#processIfNeeded(java.lang.Long)
     */
    public void processIfNeeded(Long courseId, Locale locale) {
        this.locale = locale;

        if ((courseId != null) && (courseId.longValue() != 0)) {
            Date now = new Date();
            Course course = courseManager.getCourse(String.valueOf(courseId));

            if (course.getRegisterBy().before(now)) {
            	// due date of registration is passed
            	// processing waitlist, all registrations treated equal
                processSingleWaitingList(courseId, new Boolean(false));
            }
            else {
            	// course is open for registration
            	boolean localsFirst = getLocalsFirst(course);
            	processSingleWaitingList(course.getId(), localsFirst);
            }
        }
    }

    /**
     * Checks if local registrations have 1st priority, or if all registrations should be treated equal 
     * @return priority
     */
    private boolean getLocalsFirst(Course course) {
    	
        Integer noAttendants = registrationManager.getNumberOfAttendants(false, course);
        if (course.getMaxAttendants().longValue() < noAttendants.longValue()) {
            // So there's a waiting list - let's see if all the local seats are taken
            Integer localAttendants = registrationManager.getNumberOfAttendants(true, course);
            if (localAttendants.longValue() >= course.getReservedInternal().longValue()) {
                // reserved quota is full -- all registration on waitlist are treated equal
            	return false;
            }
            else {
                // reserved quota is NOT full -- local registrations have 1st priority
            	return true;
            }
        }
        else return false; // no waitlist -- all treated equal
    }
    
    /**
     * @see no.unified.soak.service.WaitingListManager#processSingleWaitingList(java.lang.Long,
     *      boolean)
     */
    public void processSingleWaitingList(Long specificCourseId, boolean localsFirst) {
        // Find all courses where
        // 1) now > registerby
        // 2) startdate < now
        List<Course> courses = null;

        if ((specificCourseId == null) || (specificCourseId.longValue() == 0)) {
            courses = courseManager.getWaitingListCourses();
        } else {
            courses = new ArrayList<Course>();
            courses.add(courseManager.getCourse(String.valueOf(specificCourseId)));
        }
//        log.debug("Number of courses found for waitinglist: " + courses.size());

        // Are there any that needs checking?
        if (courses != null) {
            List<Long> courseIds = new ArrayList<Long>();

            // Create a List of all IDs from the course
            for (int i = 0; i < courses.size(); i++) {
                courseIds.add(courses.get(i).getId());
            }

            // Find all reservations on the waitinglist for these courses
            List<Registration> registrations = registrationManager.getWaitingListRegistrations(courseIds);
            Long courseId = new Long(0);
            Course currentCourse = null;
            int availableSeats = 0;
            int availableLocalSeats = 0;
            int taken = 0;
            int lowestPossibleRegistration = 0;

            // Now process the information course by course - registration by
            // registration
            if ((registrations != null) && (registrations.size() > 0)) {
                // This map keeps track of which of the registrations have been
                // used
                HashMap<Long, Boolean> used = new HashMap<Long, Boolean>();

                // Loop over all registrations
                for (int i = 0; i < registrations.size(); i++) {
                    Registration currentRegistration = null;

                    // Is it a a change of course - if it is we have a lot of
                    // variables to update?
                    if (courseId.longValue() != registrations.get(i).getCourseid().longValue()) {
                        currentCourse = registrations.get(i).getCourse();
                        courseId = currentCourse.getId();
                        availableLocalSeats = currentCourse.getReservedInternal() -
                            registrationManager.getNumberOfOccupiedSeats(currentCourse, new Boolean(true)).intValue();

                        Integer takenInt = registrationManager.getNumberOfOccupiedSeats(currentCourse, new Boolean(false));

                        if (takenInt != null) {
                            taken = takenInt.intValue();
                        } else {
                            taken = -1;
                        }

                        // Calculate totally available seats at the course
                        availableSeats = currentCourse.getMaxAttendants() - taken;

                        lowestPossibleRegistration = i;
                    }

                    if (localsFirst) {
                        // Are there vacant seats reserved for local attendants?
                        if (availableLocalSeats > 0) {
                            // Find first unused
                            int j = getNextRegistration(registrations, courseId, currentCourse, lowestPossibleRegistration, used);

                            // If no local was found on the waitinglist for this
                            // course, we give the seat to the first one on the
                            // waiting list regardless of organization
                            if (j >= registrations.size()) {
                                j = lowestPossibleRegistration;

                                while (used.containsKey(new Long(j)))
                                    j++;

                                used.put(new Long(j), new Boolean(true));
                                currentRegistration = registrations.get(j);
                            } else {
                                // Get our local, and then make sure he isnt picked again this run
                                currentRegistration = registrations.get(j);
                                availableLocalSeats--;
                                used.put(new Long(j), new Boolean(true));
                            }
                        } else {
                            // No seats left reserved for the locals. So we're
                            // using first come - first serve for the rest of this course
                            int j = lowestPossibleRegistration;

                            while (used.containsKey(new Long(j)))
                                j++;

                            used.put(new Long(j), new Boolean(true));
                            currentRegistration = registrations.get(j);
                        }
                    } else {
                        // First come - first serve!
                        currentRegistration = registrations.get(i);
                        used.put(new Long(i), new Boolean(true));
                    }

                    availableSeats = saveReservation(availableSeats, currentRegistration);
                }
            }
        }
    }

    /**
     * If there are available seats, we move the applicant from the waiting list
     * to the confirmed list
     *
     * @param availableSeats
     *            Number of available seats
     * @param currentRegistration
     *            The registration we (might) want to persist changes to
     */
	private int saveReservation(int availableSeats, Registration currentRegistration) {
		// If there are more seats available, we reserve a seat for
		// this one
		if (availableSeats >= currentRegistration.getParticipants().intValue()) {
			currentRegistration.setStatus(Registration.Status.RESERVED);
			registrationManager.saveRegistration(currentRegistration);
			availableSeats = availableSeats - currentRegistration.getParticipants().intValue();

			notifyAttendant(true, currentRegistration);
		}

		return availableSeats;
	}

    /**
     * Sends an e-mail to the attendant informing about the status of the application to a course.
     *
     * @param confirmed Is the reservation confirmed (true), or is the applicant bumped to the waiting list (false)
     * @param currentRegistration The registration in question
     */
    private void notifyAttendant(boolean confirmed, Registration currentRegistration) {
//    	log.debug("Sending mail from WaitingListManager");
    	// Get the course we are notifying the attendant about
    	Course course = currentRegistration.getCourse();
    	
    	// Create the body of the e-mail
//    	StringBuffer msg = MailUtil.createStandardBody(course, Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION, locale, messageSource, null, confirmed);
    	StringBuffer msg = MailUtil.create_EMAIL_EVENT_WAITINGLIST_NOTIFICATION_body(course, currentRegistration, null, confirmed, configurationManager.getConfigurationsMap());

    	// Create the email
    	ArrayList<MimeMessage> theEmails = MailUtil.getMailMessages(currentRegistration, Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION, course, msg, mailSender, false);
    	
    	// Send the email
    	MailUtil.sendMimeMails(theEmails, mailEngine);
    }

    /**
    * Finds the next registration to process. This function is used only when
    * locals have priority.
    *
    * @param registrations
    *            All registrations
    * @param courseId
    *            The current courseId
    * @param currentCourse
    *            The current course
    * @param lowestPossibleRegistration
    *            The lowest index in the registrationsarray that has not been
    *            used
    * @param used
    *            A map that keeps track of which of the registrations that have
    *            been processed
    * @return Index of the next registration
    */
    private int getNextRegistration(List<Registration> registrations,
        Long courseId, Course currentCourse, int lowestPossibleRegistration,
        HashMap<Long, Boolean> used) {
        int j = lowestPossibleRegistration;
        boolean ok = true;

        while (ok) {
            // If we have looked through the entire registrationsarray, we call
            // it the quits
            if (j >= registrations.size()) {
                ok = false;
            } else {
                // Is the registrations we're looking at still for the same
                // course, if it isn't - we call it the quits
                if (registrations.get(j).getCourseid().longValue() == courseId.longValue()) {
                    // So far - so good. Let's see if the attendant is a "local"
                    if (registrations.get(j).getOrganizationid() != null 
                    		&& registrations.get(j).getOrganizationid().longValue() == currentCourse.getOrganizationid().longValue()) {
                        // We got ourselves a local attendant. Now let's see if
                        // we've used this one earlier
                        if (!used.containsKey(new Long(j))) {
                            // Everything A-OK! We have got our local!
                            ok = false;
                        }
                    }
                } else {
                    // We have looked through all registrations for the current
                    // course and found no locals, so we give up
                    ok = false;
                    j = registrations.size() + 1;
                }
            }

            if (ok) {
                j++;
            }
        }

        return j;
    }

	public void sayHello() {
		log.debug("HELLO!!!");
		// TODO Auto-generated method stub
	}
}

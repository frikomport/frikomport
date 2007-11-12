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

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.util.MailUtil;

import org.springframework.context.MessageSource;

import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Handles everything that has to do with waitinglists
 *
 * @author hrj
 */
public class WaitingListManagerImpl extends BaseManager
    implements WaitingListManager {
    private RegistrationManager registrationManager;
    private CourseManager courseManager;
    protected MailEngine mailEngine = null;
    private MessageSource messageSource = null;
    private Locale locale = null;

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

    /**
     * @see no.unified.soak.service.WaitingListManager#setMailEngine(no.unified.soak.service.MailEngine)
     */
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    /**
     * @see no.unified.soak.service.WaitingListManager#processEntireWaitingList()
     */
    public void processEntireWaitingList(Locale locale) {
        this.locale = locale;
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

            // Are we allowed to make changes at all now
            if (!course.getFreezeAttendance().before(now)) {
                // Are we in the periode before freeze but after the date that
                // stops manual registrations?
                // In that case "first come, first serve" is the rule
                if (course.getRegisterBy().before(now)) {
                    processSingleWaitingList(courseId, new Boolean(false));
                } else // In this case we need to prioritize the local attendants on a
                       // waitinglist
                 {
                    // First check if there IS a waiting list
                    Integer noAttendants = registrationManager.getNumberOfAttendants(new Boolean(
                                false), course);

                    if (course.getMaxAttendants().longValue() < noAttendants.longValue()) {
                        // So there's a waiting list - let's see if all the
                        // local seats are taken
                        Integer localAttendants = registrationManager.getNumberOfAttendants(true,
                                course);

                        if (localAttendants.longValue() >= course.getReservedMunicipal()
                                                                     .longValue()) {
                            processSingleWaitingList(courseId, false);
                        } else {
                            // We need to prioritize locals
                            processSingleWaitingList(courseId, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * @see no.unified.soak.service.WaitingListManager#processSingleWaitingList(java.lang.Long,
     *      boolean)
     */
    public void processSingleWaitingList(Long specificCourseId,
        boolean localsFirst) {
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
                    if (courseId.longValue() != registrations.get(i)
                                                                 .getCourseid()
                                                                 .longValue()) {
                        currentCourse = registrations.get(i).getCourse();
                        courseId = currentCourse.getId();
                        availableLocalSeats = currentCourse.getReservedMunicipal() -
                            registrationManager.getNumberOfOccupiedSeats(currentCourse,
                                new Boolean(true)).intValue();

                        Integer takenInt = registrationManager.getNumberOfOccupiedSeats(currentCourse,
                                new Boolean(false));

                        if (takenInt != null) {
                            taken = takenInt.intValue();
                        } else {
                            taken = -1;
                        }

                        // Calculate totally available seats at the course
                        availableSeats = currentCourse.getMaxAttendants() -
                            taken;

                        lowestPossibleRegistration = i;
                    }

                    if (localsFirst) {
                        // Are there vacant seats reserved for local attendants?
                        if (availableLocalSeats > 0) {
                            // Find first unused
                            int j = getNextRegistration(registrations,
                                    courseId, currentCourse,
                                    lowestPossibleRegistration, used);

                            // If no local was found on the waitinglist for this
                            // course, we give the seat to the first one on the
                            // waiting list regardless of municipality
                            if (j >= registrations.size()) {
                                j = lowestPossibleRegistration;

                                while (used.containsKey(new Long(j)))
                                    j++;

                                used.put(new Long(j), new Boolean(true));
                                currentRegistration = registrations.get(j);
                            } else {
                                // Get our local, and then make sure he isnt
                                // picked again this run
                                currentRegistration = registrations.get(j);
                                availableLocalSeats--;
                                used.put(new Long(j), new Boolean(true));
                            }
                        } else {
                            // No seats left reserved for the locals. So we're
                            // using first come - first serve for the rest of
                            // this course
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

                    availableSeats = saveReservation(availableSeats,
                            currentRegistration);
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
    private int saveReservation(int availableSeats,
        Registration currentRegistration) {
        // If there are more seats avaiable, we reserve a seat for
        // this one
        if (availableSeats > 0) {
            currentRegistration.setReserved(true);
            registrationManager.saveRegistration(currentRegistration);
            availableSeats--;

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
    private void notifyAttendant(boolean confirmed,
        Registration currentRegistration) {
//    	log.debug("Sending mail from WaitingListManager");
    	// Get the course we are notifying the attendant about
    	Course course = currentRegistration.getCourse();
    	
    	// Create the body of the e-mail
    	StringBuffer msg = MailUtil.createStandardBody(course, Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION, locale, messageSource, null, confirmed);

    	// Create the email
    	ArrayList<SimpleMailMessage> theEmails = MailUtil.setMailInfo(currentRegistration, Constants.EMAIL_EVENT_WAITINGLIST_NOTIFICATION, course, msg, messageSource, locale);
    	
    	// Send the email
    	MailUtil.sendMails(theEmails, mailEngine);
    	
//        SimpleMailMessage theMessage = new SimpleMailMessage();
//        theMessage.setFrom(StringEscapeUtils.unescapeHtml(
//                messageSource.getMessage("mail.default.from", null, locale)));
//
//        ArrayList<String> recipients = new ArrayList<String>();
//
//        if (!StringUtils.isEmpty(currentRegistration.getEmail())) {
//            recipients.add(currentRegistration.getEmail());
//        }
//
//        if (recipients.size() > 0) {
//            String[] recipientArray = new String[recipients.size()];
//
//            for (int i = 0; i < recipients.size(); i++) {
//                recipientArray[i] = recipients.get(i);
//            }
//
//            theMessage.setTo(recipientArray);
//        } else {
//            theMessage.setTo(currentRegistration.getCourse().getInstructor()
//                                                .getEmail());
//        }
//
//        StringBuffer msg = new StringBuffer();
//        String identifier = currentRegistration.getFirstName() + " " +
//            currentRegistration.getLastName();
//
//        if ((currentRegistration.getEmployeeNumber() != null) &&
//                (currentRegistration.getEmployeeNumber().intValue() != 0)) {
//            identifier = identifier + " (" +
//                StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                        "registration.employeeNumber", null, locale))
//                                 .toLowerCase() + " " +
//                currentRegistration.getEmployeeNumber().intValue() + ")";
//        }
//
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "misc.hello", null, locale)) + " " + identifier + "!\n\n");
//        if (confirmed) {
//            theMessage.setSubject(StringEscapeUtils.unescapeHtml(
//                    messageSource.getMessage(
//                        "registrationComplete.mail.subject",
//                        new String[] { currentRegistration.getCourse().getName() },
//                        locale)));
//            msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                        "registrationComplete.mail.body", null, locale)));
//        } else {
//            theMessage.setSubject(StringEscapeUtils.unescapeHtml(
//                    messageSource.getMessage(
//                        "registrationToWaitinglist.mail.subject",
//                        new String[] { currentRegistration.getCourse().getName() },
//                        locale)));
//        }
//
//        msg.append("\n\n");
//
//        // Include course details
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.name", null, locale)) + ": " +
//            currentRegistration.getCourse().getName() + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.type", null, locale)) + ": " +
//            currentRegistration.getCourse().getType() + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.startTime", null, locale)) + ": " +
//            DateUtil.getDateTime(messageSource.getMessage("date.format", null,
//                    locale) + " " +
//                messageSource.getMessage("time.format", null, locale),
//                currentRegistration.getCourse().getStartTime()) + "\n");
//
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.stopTime", null, locale)) + ": " +
//            DateUtil.getDateTime(messageSource.getMessage("date.format", null,
//                    locale) + " " +
//                messageSource.getMessage("time.format", null, locale),
//                currentRegistration.getCourse().getStopTime()) + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.duration", null, locale)) + ": " +
//            currentRegistration.getCourse().getDuration() + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.municipality", null, locale)) + ": " +
//            currentRegistration.getCourse().getMunicipality().getName() + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.serviceArea", null, locale)) + ": " +
//            currentRegistration.getCourse().getServiceArea().getName() + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.location", null, locale)) + ": " +
//            currentRegistration.getCourse().getLocation().getName() + "\n");
//
//        if (currentRegistration.getCourse().getResponsible() != null) {
//            msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                        "course.responsible", null, locale)) + ": " +
//                currentRegistration.getCourse().getResponsible().getName() +
//                "\n");
//        }
//
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.instructor", null, locale)) + ": " +
//            currentRegistration.getCourse().getInstructor().getName() + "\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "course.description", null, locale)) + ": " +
//            currentRegistration.getCourse().getDescription() + "\n");
//
//        // We cannot link to a deleted course, so the link is only displayed if
//        // the course still exists
//        String baseurl = StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "javaapp.baseurl", null, locale));
//        String coursedetailurl = StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "javaapp.coursedetailurl",
//                    new String[] {
//                        String.valueOf(currentRegistration.getCourse().getId())
//                    }, locale));
//        msg.append("\n\n");
//        msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                    "javaapp.findurlhere", null, locale)) + " " + baseurl +
//            coursedetailurl);
//
//        msg.append("\n\n");
//
//        if (confirmed) {
//            msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                        "registrationComplete.mail.footer", null, locale)));
//        } else {
//            msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage(
//                        "registrationToWaitinglist.mail.footer",
//                        new String[] {
//                            " " + currentRegistration.getCourse().getName() +
//                            "\n\n"
//                        }, locale)));
//        }
//
//        msg.append("\n\n");
//		msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage("mail.contactinfo", null, 
//				locale))
//				+ "\n");
//		msg.append(StringEscapeUtils.unescapeHtml(messageSource.getMessage("mail.donotreply",
//				new String[] {messageSource.getMessage("mail.default.from", null, locale)}, locale))
//				+ "\n");
//
//        theMessage.setText(msg.toString());
//        mailEngine.send(theMessage);
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
                    if (registrations.get(j).getMunicipalityid().longValue() == currentCourse.getMunicipalityid()
                                                                                                 .longValue()) {
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

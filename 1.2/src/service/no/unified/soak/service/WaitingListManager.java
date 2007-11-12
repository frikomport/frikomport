/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created February 4th 2006
 */
package no.unified.soak.service;

import java.util.Locale;


/**
 * Interface for functions dealing with the waiting list
 *
 * @author hrj
 */
public interface WaitingListManager extends Manager {
    /**
     * Set the messagesource - used to access resourcemessages
     * @param messageSource
     */

    //    public void setMessageSource(MessageSource messageSource);

    /**
     * Setter for mailEngine - used for sending e-mails
     * @param mailEngine
     */
    public void setMailEngine(MailEngine mailEngine);

    /**
    * Setter for DAO, convenient for unit testing
    */
    public void setRegistrationManager(RegistrationManager registrationManager);

    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setCourseManager(CourseManager courseManager);

    /**
     * Examines and processes the waiting list of all courses that are in the
     * period of time where registration is no longer available, but attendance
     * has not yet been frozen.
     */
    public void processEntireWaitingList(Locale locale);

    /**
     * This function checks whether or not there is a point of running the
     * waitinglist-process. Typically run in situations where a registration has
     * been deleted or something similar
     *
     * @param courseId
     *            the affected course
     */
    public void processIfNeeded(Long courseId, Locale locale);

    /**
     * Examines and processes a specifics courses waitinglist - moving
     * applicants from the waiting list to the confirmed list according to set
     * rules.
     *
     * @param specificCourseId
     *            If the waitinglist should be processed for one specific course
     *            the ID can be given her.
     * @param localsFirst
     *            If the process is to put local applicants before "foreign"
     *            ones, this should be set to true.
     */
    public void processSingleWaitingList(Long specificCourseId,
        boolean localsFirst);
    
    public void sayHello();
}

/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Know IT Objectnet AS
*/
/*
 * Created November 26 2009
 */
package no.unified.soak.service;

/**
 * Interface for functions dealing with the registerBy date
 *
 * @author sa
 */
public interface RegisterByDateManager extends Task {
    /**
     * Setter for mailEngine - used for sending e-mails
     * @param mailEngine
     */
    public void setMailEngine(MailEngine mailEngine);

    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setCourseManager(CourseManager courseManager);

    /**
     * Setter for DAO, convenient for unit testing
     */
     public void setRegistrationManager(RegistrationManager registrationManager);

     public void setConfigurationManager(ConfigurationManager configurationManager);

    /**
     * Perform actions if a Course' registerBy date is due
     */
    public void checkRegisterByDates();
}

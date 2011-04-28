/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * created 14. dec 2005
 */
package no.unified.soak.dao;

import no.unified.soak.model.Person;

import java.util.List;


/**
 * User Data Access Object (DAO) interface.
 *
 * @author hrj
 */
public interface PersonDAO extends DAO {
    /**
     * Retrieves all of the persons
         * @param includeDisabled
         *            set to 'true' to include the disabled ones
     */
    public List getPersons(Person person, Boolean includeDisabled);

    /**
     * Gets person's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the person's id
     * @return person populated person object
     */
    public Person getPerson(final Long id);

    /**
     * Saves a person's information
     * @param person the object to be saved
     */
    public void savePerson(Person person);

    /**
    * Removes a person from the database by id
    * @param id the person's id
    */
    public void removePerson(final Long id);
}

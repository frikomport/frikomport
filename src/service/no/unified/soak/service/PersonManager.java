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
package no.unified.soak.service;

import java.util.List;

import no.unified.soak.dao.PersonDAO;
import no.unified.soak.model.Person;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface PersonManager extends Manager {
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setPersonDAO(PersonDAO personDAO);

    /**
     * Retrieves all of the persons
     */
    public List getPersons(Person person, Boolean includeDisabled);


    /**
     * Gets person's information based on id.
     *
     * @param id
     *            the person's id
     * @return person populated person object
     */
    public Person getPerson(final String id);

    /**
     * Saves a person's information
     *
     * @param person
     *            the object to be saved
     */
    public void savePerson(Person person);

    /**
     * Removes a person from the database by id
     *
     * @param id
     *            the person's id
     */
    public void removePerson(final String id);
}

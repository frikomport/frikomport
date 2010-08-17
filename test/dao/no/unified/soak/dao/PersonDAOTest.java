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

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Test of the PersonDAO class
 *
 * @author hrj
 */
public class PersonDAOTest extends BaseDAOTestCase {
    private Long personId = new Long("1");
    private Person person = null;
    private PersonDAO dao = null;

    public void setPersonDAO(PersonDAO dao) {
        this.dao = dao;
    }

    /**
     * Tests the add new Person functionality.
     * 1) Set required fields
     * 2) Save the new Person
     * 3) Verify that it has been saved correctly
     *
     * @throws Exception
     */
    public void testAddPerson() throws Exception {
        person = new Person();

        // set required fields
        String email = "hans.hansen@hansen.nn";
        person.setEmail(email);

        String name = "Hans Hansen";
        person.setName(name);

        Boolean selectable = new Boolean("true");
        person.setSelectable(selectable);

        dao.savePerson(person);

        // verify a primary key was assigned
        assertNotNull(person.getId());

        // verify set fields are same after save
        assertEquals(email, person.getEmail());
        assertEquals(name, person.getName());
        assertEquals(selectable, person.getSelectable());
    }

    /**
     * Tests getting an existing Person
     * Prerequisite: A Person with Id = 1 exists in the database
     *
     * @throws Exception
     */
    public void testGetPerson() throws Exception {
        person = dao.getPerson(personId);
        assertNotNull(person);
    }

    /**
     * Tests getting a list of all Persons (or people if you will) in the database
     * Prerequisite: A minimum of one person must exist in the datbase
          * minimum of one selectable Person must exist in the database
     *
     * @throws Exception
     */
    public void testGetPersons() throws Exception {
        person = new Person();

        List results = dao.getPersons(person, new Boolean(false));
        assertTrue(results.size() > 0);
    }

    /**
     * Tests the persistance of a Person to the databsae
     * 1) Set required fields
     * 2) Save it
     * 3) Verify that it has been stored correctly
     *
     * @throws Exception
     */
    public void testSavePerson() throws Exception {
        person = dao.getPerson(personId);

        // update required fields
        String email = "per.persen@persen.pp";
        person.setEmail(email);

        String name = "Per Persen";
        person.setName(name);

        Boolean selectable = new Boolean("true");
        person.setSelectable(selectable);

        dao.savePerson(person);

        assertEquals(email, person.getEmail());
        assertEquals(name, person.getName());
        assertEquals(selectable, person.getSelectable());
    }

    /**
     * Tests the removal of an existing Person
     * Prerequisite: A Person with id = 3 must exist in the database
     *
     * 1) Remove Person
     * 2) Try to retrieve the (hopefully) deleted object from the db
     * 3) If Exception gotten => Object not found => Everything is ok!
     *
     * @throws Exception
     */
    public void testRemovePerson() throws Exception {
        Long removeId = new Long("3");
        dao.removePerson(removeId);

        try {
            dao.getPerson(removeId);
            fail("person found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
    }
}

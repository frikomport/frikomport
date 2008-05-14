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

import no.unified.soak.dao.PersonDAO;
import no.unified.soak.model.Person;
import no.unified.soak.service.impl.PersonManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for PersonManager
 *
 * @author hrj
 */
public class PersonManagerTest extends BaseManagerTestCase {
    private final String personId = "1";
    private PersonManager personManager = new PersonManagerImpl();
    private Mock personDAO = null;
    private Person person = null;

    protected void setUp() throws Exception {
        super.setUp();
        personDAO = new Mock(PersonDAO.class);
        personManager.setPersonDAO((PersonDAO) personDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        personManager = null;
    }

    /**
     * Tests getting a list of all Persons (people) in the database using a mock
     * DAO
     *
     * @throws Exception
     */
    public void testGetPersons() throws Exception {
        List results = new ArrayList();
        person = new Person();
        results.add(person);

        // set expected behavior on dao
        personDAO.expects(once()).method("getPersons").will(returnValue(results));

        List persons = personManager.getPersons(null, new Boolean(false));
        assertTrue(persons.size() == 1);
        personDAO.verify();
    }

    /**¨
     * Tests getting a single person using a mock DAO
     *
     * @throws Exception
     */
    public void testGetPerson() throws Exception {
        // set expected behavior on dao
        personDAO.expects(once()).method("getPerson")
                 .will(returnValue(new Person()));
        person = personManager.getPerson(personId);
        assertTrue(person != null);
        personDAO.verify();
    }

    /**
     * Tests persisting a single Person using a mock DAO
     * @throws Exception
     */
    public void testSavePerson() throws Exception {
        // set expected behavior on dao
        personDAO.expects(once()).method("savePerson").with(same(person))
                 .isVoid();

        personManager.savePerson(person);
        personDAO.verify();
    }

    /**
     * Tests adding and then removing a Person using a mock DAO
     *
     * 1) Set the required fields 2) "Save" it 3) "Remove it" 4) Check that
     * everything went according to plan
     *
     * @throws Exception
     */
    public void testAddAndRemovePerson() throws Exception {
        person = new Person();

        // set required fields
        person.setEmail("marit.maritsen@ikkemail.nn");
        person.setName("Marit Maritsen");

        // set expected behavior on dao
        personDAO.expects(once()).method("savePerson").with(same(person))
                 .isVoid();
        personManager.savePerson(person);
        personDAO.verify();

        // reset expectations
        personDAO.reset();

        personDAO.expects(once()).method("removePerson")
                 .with(eq(new Long(personId)));
        personManager.removePerson(personId);
        personDAO.verify();

        // reset expectations
        personDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Person.class,
                person.getId());
        personDAO.expects(once()).method("removePerson").isVoid();
        personDAO.expects(once()).method("getPerson").will(throwException(ex));
        personManager.removePerson(personId);

        try {
            personManager.getPerson(personId);
            fail("Person with identifier '" + personId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        personDAO.verify();
    }
}

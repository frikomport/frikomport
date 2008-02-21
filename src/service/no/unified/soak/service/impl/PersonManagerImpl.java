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
package no.unified.soak.service.impl;

import no.unified.soak.dao.PersonDAO;
import no.unified.soak.dao.jdbc.IUserDaoJdbc;
import no.unified.soak.ext.IUser;
import no.unified.soak.model.Person;
import no.unified.soak.service.PersonManager;

import java.util.List;


/**
 * Implementation of PersonManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class PersonManagerImpl extends BaseManager implements PersonManager {
    private PersonDAO dao;
    private IUserDaoJdbc userEzDaoJdbc = null;
    
    public void setUserEzDaoJdbc(IUserDaoJdbc userEzDaoJdbc)
    {
        this.userEzDaoJdbc = userEzDaoJdbc;
    }

    /**
     * Set the DAO for communication with the data layer.
     *
     * @param dao
     */
    public void setPersonDAO(PersonDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.PersonManager#getPersons(no.unified.soak.model.Person)
     */
    public List getPersons(final Person person, final Boolean includeDisabled) {
        return dao.getPersons(person, includeDisabled);
    }

    /**
     * @see no.unified.soak.service.PersonManager#getPerson(String id)
     */
    public Person getPerson(final String id) {
        return dao.getPerson(new Long(id));
    }

    /**
     * @see no.unified.soak.service.PersonManager#savePerson(Person person)
     */
    public void savePerson(Person person) {
        dao.savePerson(person);
    }

    /**
     * @see no.unified.soak.service.PersonManager#removePerson(String id)
     */
    public void removePerson(final String id) {
        dao.removePerson(new Long(id));
    }

    public List getEZResponsibles(IUser user) {
        List users = userEzDaoJdbc.findKursansvarligeUser();

        return users;
    }
    
    public List getEZRoles() {
        List roles = userEzDaoJdbc.findRoles();
        return roles;
    }
}

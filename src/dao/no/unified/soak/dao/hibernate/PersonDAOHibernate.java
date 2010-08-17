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
package no.unified.soak.dao.hibernate;

import java.util.List;

import no.unified.soak.dao.PersonDAO;
import no.unified.soak.model.Person;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;


/**
 * Implementations of the PersonDAO
 *
 * @author hrj
 */
public class PersonDAOHibernate extends BaseDAOHibernate implements PersonDAO {
    /**
     * @see no.unified.soak.dao.PersonDAO#getPersons(no.unified.soak.model.Person)
     */
    public List getPersons(final Person person, final Boolean includeDisabled) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Person.class);

        // If the includeDisabled is not true, we only return enabled persons
        if (!includeDisabled.booleanValue()) {
            criteria.add(Restrictions.eq("selectable", new Boolean("true")));
        }

        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * @see no.unified.soak.dao.PersonDAO#getPerson(Long id)
     */
    public Person getPerson(final Long id) {
        Person person = (Person) getHibernateTemplate().get(Person.class, id);

        if (person == null) {
            log.warn("uh oh, person with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Person.class, id);
        }

        return person;
    }

    /**
     * @see no.unified.soak.dao.PersonDAO#savePerson(Person person)
     */
    public void savePerson(final Person person) {
        getHibernateTemplate().saveOrUpdate(person);
    }

    /**
     * @see no.unified.soak.dao.PersonDAO#removePerson(Long id)
     */
    public void removePerson(final Long id) {
        getHibernateTemplate().delete(getPerson(id));
    }
}

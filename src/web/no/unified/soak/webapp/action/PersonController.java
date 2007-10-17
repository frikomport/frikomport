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
package no.unified.soak.webapp.action;

import no.unified.soak.Constants;
import no.unified.soak.model.Person;
import no.unified.soak.service.PersonManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controller class for Person. Creates a view with a list of all
 * Persons (people).
 *
 * @author hrj
 */
public class PersonController implements Controller {
    private final Log log = LogFactory.getLog(PersonController.class);
    private PersonManager personManager = null;

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        Person person = new Person();
        // populate object with request parameters
        BeanUtils.populate(person, request.getParameterMap());

        List<Person> persons = getPersons();

        return new ModelAndView("personList", Constants.PERSON_LIST, persons);
    }

    /**
     * Gets the list of persons
     * @return A list of persons
     */
    private List<Person> getPersons() {
        // Get all persons
        List<Person> persons = personManager.getPersons(null, new Boolean(true));
        return persons;
    }
}

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import no.unified.soak.Constants;
import no.unified.soak.model.Person;
import no.unified.soak.model.User;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Controller class for Person. Creates a view with a list of all
 * Persons (people).
 *
 * @author hrj
 */
public class PersonController extends BaseFormController {
    private final Log log = LogFactory.getLog(PersonController.class);
    private PersonManager personManager = null;

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }
    


//    /**
//     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
//     */
//    public ModelAndView handleRequest(HttpServletRequest request,
//        HttpServletResponse response) throws Exception {
//        if (log.isDebugEnabled()) {
//            log.debug("entering 'handleRequest' method...");
//        }
//
//        Person person = new Person();
//        // populate object with request parameters
//        BeanUtils.populate(person, request.getParameterMap());
//
//        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    	User user = (User) 
//    	List roles = user.getRoleNameList();
//
//    	List<Person> persons = getPersons(roles);
//
//        return new ModelAndView("personList", Constants.PERSON_LIST, persons);
//    }
    
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap();
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method...");
        }
        User user = getUser(request);
        List roles = user.getRoleNameList();

        List<Person> persons = getPersons(roles);
        model.put("personList", persons);
        
        return model;
    }

    /**
    * Gets the list of persons
     * @param List of roles
     * @return List of persons based on users role
     */
    private List<Person> getPersons(List roles) {
        // Anonymous / empoyees does not return disabled users 
    	Boolean includeDisabledUsers = new Boolean(true);
    	
    	Iterator it = roles.listIterator();
    	while(it.hasNext()) {
    		String roleName = (String)it.next();
    		if(Constants.ANONYMOUS_ROLE.equalsIgnoreCase(roleName) || Constants.EMPLOYEE_ROLE.equalsIgnoreCase(roleName)) {
    			includeDisabledUsers = new Boolean(false);
    			break;
    		}
    	}
    	
        List<Person> persons = personManager.getPersons(null, includeDisabledUsers);
        return persons;
    }
}

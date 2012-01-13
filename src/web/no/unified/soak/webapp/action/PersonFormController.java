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

import no.unified.soak.model.Person;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.CourseStatus;

import org.apache.commons.lang.StringUtils;

import org.springframework.validation.BindException;

import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of SimpleFormController that interacts with the
 * PersonManager to retrieve/persist values to the database.
 *
 * @author hrj
 */
public class PersonFormController extends BaseFormController {
    private PersonManager personManager = null;
    private CourseManager courseManager = null;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String id = request.getParameter("id");
        Person person = null;

        if (!StringUtils.isEmpty(id)) {
            person = personManager.getPerson(id);
        } else {
            person = new Person();
        }

        return person;
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<String, List> parameters = new HashMap<String, List>();

        Integer[] coursestatus = null;
        if(ApplicationResourcesUtil.isSVV()){
        	coursestatus = new Integer[]{CourseStatus.COURSE_CREATED, CourseStatus.COURSE_PUBLISHED, CourseStatus.COURSE_FINISHED};
        }
        List courses = courseManager.findByInstructor((Person)formBackingObject(request), coursestatus);
        if(courses != null){
            parameters.put("courseList",courses);
        }

        return parameters;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Person person = (Person) command;
        boolean isNew = (person.getId() == null);
        Locale locale = request.getLocale();

        // Are we to return to the list?
        if (request.getParameter("return") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'return' from jsp");
            }
        } // or to delete?
        else if (request.getParameter("delete") != null) {
            personManager.removePerson(person.getId().toString());

            saveMessage(request, getText("person.deleted", locale));
        } else {
        	// person is new or updated
        	
        	// validering flyttet fra klient til kontroller
			if (validateAnnotations(person, errors, null) > 0) {
				personManager.evict(person);
				return showForm(request, response, errors);
			}
        	// ---

            personManager.savePerson(person);

            String key = (isNew) ? "person.added" : "person.updated";
            saveMessage(request, getText(key, locale));

            if (!"list".equals(request.getAttribute("from"))) {
                return new ModelAndView("redirect:detailsPerson.html", "id",  person.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}

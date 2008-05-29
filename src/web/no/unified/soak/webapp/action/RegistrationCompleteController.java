/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 09.jan.2006
 */
package no.unified.soak.webapp.action;

import no.unified.soak.model.Registration;
import no.unified.soak.model.Course;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.CourseManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;


/**
 * Let's the applicant know the status of the registration
 *
 * @author hrj
 */
public class RegistrationCompleteController extends BaseFormController {
    protected final transient Log log = LogFactory.getLog(getClass());
    private RegistrationManager registrationManager = null;
    private CourseManager courseManager = null;

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setCourseManager(CourseManager courseManager){
        this.courseManager = courseManager;
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map model = new HashMap();
        Locale locale = request.getLocale();

        Registration reg = (Registration)formBackingObject(request);

        // Retrieve the course the user wants to attend
		Course course = courseManager.getCourse(""+reg.getCourseid());
		if (course != null) {
			model.put("course", course);
		}

        return model;
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String regid = request.getParameter("registrationid");
        Registration registration = new Registration();

        if (regid == null) {
            // ERROR - should never happen
        } else {
            registration = registrationManager.getRegistration(regid);
        }

        return registration;
    }
}

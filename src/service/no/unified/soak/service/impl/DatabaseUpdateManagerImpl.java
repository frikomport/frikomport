package no.unified.soak.service.impl;

import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.Constants;

import java.util.Locale;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import org.springframework.validation.Validator;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.apache.commons.validator.EmailValidator;

/**
 * User: gv
 * Date: 05.jun.2008
 * Time: 10:26:23
 */
public class DatabaseUpdateManagerImpl extends BaseManager implements DatabaseUpdateManager {

    private CourseManager courseManager = null;
    private RegistrationManager registrationManager = null;
    private UserManager userManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void updateDatabase() {
        // Course updates
        updateResponsibleUsername();
        updateRolename();
        updateRestrictions();
        // Registrations
        updateRegistrations();
    }

    /**
     * Checks the registrations table and creates users based on email addresses
     */
    private void updateRegistrations() {
        List<Registration> registrations = registrationManager.getRegistrations(new Registration());
        if(registrations != null && !registrations.isEmpty()){
            Iterator<Registration> it = registrations.iterator();
            while (it.hasNext()){
                Registration registration = it.next();
                User admin = userManager.getUser("admin");
                // Only use valid emails
                if(registration.getUsername() == null || registration.getUsername().trim().length() == 0){
                    if(EmailValidator.getInstance().isValid(registration.getEmail())){
                        User user = userManager.findUser(registration.getEmail());
                        if(user == null){
                            user = userManager.addUser(registration.getEmail(), registration.getFirstName(), registration.getLastName(), registration.getEmail(), new Integer(0), null, new Integer(0));
                        }
                        // Connect user with registration
                        registration.setUser(user);
                        registration.setUsername(user.getUsername());
                        registrationManager.saveRegistration(registration);
                    }
                    else {
                        // Connect with admin user
                        registration.setUser(admin);
                        registration.setUsername(admin.getUsername());
                        registrationManager.saveRegistration(registration);
                    }
                }
            }
        }
    }

    private void updateRolename() {
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(course.getRole().equals("Anonymous")){
                    course.setRole(Constants.ANONYMOUS_ROLE);
                } else if(course.getRole().equals("Ansatt")){
                    course.setRole(Constants.EMPLOYEE_ROLE);
                } else if(course.getRole().equals("Kommuneansatt")){
                    course.setRole(Constants.EMPLOYEE_ROLE);
                } else if(course.getRole().equals("Kursansvarlig")){
                    course.setRole(Constants.INSTRUCTOR_ROLE);
                } else if(course.getRole().equals("Opplaringsansvarlig")){
                    course.setRole(Constants.EDITOR_ROLE);
                } else if(course.getRole().equals("Oppl�ringsansvarlig")){
                    course.setRole(Constants.EDITOR_ROLE);
                } else if(course.getRole().equals("Admin")){
                    course.setRole(Constants.ADMIN_ROLE);
                }
                courseManager.saveCourse(course);
            }
        }
    }

    private void updateResponsibleUsername() {
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(course.getResponsible() != null && course.getResponsibleUsername() == null){
                    course.setResponsibleUsername(course.getResponsible().getUsername());
                    courseManager.saveCourse(course);
                }
            }
        }
    }

    private void updateRestrictions(){
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(course.getRestricted() == null){
                    course.setRestricted(false);
                    courseManager.saveCourse(course);
                }
            }
        }
    }

    public void executeTask() {
        log.info("running databaseUpdateManager");
        updateDatabase();
    }

    public void setLocale(Locale locale) {
        // Do nothing
    }
}

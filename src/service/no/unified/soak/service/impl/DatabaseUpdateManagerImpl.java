package no.unified.soak.service.impl;

import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.model.Course;
import no.unified.soak.Constants;

import java.util.Locale;
import java.util.List;
import java.util.Iterator;

/**
 * User: gv
 * Date: 05.jun.2008
 * Time: 10:26:23
 */
public class DatabaseUpdateManagerImpl extends BaseManager implements DatabaseUpdateManager {

    private CourseManager courseManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void updateDatabase() {
        // Course updates
        updateResponsibleUsername();
        updateRolename();

        // Registrations... make users based on registrations
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
                } else if(course.getRole().equals("Opplæringsansvarlig")){
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

    public void executeTask() {
        log.info("running courseUpdateManager");
        updateDatabase();
    }

    public void setLocale(Locale locale) {
        // Do nothing
    }
}

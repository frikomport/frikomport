package no.unified.soak.service.impl;

import no.unified.soak.service.CourseStatusManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.model.Course;
import no.unified.soak.util.CourseStatus;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 03.jun.2008
 * Time: 11:51:59
 * To change this template use File | Settings | File Templates.
 */
public class CourseStatusManagerImpl extends BaseManager implements CourseStatusManager {

    private CourseManager courseManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void processCourses(){
        Course search = new Course();
        // Get only published courses
        search.setStatus(CourseStatus.COURSE_PUBLISHED);
        List<Course> courses = courseManager.searchCourses(search,null,null);
        Iterator<Course> it = courses.iterator();
        while (it.hasNext()){
            Course course = it.next();
            // if finished, change status
            if(course.getStopTime() != null && course.getStopTime().before(new Date())){
                log.debug("Course finished: " + course.getId());
                course.setStatus(CourseStatus.COURSE_FINISHED);
                courseManager.saveCourse(course);
            }
        }
    }

    public void executeTask() {
        log.info("running courseStatusManager");
        processCourses();
    }

    public void setLocale(Locale locale) {
        //Do nothing
    }
}

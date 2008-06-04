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
        Course course = new Course();
        // Get only published courses
        course.setStatus(CourseStatus.COURSE_PUBLISHED);
        List<Course> courses = courseManager.searchCourses(course,null,null);
        Iterator<Course> it = courses.iterator();
        while (it.hasNext()){
            Course c = it.next();
            // if finished, change status
            if(c.getStopTime().before(new Date())){
                c.setStatus(CourseStatus.COURSE_FINISHED);
                courseManager.saveCourse(c);
            }
        }

    }

    public void executeTask() {
        processCourses();
        log.info("Ran CourseStatusManager");
    }

    public void setLocale(Locale locale) {
        //Do nothing
    }
}

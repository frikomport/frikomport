package no.unified.soak.service;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 03.jun.2008
 * Time: 11:49:03
 * To change this template use File | Settings | File Templates.
 */
public interface CourseStatusManager extends Task {
    /**
     * Processes all courses and sets status to finished when finished.
     */
    public void processCourses();
}



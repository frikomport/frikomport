package no.unified.soak.webapp.json;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.unified.soak.model.Course;
import no.unified.soak.model.Location;
import no.unified.soak.service.CourseManager;
import no.unified.soak.util.CourseStatus;

@Path("/moter")
public class MoterJsonController {

	/*
	 * TODO: Denne skal ikke være static
	 */
	private static CourseManager courseManager;
	
	
	public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }
	
	@GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Course> getMoter(@QueryParam("postnr") String postnr, @QueryParam("mnd") Integer mnd) {
		if (postnr == null && mnd == null) {
			return courseManager.getAllCourses();
		}
		Course searchCourse = new Course();
		if (postnr != null) {
			Location loc = new Location();
			loc.setPostalCode(postnr);
			searchCourse.setLocation(loc);
		}
		Date stopDate = null;
		if (mnd != null && mnd > 0) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MONTH, mnd);
			stopDate = now.getTime();
		}
		
		return courseManager.searchCourses(searchCourse , new Date(System.currentTimeMillis()), stopDate, new Integer[]{CourseStatus.COURSE_CREATED, CourseStatus.COURSE_FINISHED, CourseStatus.COURSE_PUBLISHED});

	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("{id}")
	public Course getMote(@PathParam("id") String courseId) {
		return courseManager.getCourse(courseId);
	}
}


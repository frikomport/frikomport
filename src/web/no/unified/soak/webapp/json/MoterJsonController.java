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
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.LocationManager;

import org.apache.commons.lang.StringUtils;

@Path("/moter")
public class MoterJsonController {

	/*
	 * TODO: Denne skal ikke være static
	 */
	private CourseManager courseManager;
	private LocationManager locationManager;

	public MoterJsonController() {
	}
	
	public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

	public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

	@GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Course> getMoter(@QueryParam("postnr") String postnr) {
		List<Course> courseList;
		if (postnr == null) {
			courseList = courseManager.getAllCourses();
		} else {
			List<Long> locationIds = locationManager.getLocationIds((StringUtils.leftPad(""+postnr, 4, '0')));
			courseList = courseManager.findByLocationIds(locationIds,999);
		}
		
		Calendar now = Calendar.getInstance();
		Date startTime = now.getTime();
		
		List<Course> resultList = new ArrayList<Course>(); 
		for (Course course : courseList) {
			if (course.getStartTime().after(startTime)) {
				resultList.add(course);
			}
		}
		return resultList;
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("{id}")
	public Course getMote(@PathParam("id") String courseId) {
		Course result;
		try {
			result = courseManager.getCourse(courseId);
		} catch (RuntimeException e) {
			throw new NotFoundException("Course with id [" + courseId + "] not found");
		}
		
		return result;
	}
	
}


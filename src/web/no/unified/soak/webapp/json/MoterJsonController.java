package no.unified.soak.webapp.json;

import java.util.ArrayList;
import java.util.Iterator;
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
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.util.CourseStatus;

import org.apache.commons.lang.StringUtils;

@Path("/moter")
public class MoterJsonController {

	private CourseManager courseManager;
	private LocationManager locationManager;
	private RegistrationManager registrationManager;

	public MoterJsonController() {
	}
	
	public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

	public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

	public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

	@GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Course> getMoter(@QueryParam("postnr") String postnr) {
		List<Course> courseList = new ArrayList<Course>();
		if (postnr == null) {
			List<Course> tmpList = courseManager.getAllCourses();
			for (Course course : tmpList) {
				if (course.getStatus() == CourseStatus.COURSE_PUBLISHED) {
					courseList.add(course);				
				}
			}
		} else {
			List<Long> locationIds = locationManager.getLocationIds((StringUtils.leftPad(""+postnr, 4, '0')));
			courseList = courseManager.findByLocationIds(locationIds,15);
		}
		if (courseList != null) {
			return updateAvailableAttendants(courseList);
		} else {
			return null;
		}
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("{id}")
	public Course getMote(@PathParam("id") String courseId) {
		Course course;
		try {
			course = courseManager.getCourse(courseId);
			updateAvailability(course);
			
		} catch (RuntimeException e) {
			throw new NotFoundException("Course not found. " + e.getLocalizedMessage());
		}
		return course;
	}
	
	private List<Course> updateAvailableAttendants(List courses) {
		List<Course> updated = new ArrayList<Course>();
		
		for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
			Course course = (Course) iterator.next();
			updateAvailability(course);
			updated.add(course);
		}

		return updated;
	}

	private void updateAvailability(Course course) {
		Integer available = registrationManager.getAvailability(true, course);
		course.setAvailableAttendants(available);
	}
	
}


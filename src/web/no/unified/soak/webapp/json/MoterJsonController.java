package no.unified.soak.webapp.json;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.unified.soak.model.Course;
import no.unified.soak.model.Organization;

@Path("/moter")
public class MoterJsonController {
	
	
	
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	public List<Course> getMoter(@QueryParam("postnr") String postnr) {
		Course course = new Course();
		course.setName("Testnavn på kurs" + (postnr!=null ? " på postnr " + postnr:""));
		course.setAvailableAttendants(5);
		course.setAttendants(35);
		course.setId(1234L);
		course.setDescription("Testkurs");
		course.setRestricted(postnr != null);
		Organization org = new Organization("orgnavn", 12341234, 1, true);
		course.setOrganization(org);

		Organization org2 = new Organization("orgnavn2", 123412342, 2, true);
		course.setOrganization(org2);
		
		Course course2 = new Course();
		course2.setName("Testnavn på kurs" + (postnr!=null ? " på postnr " + postnr:""));
		course2.setAvailableAttendants(5);
		course2.setAttendants(35);
		course2.setId(1234L);
		course2.setDescription("Testkurs");
		course2.setRestricted(postnr != null);
		Organization org2_1 = new Organization("orgnavn", 12341234, 1, true);
		course2.setOrganization(org2_1);

		Organization org2_2 = new Organization("orgnavn2", 123412342, 2, true);
		course2.setOrganization(org2_2);
		
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		courses.add(course2);
		
		return courses;
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("{id}")
	public Course getMote(@PathParam("id") String courseId) {
		Course course = new Course();
		course.setName("Testnavn på kurs");
		course.setAvailableAttendants(5);
		course.setAttendants(35);
		course.setId(Long.valueOf(courseId));
		course.setDescription("Testkurs");
		Organization org = new Organization("orgnavn", 12341234, 1, true);
		course.setOrganization(org);

		Organization org2 = new Organization("orgnavn2", 123412342, 2, true);
		course.setOrganization(org2);
		return course;
	}
}


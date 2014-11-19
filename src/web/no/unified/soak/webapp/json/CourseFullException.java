package no.unified.soak.webapp.json;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.Responses;

public class CourseFullException extends WebApplicationException {

	/**
	 * Create a HTTP 409 (Conflict) exception.
	 */
	public CourseFullException() {
		super(Responses.conflict().entity("COURSE_FULL").type("text/plain").build());
	}
}
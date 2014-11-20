package no.unified.soak.webapp.json;

import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.Responses;

public class CourseCancelledException extends WebApplicationException {

	/**
	 * Create a HTTP 409 (Conflict) exception.
	 */
	public CourseCancelledException() {
		super(Responses.conflict().entity("COURSE_CANCELLED").type("text/plain").build());
	}
}
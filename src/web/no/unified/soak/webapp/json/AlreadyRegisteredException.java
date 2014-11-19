package no.unified.soak.webapp.json;

import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.Responses;

public class AlreadyRegisteredException extends WebApplicationException {

	/**
	 * Create a HTTP 409 (Conflict) exception.
	 */
	public AlreadyRegisteredException() {
		super(Responses.conflict().entity("ALREADY_REGISTERED").type("text/plain").build());
	}
}
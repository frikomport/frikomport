package no.unified.soak.webapp.json;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;
import no.unified.soak.service.RegistrationManager;

@Path("/paamelding")
public class PameldingJsonController {
	
	@Context UriInfo uriInfo;
	
	private RegistrationManager registrationManager;

	public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }
	
	@POST
	@Consumes( MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response registrerPamelding(Registration registration) {
		
		
		if (!isValid(registration)) {
			throw new WebApplicationException(Response.status(com.sun.jersey.api.client.ClientResponse.Status.PRECONDITION_FAILED).build());
		}

		registration.setLocale("no");
		registration.setStatus(Status.RESERVED);
		registration.setUsername(registration.getEmail());
		
		try {
			registrationManager.saveRegistration(registration);
	
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			URI createdUri  = ub.path(""+registration.getId()).build();
		
			return Response.created(createdUri).build();
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
	}
	
	private boolean isValid(Registration registration) {
		return (isSet(registration.getFirstName()) &&
				isSet(registration.getLastName()) &&
				isSet(registration.getBirthdate()) &&
				isSet(registration.getMobilePhone()) &&
				isSet(registration.getCourseid()) &&
				isSet(registration.getParticipants()) &&
				isSet(registration.getEmail()) &&
				isSet(registration.getPostnr()));
	}

	private boolean isSet(Object value) {
		return value != null && !"".equals(""+value);
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/{id}")
	public Registration hentPamelding(@PathParam("id") String registrationId) {
		Registration registration;
		try {
			registration = registrationManager.getRegistration(registrationId);
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
		return registration;
	}
	
	@DELETE
	@Path("/{id}")
	public Response avbrytPamelding(@PathParam("id") String registrationId) {
		try {
			registrationManager.removeRegistration(registrationId);
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
		return Response.noContent().build();
	}
}

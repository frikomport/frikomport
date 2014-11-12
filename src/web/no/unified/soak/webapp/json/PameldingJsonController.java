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
		
		registration.setLocale("no");
		registration.setStatus(Status.RESERVED);
		try {
			registrationManager.saveRegistration(registration);
	
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			URI createdUri  = ub.path(""+registration.getId()).build();
		
		return Response.created(createdUri).build();
		} catch (RuntimeException e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}
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

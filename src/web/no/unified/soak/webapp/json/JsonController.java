package no.unified.soak.webapp.json;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class JsonController {

	@GET
	@Produces("text/json")
    public String getHelloWorld() {
    	return "{\"hello\" : \"world\" }";
    }
}

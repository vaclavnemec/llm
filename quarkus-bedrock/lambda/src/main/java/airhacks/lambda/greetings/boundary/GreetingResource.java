package airhacks.lambda.greetings.boundary;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    Greeter greeter;


    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(String message) {
        Log.info(message + " retrieved");
        return this.greeter.greetings(message);
    }
}
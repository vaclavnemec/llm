package airhacks.lambda.greetings.boundary;

import static java.lang.System.Logger.Level.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import airhacks.lambda.greetings.control.Bedrock;

@ApplicationScoped
public class Greeter {

    static System.Logger LOG = System.getLogger(Greeter.class.getName()); 

    @Inject
    @ConfigProperty(defaultValue = "You are a friendly greeter. Write a humourous and friendly message", name="system")
    String systemMessage;
    

    public String greetings(String userMessage) {
        var response = Bedrock.invokeClaude(systemMessage, userMessage,0f);
        LOG.log(INFO, "received: " + response);
        return response.getString("completion");
    }
}

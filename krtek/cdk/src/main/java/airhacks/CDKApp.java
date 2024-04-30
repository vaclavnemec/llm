package airhacks;

import java.util.Map;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;

public interface CDKApp {

    static void main(String... args) {

        var app = new App();
        var appName = "krtek";

        Tags.of(app).add("project", "MicroProfile with Quarkus on AWS Lambda");
        Tags.of(app).add("environment", "development");
        Tags.of(app).add("application", appName);

        var stack = new InfrastructureBuilder(app, appName)
                .functionName("airhacks_ChiefKrtek")
                .configuration(Map.of("message","where is myska"))
                .functionURLBuilder()
                .build();        
        app.synth();
    }
}

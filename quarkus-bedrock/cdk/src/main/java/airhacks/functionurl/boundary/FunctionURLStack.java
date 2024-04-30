package airhacks.functionurl.boundary;

import airhacks.InfrastructureBuilder;
import airhacks.functionurl.control.Bedrock;
import airhacks.lambda.control.QuarkusLambda;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.constructs.Construct;

public class FunctionURLStack extends Stack {

    public static class FunctionURLBuilder {
        private InfrastructureBuilder infrastructureBuilder;
        private FunctionUrlAuthType authType = FunctionUrlAuthType.NONE;
        private StackProps stackProps = StackProps.builder().env(
                Environment
                        .builder().region("eu-central-1")
                        .build())
                .build();

        public FunctionURLBuilder(InfrastructureBuilder infrastructureBuilder) {
            this.infrastructureBuilder = infrastructureBuilder;
        }

        public Construct construct() {
            return this.infrastructureBuilder.construct();
        }

        public String stackId() {
            return this.infrastructureBuilder.stackId();
        }

        public FunctionURLBuilder withIAMAuth() {
            this.authType = FunctionUrlAuthType.AWS_IAM;
            return this;
        }

        public FunctionURLBuilder inUSEast1() {
            this.stackProps = StackProps
                    .builder()
                    .env(Environment.builder().region("us_east_1").build())
                    .build();
            return this;
        }

        public FunctionURLStack build() {
            return new FunctionURLStack(this);
        }

    }

    public FunctionURLStack(FunctionURLBuilder builder) {
        super(builder.construct(), builder.stackId(), builder.stackProps);
        var infrastructureBuilder = builder.infrastructureBuilder;
        var quarkusLambda = new QuarkusLambda(this, infrastructureBuilder.functionZipLocation(),
                infrastructureBuilder.functionName(),
                infrastructureBuilder.functionHandler(), infrastructureBuilder.ram(),
                infrastructureBuilder.isSnapStart(),
                infrastructureBuilder.timeout(),
                infrastructureBuilder.configuration());
        var function = quarkusLambda.getFunction();
        var functionUrl = function.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(builder.authType)
                .build());
        var policy = Bedrock.invokePolicy();
        function.addToRolePolicy(policy);
        CfnOutput.Builder.create(this, "FunctionURLOutput").value(functionUrl.getUrl()).build();

    }
}

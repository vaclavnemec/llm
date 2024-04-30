package airhacks.functionurl.control;

import java.util.List;

import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;

public interface Bedrock {
    
    static PolicyStatement invokePolicy(){
        return PolicyStatement.Builder.create()
                .sid("BedrockInvokeAccess")
                .resources(List.of("arn:aws:bedrock:*::foundation-model/*"))
                .actions(List.of("bedrock:InvokeModel"))
                .effect(Effect.ALLOW)
                .build();   
    }
}

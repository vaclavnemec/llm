package airhacks.lambda.greetings.control;

import java.time.Duration;
import java.util.List;

import org.json.JSONObject;

import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;

public interface Bedrock {

        String claudeModelId = "anthropic.claude-v2:1";


        BedrockRuntimeClient client = BedrockRuntimeClient
                        .builder()
                        .overrideConfiguration(ClientOverrideConfiguration
                                        .builder()
                                        .apiCallTimeout(Duration.ofMinutes(5))
                                        .build())
                        .build();

        /**
         * https://docs.anthropic.com/claude/docs/models-overview#model-comparison
         * https://docs.anthropic.com/claude/reference/messages_post
         */


        int MAX_TOKENS = 4000;

        static String getPrompt(String system, String user) {
                if (system == null || system.isEmpty()) {
                        return "\n\nHuman: %s\n\nAssistant:".formatted(user);
                } else {
                        return """
                                        %s
                                        \n\nHuman: %s\n\nAssistant:
                                        """.formatted(system, user);
                }
        }

        public static JSONObject invokeClaude(String system, String user, float temperature) {
                

                // Claude requires you to enclose the prompt as follows:
                var enclosedPrompt = getPrompt(system, user);
                var payload = new JSONObject()
                                .put("prompt", enclosedPrompt)
                                .put("max_tokens_to_sample", MAX_TOKENS)
                                .put("temperature", temperature)
                                .put("stop_sequences", List.of("\n\nHuman:"))
                                .toString();

                Log.debug(payload);
                Log.debug("AI is thinking...");
                var request = InvokeModelRequest.builder()
                                .body(SdkBytes.fromUtf8String(payload))
                                .modelId(claudeModelId)
                                .overrideConfiguration(AwsRequestOverrideConfiguration.builder()
                                                .apiCallTimeout(Duration.ofMinutes(5))
                                                .build())
                                .contentType("application/json")
                                .accept("application/json")
                                .build();
                var response = client.invokeModel(request);
                var message = response.body().asUtf8String();
                return new JSONObject(message);
        }

}

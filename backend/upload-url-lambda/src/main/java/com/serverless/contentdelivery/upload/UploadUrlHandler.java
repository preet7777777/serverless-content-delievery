package com.serverless.contentdelivery.upload;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.serverless.contentdelivery.shared.aws.AwsClientFactory;
import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.domain.UploadAuthorizationRequest;
import com.serverless.contentdelivery.shared.domain.UploadAuthorizationResponse;
import com.serverless.contentdelivery.shared.http.JsonMapperFactory;
import com.serverless.contentdelivery.shared.service.UploadAuthorizationService;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * API description:
 * POST /v1/uploads/presign validates upload metadata and returns a short-lived
 * pre-signed S3 PUT URL for one direct browser upload.
 */
public class UploadUrlHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AppConfig config = AppConfig.fromEnvironment();
    private final UploadAuthorizationService uploadAuthorizationService =
            new UploadAuthorizationService(config, AwsClientFactory.createS3Presigner(config));

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            final UploadAuthorizationRequest request = JsonMapperFactory.objectMapper()
                    .readValue(input.getBody(), UploadAuthorizationRequest.class);
            final UploadAuthorizationResponse response = uploadAuthorizationService.authorize(request);

            return jsonResponse(200, response);
        } catch (IllegalArgumentException exception) {
            return errorResponse(400, exception.getMessage());
        } catch (Exception exception) {
            return errorResponse(500, "Upload authorization failed.");
        }
    }

    private APIGatewayProxyResponseEvent jsonResponse(int statusCode, Object responseBody) throws JsonProcessingException {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(JsonMapperFactory.objectMapper().writeValueAsString(responseBody));
    }

    private APIGatewayProxyResponseEvent errorResponse(int statusCode, String message) {
        try {
            return jsonResponse(statusCode, Map.of("message", message));
        } catch (JsonProcessingException exception) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withBody("{\"message\":\"" + message + "\"}");
        }
    }

}

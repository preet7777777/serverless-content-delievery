package com.serverless.contentdelivery.local;

import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.domain.UploadAuthorizationRequest;
import com.serverless.contentdelivery.shared.http.JsonMapperFactory;
import com.serverless.contentdelivery.shared.service.UploadAuthorizationService;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;

/**
 * This controller mirrors the production upload contract while allowing local
 * auth bypass so the end-to-end browser flow stays testable without Cognito.
 */
public final class LocalUploadAuthorizationController {
    private LocalUploadAuthorizationController() {
    }

    public static void handle(
            HttpExchange exchange,
            AppConfig config,
            UploadAuthorizationService uploadAuthorizationService) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            LocalDevelopmentApplication.writeEmpty(exchange, 204);
            return;
        }

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            LocalDevelopmentApplication.writeJson(exchange, 405, Map.of("message", "Method not allowed."));
            return;
        }

        if (!config.localAuthBypassEnabled()) {
            final String authorizationHeader = exchange.getRequestHeaders().getFirst("Authorization");
            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                LocalDevelopmentApplication.writeJson(exchange, 401, Map.of("message", "Authorization header is required."));
                return;
            }
        }

        try {
            final UploadAuthorizationRequest request = JsonMapperFactory.objectMapper().readValue(
                    LocalDevelopmentApplication.readRequestBody(exchange),
                    UploadAuthorizationRequest.class
            );
            LocalDevelopmentApplication.writeJson(exchange, 200, uploadAuthorizationService.authorize(request));
        } catch (IllegalArgumentException exception) {
            LocalDevelopmentApplication.writeJson(exchange, 400, Map.of("message", exception.getMessage()));
        } catch (Exception exception) {
            LocalDevelopmentApplication.writeJson(exchange, 500, Map.of("message", "Local upload authorization failed."));
        }
    }
}

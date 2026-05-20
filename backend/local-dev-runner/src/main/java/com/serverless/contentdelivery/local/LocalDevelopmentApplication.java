package com.serverless.contentdelivery.local;

import com.serverless.contentdelivery.shared.aws.AwsClientFactory;
import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.http.JsonMapperFactory;
import com.serverless.contentdelivery.shared.service.ImageProcessingService;
import com.serverless.contentdelivery.shared.service.UploadAuthorizationService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Local development runner description:
 * This process exposes a tiny HTTP API compatible with the frontend contract
 * and continuously polls the raw bucket so the upload-to-optimization flow can
 * be tested end to end against LocalStack.
 */
public final class LocalDevelopmentApplication {
    private LocalDevelopmentApplication() {
    }

    public static void main(String[] args) throws IOException {
        final AppConfig config = AppConfig.fromEnvironment();
        final S3Client s3Client = AwsClientFactory.createS3Client(config);
        final UploadAuthorizationService uploadAuthorizationService =
                new UploadAuthorizationService(config, AwsClientFactory.createS3Presigner(config));
        final ImageProcessingService imageProcessingService = new ImageProcessingService(config, s3Client);

        final LocalProcessorPoller poller = new LocalProcessorPoller(config, s3Client, imageProcessingService);
        poller.start();

        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/health", exchange -> writeJson(exchange, 200, Map.of("status", "ok")));
        httpServer.createContext("/v1/uploads/presign", exchange ->
                LocalUploadAuthorizationController.handle(exchange, config, uploadAuthorizationService));
        httpServer.setExecutor(Executors.newCachedThreadPool());
        httpServer.start();

        System.out.println("Local development API listening on http://0.0.0.0:8080");
    }

    static void writeJson(HttpExchange exchange, int statusCode, Object payload) throws IOException {
        final byte[] responseBody = JsonMapperFactory.objectMapper().writeValueAsBytes(payload);
        final Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.set("Access-Control-Allow-Methods", "POST, OPTIONS, GET");
        exchange.sendResponseHeaders(statusCode, responseBody.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        }
    }

    static void writeEmpty(HttpExchange exchange, int statusCode) throws IOException {
        final Headers headers = exchange.getResponseHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.set("Access-Control-Allow-Methods", "POST, OPTIONS, GET");
        exchange.sendResponseHeaders(statusCode, -1);
        exchange.close();
    }

    static String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}

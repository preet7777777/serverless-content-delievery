package com.serverless.contentdelivery.shared.config;

import java.net.URI;
import java.time.Duration;
import java.util.List;

/**
 * Shared application configuration keeps both Lambdas aligned on the same
 * operational limits, storage boundaries, and deployment metadata.
 */
public record AppConfig(
        String awsRegion,
        String rawBucketName,
        String optimizedBucketName,
        String rawPrefix,
        String optimizedPrefix,
        String thumbnailPrefix,
        URI s3EndpointOverride,
        URI cdnBaseUrl,
        List<String> allowedMimeTypes,
        long maxUploadSizeBytes,
        Duration uploadUrlTtl,
        Duration localProcessorPollInterval,
        boolean localAuthBypassEnabled,
        String logLevel,
        String metricsNamespace) {

    public static AppConfig fromEnvironment() {
        return new AppConfig(
                getenv("AWS_REGION", "ap-south-1"),
                getenv("RAW_BUCKET_NAME", "scd-raw-assets-shared"),
                getenv("OPTIMIZED_BUCKET_NAME", "scd-optimized-assets-shared"),
                getenv("RAW_PREFIX", "raw/"),
                getenv("OPTIMIZED_PREFIX", "optimized/"),
                getenv("THUMBNAIL_PREFIX", "thumbnails/"),
                getOptionalUri("S3_ENDPOINT_OVERRIDE"),
                URI.create(getenv("CDN_BASE_URL", "https://cdn.example.com")),
                List.of(getenv("ALLOWED_MIME_TYPES", "image/jpeg,image/png,image/webp").split(",")),
                Long.parseLong(getenv("MAX_UPLOAD_SIZE_BYTES", "10485760")),
                Duration.ofSeconds(Long.parseLong(getenv("UPLOAD_URL_TTL_SECONDS", "300"))),
                Duration.ofSeconds(Long.parseLong(getenv("LOCAL_PROCESSOR_POLL_INTERVAL_SECONDS", "5"))),
                Boolean.parseBoolean(getenv("LOCAL_AUTH_BYPASS_ENABLED", "false")),
                getenv("LOG_LEVEL", "INFO"),
                getenv("METRICS_NAMESPACE", "ServerlessContentDelivery")
        );
    }

    private static URI getOptionalUri(String key) {
        final String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return null;
        }
        return URI.create(value);
    }

    private static String getenv(String key, String fallback) {
        final String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}

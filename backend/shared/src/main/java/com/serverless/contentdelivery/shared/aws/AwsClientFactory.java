package com.serverless.contentdelivery.shared.aws;

import com.serverless.contentdelivery.shared.config.AppConfig;
import java.net.URI;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Shared AWS client creation keeps Lambda and local-runtime behavior aligned
 * while still allowing endpoint overrides for LocalStack-based testing.
 */
public final class AwsClientFactory {
    private AwsClientFactory() {
    }

    public static S3Client createS3Client(AppConfig config) {
        final var builder = S3Client.builder()
                .region(Region.of(config.awsRegion()))
                .credentialsProvider(credentialsProvider(config))
                .httpClientBuilder(ApacheHttpClient.builder())
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(usePathStyle(config)).build());

        if (config.s3EndpointOverride() != null) {
            builder.endpointOverride(config.s3EndpointOverride());
        }

        return builder.build();
    }

    public static S3Presigner createS3Presigner(AppConfig config) {
        final S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(config.awsRegion()))
                .credentialsProvider(credentialsProvider(config))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(usePathStyle(config)).build());

        if (config.s3EndpointOverride() != null) {
            builder.endpointOverride(config.s3EndpointOverride());
        }

        return builder.build();
    }

    private static AwsCredentialsProvider credentialsProvider(AppConfig config) {
        if (config.s3EndpointOverride() != null) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test"));
        }
        return DefaultCredentialsProvider.create();
    }

    private static boolean usePathStyle(AppConfig config) {
        final URI endpoint = config.s3EndpointOverride();
        return endpoint != null && endpoint.getHost() != null && !endpoint.getHost().endsWith("amazonaws.com");
    }
}

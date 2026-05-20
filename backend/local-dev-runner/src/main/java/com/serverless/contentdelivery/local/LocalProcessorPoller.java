package com.serverless.contentdelivery.local;

import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.service.ImageProcessingService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

/**
 * LocalStack does not provide the same event wiring guarantees as real S3
 * notifications in this setup, so the local worker polls raw storage and
 * applies the same processing rules idempotently.
 */
public final class LocalProcessorPoller {
    private final AppConfig config;
    private final S3Client s3Client;
    private final ImageProcessingService imageProcessingService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public LocalProcessorPoller(AppConfig config, S3Client s3Client, ImageProcessingService imageProcessingService) {
        this.config = config;
        this.s3Client = s3Client;
        this.imageProcessingService = imageProcessingService;
    }

    public void start() {
        scheduledExecutorService.scheduleWithFixedDelay(this::poll, 2, config.localProcessorPollInterval().toSeconds(), TimeUnit.SECONDS);
    }

    private void poll() {
        try {
            s3Client.listObjectsV2Paginator(ListObjectsV2Request.builder()
                            .bucket(config.rawBucketName())
                            .prefix(config.rawPrefix())
                            .build())
                    .contents()
                    .forEach(object -> imageProcessingService.processIfNeeded(config.rawBucketName(), object.key()));
        } catch (Exception exception) {
            System.err.println("Local processor poll failed: " + exception.getMessage());
        }
    }
}

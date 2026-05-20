package com.serverless.contentdelivery.processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.serverless.contentdelivery.shared.aws.AwsClientFactory;
import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.service.ImageProcessingService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * API description:
 * S3 object-created events invoke this Lambda to read one raw image, generate
 * optimized variants, and write them into the controlled delivery prefixes.
 */
public class ImageProcessorHandler implements RequestHandler<S3Event, String> {
    private final AppConfig config = AppConfig.fromEnvironment();
    private final ImageProcessingService imageProcessingService =
            new ImageProcessingService(config, AwsClientFactory.createS3Client(config));

    @Override
    public String handleRequest(S3Event input, Context context) {
        if (input == null || input.getRecords() == null || input.getRecords().isEmpty()) {
            return "No records received.";
        }

        input.getRecords().forEach(record -> {
            final String bucket = record.getS3().getBucket().getName();
            final String objectKey = URLDecoder.decode(record.getS3().getObject().getKey(), StandardCharsets.UTF_8);
            imageProcessingService.processIfNeeded(bucket, objectKey);
        });

        return "Processed " + input.getRecords().size() + " object(s).";
    }
}

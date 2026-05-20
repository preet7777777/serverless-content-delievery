package com.serverless.contentdelivery.shared.service;

import com.serverless.contentdelivery.shared.config.AppConfig;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import net.coobird.thumbnailator.Thumbnails;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * This service owns raw-object transformation so both Lambda events and local
 * polling can produce the same optimized assets.
 */
public final class ImageProcessingService {
    private final AppConfig config;
    private final S3Client s3Client;

    public ImageProcessingService(AppConfig config, S3Client s3Client) {
        this.config = config;
        this.s3Client = s3Client;
    }

    public boolean processIfNeeded(String sourceBucket, String sourceKey) {
        final String optimizedKey = buildDerivedKey(config.optimizedPrefix(), sourceKey);
        if (objectExists(config.optimizedBucketName(), optimizedKey)) {
            return false;
        }

        final ResponseBytes<GetObjectResponse> originalImage = s3Client.getObjectAsBytes(
                GetObjectRequest.builder().bucket(sourceBucket).key(sourceKey).build()
        );

        writeDerivedAsset(optimizedKey, originalImage.asByteArray(), 1280, 0.82f);
        writeDerivedAsset(buildDerivedKey(config.thumbnailPrefix(), sourceKey), originalImage.asByteArray(), 320, 0.75f);
        return true;
    }

    public String buildDerivedKey(String targetPrefix, String sourceKey) {
        final String relativeKey = sourceKey.startsWith(config.rawPrefix())
                ? sourceKey.substring(config.rawPrefix().length())
                : sourceKey;

        final int extensionIndex = relativeKey.lastIndexOf('.');
        final String jpegRelativeKey = extensionIndex >= 0
                ? relativeKey.substring(0, extensionIndex) + ".jpg"
                : relativeKey + ".jpg";

        return targetPrefix + jpegRelativeKey;
    }

    private boolean objectExists(String bucketName, String objectKey) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(objectKey).build());
            return true;
        } catch (NoSuchKeyException exception) {
            return false;
        } catch (S3Exception exception) {
            if (exception.statusCode() == 404) {
                return false;
            }
            throw exception;
        }
    }

    private void writeDerivedAsset(String targetKey, byte[] sourceBytes, int width, float quality) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(new ByteArrayInputStream(sourceBytes))
                    .size(width, width)
                    .outputFormat("jpg")
                    .outputQuality(quality)
                    .toOutputStream(outputStream);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(config.optimizedBucketName())
                            .key(targetKey)
                            .contentType("image/jpeg")
                            .cacheControl("public, max-age=31536000, immutable")
                            .build(),
                    RequestBody.fromBytes(outputStream.toByteArray())
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to write derived asset for key " + targetKey, exception);
        }
    }
}

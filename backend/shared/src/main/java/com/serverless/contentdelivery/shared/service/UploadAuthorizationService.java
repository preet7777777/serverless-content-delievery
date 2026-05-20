package com.serverless.contentdelivery.shared.service;

import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.domain.UploadAuthorizationRequest;
import com.serverless.contentdelivery.shared.domain.UploadAuthorizationResponse;
import com.serverless.contentdelivery.shared.validation.UploadRequestValidator;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/**
 * This service owns the control-plane logic for validating one upload request
 * and turning it into a tightly scoped direct-upload permission.
 */
public final class UploadAuthorizationService {
    private final AppConfig config;
    private final S3Presigner presigner;

    public UploadAuthorizationService(AppConfig config, S3Presigner presigner) {
        this.config = config;
        this.presigner = presigner;
    }

    public UploadAuthorizationResponse authorize(UploadAuthorizationRequest request) {
        UploadRequestValidator.validate(request, config);

        final String fileName = sanitizeFileName(request.fileName());
        final String objectKey = config.rawPrefix() + UUID.randomUUID() + "-" + fileName;
        final String derivedRelativeKey = objectKey.replaceFirst("^" + java.util.regex.Pattern.quote(config.rawPrefix()), "");
        final String jpegRelativeKey = replaceExtensionWithJpeg(derivedRelativeKey);
        final String optimizedRelativeKey = config.optimizedPrefix() + jpegRelativeKey;
        final String thumbnailRelativeKey = config.thumbnailPrefix() + jpegRelativeKey;
        final OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plus(config.uploadUrlTtl());

        final PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(config.uploadUrlTtl())
                        .putObjectRequest(PutObjectRequest.builder()
                                .bucket(config.rawBucketName())
                                .key(objectKey)
                                .contentType(request.contentType())
                                .build())
                        .build()
        );

        return new UploadAuthorizationResponse(
                objectKey,
                presignedRequest.url().toString(),
                expiresAt.toString(),
                config.cdnBaseUrl().toString(),
                joinUrl(config.cdnBaseUrl().toString(), optimizedRelativeKey),
                joinUrl(config.cdnBaseUrl().toString(), thumbnailRelativeKey)
        );
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "-");
    }

    private String replaceExtensionWithJpeg(String objectKey) {
        final int extensionIndex = objectKey.lastIndexOf('.');
        if (extensionIndex < 0) {
            return objectKey + ".jpg";
        }
        return objectKey.substring(0, extensionIndex) + ".jpg";
    }

    private String joinUrl(String baseUrl, String relativePath) {
        final String sanitizedBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        final String sanitizedPath = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        return sanitizedBase + "/" + sanitizedPath;
    }
}

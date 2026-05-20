package com.serverless.contentdelivery.shared.validation;

import com.serverless.contentdelivery.shared.config.AppConfig;
import com.serverless.contentdelivery.shared.domain.UploadAuthorizationRequest;

/**
 * Validation is explicit so the API contract rejects unsafe upload requests
 * before any signed storage permission is created.
 */
public final class UploadRequestValidator {
    private UploadRequestValidator() {
    }

    public static void validate(UploadAuthorizationRequest request, AppConfig config) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required.");
        }
        if (request.fileName() == null || request.fileName().isBlank()) {
            throw new IllegalArgumentException("fileName is required.");
        }
        if (request.contentType() == null || !config.allowedMimeTypes().contains(request.contentType())) {
            throw new IllegalArgumentException("Unsupported contentType.");
        }
        if (request.sizeBytes() <= 0 || request.sizeBytes() > config.maxUploadSizeBytes()) {
            throw new IllegalArgumentException("sizeBytes is outside the permitted upload range.");
        }
    }
}

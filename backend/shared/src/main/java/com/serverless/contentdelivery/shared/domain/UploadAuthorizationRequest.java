package com.serverless.contentdelivery.shared.domain;

/**
 * This request describes one upload attempt before the browser receives any
 * direct permission to place content into raw object storage.
 */
public record UploadAuthorizationRequest(
        String fileName,
        String contentType,
        long sizeBytes) {
}

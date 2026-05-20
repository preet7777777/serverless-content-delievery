package com.serverless.contentdelivery.shared.domain;

/**
 * This response tells the client exactly where one direct upload may go and
 * which CDN base path will later expose the processed asset.
 */
public record UploadAuthorizationResponse(
        String objectKey,
        String uploadUrl,
        String expiresAt,
        String assetBaseUrl,
        String optimizedAssetUrl,
        String thumbnailAssetUrl) {
}

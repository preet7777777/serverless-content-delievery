import type {
  RuntimeConfig,
  UploadAuthorizationRequest,
  UploadAuthorizationResponse
} from "../types/env";

/**
 * This API call asks the backend to describe one allowed upload attempt.
 * The endpoint should stay small and metadata-only because the file body
 * must flow directly to S3 instead of through the control-plane API.
 */
export async function requestUploadAuthorization(
  config: RuntimeConfig,
  payload: UploadAuthorizationRequest,
  accessToken = "dummy-cognito-access-token"
): Promise<UploadAuthorizationResponse> {
  const controller = new AbortController();
  const timeout = window.setTimeout(() => controller.abort(), config.requestTimeoutMs);

  try {
    const response = await fetch(`${config.apiBaseUrl}${config.uploadUrlPath}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`
      },
      body: JSON.stringify(payload),
      signal: controller.signal
    });

    if (!response.ok) {
      throw new Error(`Upload authorization failed with status ${response.status}.`);
    }

    return (await response.json()) as UploadAuthorizationResponse;
  } finally {
    window.clearTimeout(timeout);
  }
}

/**
 * This data-plane call sends the file directly to the short-lived S3 URL
 * returned by the backend so the frontend does not proxy large file uploads.
 */
export async function uploadFileToStorage(uploadUrl: string, file: File): Promise<void> {
  const response = await fetch(uploadUrl, {
    method: "PUT",
    headers: {
      "Content-Type": file.type
    },
    body: file
  });

  if (!response.ok) {
    throw new Error(`Direct upload failed with status ${response.status}.`);
  }
}

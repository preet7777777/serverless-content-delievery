export type RuntimeConfig = {
  appName: string;
  environment: string;
  awsRegion: string;
  apiBaseUrl: string;
  uploadUrlPath: string;
  requestTimeoutMs: number;
  cdnBaseUrl: string;
  allowedMimeTypes: string[];
  maxUploadSizeBytes: number;
  auth: {
    provider: "cognito";
    userPoolId: string;
    userPoolClientId: string;
    identityPoolId: string;
    cognitoDomain: string;
  };
};

export type UploadAuthorizationRequest = {
  fileName: string;
  contentType: string;
  sizeBytes: number;
};

export type UploadAuthorizationResponse = {
  objectKey: string;
  uploadUrl: string;
  expiresAt: string;
  assetBaseUrl: string;
  optimizedAssetUrl: string;
  thumbnailAssetUrl: string;
};

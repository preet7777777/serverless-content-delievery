import type { UploadAuthorizationResponse } from "../types/env";

type AssetStatusCardProps = {
  uploadResult: UploadAuthorizationResponse | null;
};

/**
 * This component explains the eventual-consistency state to the user after
 * the direct upload has completed and before derivative assets are guaranteed
 * to exist behind the CDN.
 */
export function AssetStatusCard({ uploadResult }: AssetStatusCardProps) {
  if (!uploadResult) {
    return (
      <section className="panel muted-panel">
        <div className="panel-header">
          <p className="eyebrow">Delivery Plane</p>
          <h2>Awaiting first upload</h2>
        </div>
        <p>
          After upload, S3 will trigger the processor Lambda and CloudFront will eventually
          serve the optimized asset from the configured delivery path.
        </p>
      </section>
    );
  }

  return (
    <section className="panel">
      <div className="panel-header">
        <p className="eyebrow">Async Processing</p>
        <h2>Upload accepted</h2>
      </div>
      <p>Your original image is in raw storage. Optimized assets will appear under the CDN base URL.</p>
      <img
        className="asset-preview"
        src={uploadResult.thumbnailAssetUrl}
        alt="Processed thumbnail preview"
      />
      <dl className="details-grid">
        <div>
          <dt>Object key</dt>
          <dd>{uploadResult.objectKey}</dd>
        </div>
        <div>
          <dt>Upload URL expires</dt>
          <dd>{new Date(uploadResult.expiresAt).toLocaleString()}</dd>
        </div>
        <div>
          <dt>Asset base URL</dt>
          <dd>{uploadResult.assetBaseUrl}</dd>
        </div>
        <div>
          <dt>Optimized asset URL</dt>
          <dd>
            <a href={uploadResult.optimizedAssetUrl} target="_blank" rel="noreferrer">
              {uploadResult.optimizedAssetUrl}
            </a>
          </dd>
        </div>
        <div>
          <dt>Thumbnail URL</dt>
          <dd>
            <a href={uploadResult.thumbnailAssetUrl} target="_blank" rel="noreferrer">
              {uploadResult.thumbnailAssetUrl}
            </a>
          </dd>
        </div>
      </dl>
    </section>
  );
}

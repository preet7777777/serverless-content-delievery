import { useState } from "react";
import { requestUploadAuthorization, uploadFileToStorage } from "../api/uploadApi";
import type { RuntimeConfig, UploadAuthorizationResponse } from "../types/env";

type UploadFormProps = {
  config: RuntimeConfig;
  onUploadCompleted: (response: UploadAuthorizationResponse) => void;
};

/**
 * This component owns the browser-side upload journey.
 * It validates the selected file, calls the backend for a pre-signed URL,
 * and then uploads directly to S3 using that short-lived permission.
 */
export function UploadForm({ config, onUploadCompleted }: UploadFormProps) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage(null);

    const formData = new FormData(event.currentTarget);
    const selectedFile = formData.get("asset") as File | null;

    if (!selectedFile || selectedFile.size === 0) {
      setErrorMessage("Choose an image before requesting upload authorization.");
      return;
    }

    if (!config.allowedMimeTypes.includes(selectedFile.type)) {
      setErrorMessage("This file type is not allowed by the configured upload policy.");
      return;
    }

    if (selectedFile.size > config.maxUploadSizeBytes) {
      setErrorMessage("This file is larger than the configured upload limit.");
      return;
    }

    setIsSubmitting(true);

    try {
      const uploadAuthorization = await requestUploadAuthorization(config, {
        fileName: selectedFile.name,
        contentType: selectedFile.type,
        sizeBytes: selectedFile.size
      });

      await uploadFileToStorage(uploadAuthorization.uploadUrl, selectedFile);
      onUploadCompleted(uploadAuthorization);
      event.currentTarget.reset();
    } catch (error) {
      const message = error instanceof Error ? error.message : "The upload could not be completed.";
      setErrorMessage(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form className="panel" onSubmit={handleSubmit}>
      <div className="panel-header">
        <p className="eyebrow">Control Plane</p>
        <h2>Request a pre-signed upload URL</h2>
      </div>

      <label className="field">
        <span>Select image</span>
        <input name="asset" type="file" accept={config.allowedMimeTypes.join(",")} />
      </label>

      <button className="primary-button" disabled={isSubmitting} type="submit">
        {isSubmitting ? "Preparing upload..." : "Upload image"}
      </button>

      {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
    </form>
  );
}

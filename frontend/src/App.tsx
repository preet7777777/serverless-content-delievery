import { useEffect, useState } from "react";
import { UploadForm } from "./components/UploadForm";
import { AssetStatusCard } from "./components/AssetStatusCard";
import { loadRuntimeConfig } from "./config/runtime";
import type { RuntimeConfig, UploadAuthorizationResponse } from "./types/env";

export default function App() {
  const [config, setConfig] = useState<RuntimeConfig | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [uploadResult, setUploadResult] = useState<UploadAuthorizationResponse | null>(null);

  useEffect(() => {
    loadRuntimeConfig()
      .then(setConfig)
      .catch((error) => {
        const message = error instanceof Error ? error.message : "Runtime configuration could not be loaded.";
        setErrorMessage(message);
      });
  }, []);

  if (errorMessage) {
    return <main className="app-shell error-text">{errorMessage}</main>;
  }

  if (!config) {
    return <main className="app-shell">Loading environment configuration...</main>;
  }

  return (
    <main className="app-shell">
      <section className="hero">
        <p className="eyebrow">React Frontend + Cognito-Controlled API</p>
        <h1>{config.appName}</h1>
        <p className="hero-copy">
          This client only orchestrates the upload flow. Authentication belongs to Cognito, upload
          authorization belongs to the backend, file transfer belongs to S3, and delivery belongs to CloudFront.
        </p>
      </section>

      <section className="grid-layout">
        <UploadForm config={config} onUploadCompleted={setUploadResult} />
        <AssetStatusCard uploadResult={uploadResult} />
      </section>
    </main>
  );
}

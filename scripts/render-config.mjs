import { mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const repoRoot = path.resolve(__dirname, "..");
const sharedConfigPath = path.join(repoRoot, "config", "shared-environment.json");
const profileArgIndex = process.argv.indexOf("--profile");
const profileName = profileArgIndex >= 0 ? process.argv[profileArgIndex + 1] : "shared";

const rawConfig = await readFile(sharedConfigPath, "utf8");
const baseConfig = JSON.parse(rawConfig);

let config = baseConfig;
if (profileName !== "shared") {
  const overridePath = path.join(repoRoot, "config", `${profileName}-overrides.json`);
  const overrideContent = await readFile(overridePath, "utf8");
  const overrides = JSON.parse(overrideContent);
  config = mergeDeep(baseConfig, overrides);
}

const frontendRuntimeConfig = {
  appName: config.frontend.appName,
  environment: config.project.environment,
  awsRegion: config.project.awsRegion,
  apiBaseUrl: config.api.baseUrl,
  uploadUrlPath: config.api.uploadUrlPath,
  requestTimeoutMs: config.api.requestTimeoutMs,
  cdnBaseUrl: config.frontend.cdnBaseUrl,
  allowedMimeTypes: config.frontend.allowedMimeTypes,
  maxUploadSizeBytes: config.frontend.maxUploadSizeBytes,
  auth: config.auth
};

const infraParameters = {
  ParameterKeyValues: {
    EnvironmentName: config.project.environment,
    AwsRegion: config.project.awsRegion,
    RawBucketName: config.storage.rawBucketName,
    OptimizedBucketName: config.storage.optimizedBucketName,
    RawPrefix: config.storage.rawPrefix,
    OptimizedPrefix: config.storage.optimizedPrefix,
    ThumbnailPrefix: config.storage.thumbnailPrefix,
    CognitoUserPoolId: config.auth.userPoolId,
    CognitoUserPoolClientId: config.auth.userPoolClientId,
    CognitoIdentityPoolId: config.auth.identityPoolId,
    CognitoDomain: config.auth.cognitoDomain,
    CloudFrontPriceClass: config.delivery.cloudFrontPriceClass,
    CacheTtlSeconds: String(config.delivery.cacheTtlSeconds),
    MetricsNamespace: config.observability.metricsNamespace,
    LogLevel: config.observability.logLevel
  }
};

await mkdir(path.join(repoRoot, "frontend", "public"), { recursive: true });
await mkdir(path.join(repoRoot, "infra", "parameters"), { recursive: true });

await writeFile(
  path.join(repoRoot, "frontend", "public", "runtime-config.json"),
  JSON.stringify(frontendRuntimeConfig, null, 2) + "\n",
  "utf8"
);

await writeFile(
  path.join(repoRoot, "infra", "parameters", "generated.shared.json"),
  JSON.stringify(infraParameters, null, 2) + "\n",
  "utf8"
);

console.log(`Rendered frontend and infrastructure config using profile "${profileName}"`);

function mergeDeep(target, source) {
  if (Array.isArray(target) || Array.isArray(source)) {
    return source;
  }

  const output = { ...target };
  for (const [key, value] of Object.entries(source)) {
    if (value && typeof value === "object" && !Array.isArray(value) && key in output) {
      output[key] = mergeDeep(output[key], value);
    } else {
      output[key] = value;
    }
  }
  return output;
}

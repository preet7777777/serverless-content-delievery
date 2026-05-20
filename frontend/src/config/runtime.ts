import type { RuntimeConfig } from "../types/env";

let cachedConfig: RuntimeConfig | null = null;

/**
 * Frontend runtime configuration is loaded from a generated JSON file so the
 * React bundle stays environment-agnostic and deployable across stages.
 */
export async function loadRuntimeConfig(): Promise<RuntimeConfig> {
  if (cachedConfig) {
    return cachedConfig;
  }

  const response = await fetch("/runtime-config.json");
  if (!response.ok) {
    throw new Error("Unable to load runtime configuration.");
  }

  cachedConfig = (await response.json()) as RuntimeConfig;
  return cachedConfig;
}

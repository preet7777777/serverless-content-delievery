# Architecture Overview Diagram

## Implementation View

```mermaid
flowchart LR
    U[User Browser]
    FE[React Frontend]
    API[Upload Control Plane<br/>API Gateway or Local Java API]
    AUTH[Cognito Login<br/>Production Only]
    RAW[(Raw Images Bucket)]
    PROC[Image Processor<br/>Lambda or Local Poller]
    OPT[(Optimized Images Bucket)]
    CDN[CloudFront or<br/>Local Asset URL]
    CFG[Shared Config]

    U --> FE
    FE --> CFG
    FE --> API
    API --> AUTH
    API --> RAW
    U -->|Direct Upload via Pre-Signed URL| RAW
    RAW --> PROC
    PROC --> OPT
    U --> CDN
    CDN --> OPT
```

## Reading Notes

- Cognito belongs to login and token validation for the control plane, not to the direct upload path.
- The upload control path is synchronous and lightweight.
- The image optimization path is asynchronous and event-driven.
- The delivery path is cache-first in production and bucket-backed in local testing.

# Production AWS Architecture

```mermaid
flowchart LR
    U[User Browser]
    FE[S3 Static Frontend]
    APIG[API Gateway]
    COG[Cognito Login]
    URLL[Java Lambda<br/>Upload URL Service]
    RAW[(S3 Raw Bucket)]
    EVT[S3 Event Notification]
    PROCL[Java Lambda<br/>Image Processor]
    OPT[(S3 Optimized Bucket)]
    CF[CloudFront]

    U --> FE
    FE --> COG
    FE --> APIG
    APIG --> COG
    APIG --> URLL
    URLL --> RAW
    U -->|Direct Upload via Pre-Signed URL| RAW
    RAW --> EVT
    EVT --> PROCL
    PROCL --> OPT
    U --> CF
    CF --> OPT
```

## Reading Notes

- Cognito is responsible for login and token issuance.
- API Gateway uses Cognito-authenticated requests to protect upload authorization.
- The browser uploads directly to S3 only after the backend grants a short-lived pre-signed URL.
- CloudFront serves optimized assets and keeps the read-heavy path away from the control plane.

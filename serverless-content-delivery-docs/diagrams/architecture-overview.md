# Architecture Overview Diagram

```mermaid
flowchart LR
    U[User] --> FE[Frontend on S3]
    FE --> APIG[API Gateway]
    APIG --> L1[Lambda: Upload URL Generator]
    L1 --> S3R[(S3 Raw Bucket)]
    U -->|Direct Upload| S3R
    S3R --> EVT[S3 Event Notification]
    EVT --> L2[Lambda: Image Processor]
    L2 --> S3O[(S3 Optimized Bucket)]
    U --> CF[CloudFront]
    CF --> S3O
```

## Reading Notes

- The upload control path is synchronous and lightweight.
- The image optimization path is asynchronous and event-driven.
- The delivery path is cache-first through CloudFront.

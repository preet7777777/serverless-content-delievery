# Request Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant API as API Gateway
    participant URL as URL Lambda
    participant Raw as S3 Raw
    participant Proc as Processing Lambda
    participant Opt as S3 Optimized
    participant CDN as CloudFront

    User->>Frontend: Select image
    Frontend->>API: Request upload URL
    API->>URL: Invoke
    URL-->>Frontend: Return pre-signed URL
    Frontend->>Raw: Upload image
    Raw->>Proc: Emit object-created event
    Proc->>Raw: Read original object
    Proc->>Opt: Write optimized image + thumbnail
    User->>CDN: Request optimized image
    CDN->>Opt: Fetch on cache miss
```

## Reading Notes

- The browser never uploads the image body to API Gateway.
- Processing begins only after S3 persists the object.

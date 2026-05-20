# Local Development Architecture

```mermaid
flowchart LR
    U[User Browser]
    FE[Frontend Container<br/>localhost:5173]
    API[backend-local<br/>localhost:8080]
    LS[LocalStack S3<br/>localhost:4566]
    POLL[Local Processor Poller]
    CFG[Local Profile Config]

    U --> FE
    FE --> CFG
    FE --> API
    API -->|Pre-Signed PUT URL| U
    U -->|Direct Upload| LS
    LS --> POLL
    POLL --> LS
    FE -->|View Optimized Asset| LS
```

## Reading Notes

- This local path is designed for end-to-end verification without requiring real AWS credentials.
- Local auth bypass is intentional so browser uploads can be tested without a live Cognito login flow.
- The local processor polls raw storage because the local setup does not rely on full S3 event plumbing.

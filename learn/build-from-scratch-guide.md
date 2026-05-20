# Build From Scratch Guide

## Why This File Exists

This file is for the case where the student does **not** get the full project code.

That means she must learn:

- what to build first
- which files to create
- what each file should do
- how all parts connect

This is the missing bridge between:

- understanding the project
- building the project from scratch

## Final Goal

By the end, she should be able to build a guided version of the project herself:

- browser client
- upload authorization backend
- image processor backend
- local testing setup
- simple infrastructure thinking

## Build Order

Always build in this order:

1. understand the full flow
2. define the API contract
3. define config
4. build backend request models
5. build backend validation
6. build backend upload authorization service
7. build backend upload handler
8. build image processing service
9. build image processor handler
10. build browser upload API caller
11. build upload UI
12. build local testing flow

If she changes this order, the project will feel much harder.

## Full Project Tree To Build

```text
project-root/
├── api/
│   └── openapi.yaml
├── backend/
│   ├── pom.xml
│   ├── shared/
│   │   ├── pom.xml
│   │   └── src/main/java/com/serverless/contentdelivery/shared/
│   │       ├── config/
│   │       │   └── AppConfig.java
│   │       ├── domain/
│   │       │   ├── UploadAuthorizationRequest.java
│   │       │   └── UploadAuthorizationResponse.java
│   │       ├── http/
│   │       │   └── JsonMapperFactory.java
│   │       ├── validation/
│   │       │   └── UploadRequestValidator.java
│   │       ├── aws/
│   │       │   └── AwsClientFactory.java
│   │       └── service/
│   │           ├── UploadAuthorizationService.java
│   │           └── ImageProcessingService.java
│   ├── upload-url-lambda/
│   │   ├── pom.xml
│   │   └── src/main/java/com/serverless/contentdelivery/upload/
│   │       └── UploadUrlHandler.java
│   ├── image-processor-lambda/
│   │   ├── pom.xml
│   │   └── src/main/java/com/serverless/contentdelivery/processor/
│   │       └── ImageProcessorHandler.java
│   └── local-dev-runner/
│       ├── pom.xml
│       └── src/main/java/com/serverless/contentdelivery/local/
│           ├── LocalDevelopmentApplication.java
│           ├── LocalUploadAuthorizationController.java
│           └── LocalProcessorPoller.java
├── config/
│   ├── shared-environment.json
│   └── local-overrides.json
├── scripts/
│   └── render-config.mjs
├── frontend/
│   ├── package.json
│   ├── public/
│   │   └── runtime-config.json
│   └── src/
│       ├── main.tsx
│       ├── App.tsx
│       ├── styles.css
│       ├── types/
│       │   └── env.ts
│       ├── config/
│       │   └── runtime.ts
│       ├── api/
│       │   └── uploadApi.ts
│       └── components/
│           ├── UploadForm.tsx
│           └── AssetStatusCard.tsx
└── docker-compose.local.yml
```

## Section 1: Start With The API Contract

### File

`api/openapi.yaml`

### Why Create This First

Because backend and browser both need one agreed contract.

### What This File Should Describe

- API path
- request method
- request body
- response body
- auth requirement

### What This File Does

It tells everyone:

- what request the browser will send
- what the backend must return

### Main Endpoint

`POST /v1/uploads/presign`

### Request Fields

- `fileName`
- `contentType`
- `sizeBytes`

### Response Fields

- `objectKey`
- `uploadUrl`
- `expiresAt`
- `assetBaseUrl`
- `optimizedAssetUrl`
- `thumbnailAssetUrl`

## Section 2: Create Shared Config

### Files

- `config/shared-environment.json`
- `config/local-overrides.json`
- `scripts/render-config.mjs`

### What These Files Do

#### `shared-environment.json`

This keeps the main project configuration:

- region
- bucket names
- prefixes
- API base URL
- auth values

#### `local-overrides.json`

This changes values for local testing:

- local API URL
- local asset URL
- local auth placeholders

#### `render-config.mjs`

This reads config and generates:

- frontend runtime config
- infrastructure parameter config

### Why This Matters

It teaches a very important backend rule:

configuration should be separate from business logic.

## Section 3: Build Shared Backend Models

### File

`UploadAuthorizationRequest.java`

### What It Does

Stores upload request data from the browser:

- file name
- content type
- file size

### File

`UploadAuthorizationResponse.java`

### What It Does

Stores response data sent back by backend:

- signed upload URL
- generated object key
- expiry time
- final asset URLs

### Why These Files Matter

They make the request and response explicit and easy to reason about.

## Section 4: Build AppConfig

### File

`AppConfig.java`

### What It Does

Reads environment variables and creates one shared config object.

### Main Things It Should Hold

- region
- raw bucket
- optimized bucket
- prefixes
- max upload size
- allowed mime types
- CDN base URL
- local settings

### Why This File Matters

Without this file, values get scattered everywhere.

## Section 5: Build JSON Support

### File

`JsonMapperFactory.java`

### What It Does

Creates one shared JSON mapper for reading and writing request/response bodies.

### Why This File Matters

Handlers need to parse JSON safely and consistently.

## Section 6: Build Validation

### File

`UploadRequestValidator.java`

### What It Should Check

- request is present
- file name is present
- content type is allowed
- size is within limit

### Why This File Matters

Validation protects the backend from bad or unsafe input.

## Section 7: Build AWS Client Factory

### File

`AwsClientFactory.java`

### What It Does

Creates reusable S3 clients and S3 presigner instances.

### Why This File Matters

This keeps AWS client creation in one place and supports:

- real AWS
- LocalStack

## Section 8: Build UploadAuthorizationService

### File

`UploadAuthorizationService.java`

### This Is One Of The Most Important Files

It contains the main business logic for upload authorization.

### What This File Should Do

1. validate request
2. generate safe object key
3. create pre-signed URL
4. build response object

### What This File Should Not Do

- parse HTTP request
- build HTTP status codes
- contain UI logic

### Why This File Matters

This is the heart of the upload authorization backend.

## Section 9: Build UploadUrlHandler

### File

`UploadUrlHandler.java`

### What This File Does

This is the Lambda entrypoint for the upload API.

### Main Job

- receive HTTP request
- parse body
- call service
- return JSON response

### Important Rule

Keep it thin.

### Thin Handler Means

- request in
- service call
- response out

## Section 10: Build ImageProcessingService

### File

`ImageProcessingService.java`

### This Is Another Core File

It contains the main logic for processing raw images.

### What This File Should Do

1. read raw image from storage
2. create optimized image
3. create thumbnail
4. write outputs to optimized storage

### Why This File Matters

It teaches backend data transformation.

## Section 11: Build ImageProcessorHandler

### File

`ImageProcessorHandler.java`

### What This File Does

This is the Lambda entrypoint for processing uploaded images.

### Main Job

- receive S3 event
- get bucket and key
- call image processing service

### Important Rule

Again, keep it thin.

## Section 12: Build Local Runner

### Files

- `LocalDevelopmentApplication.java`
- `LocalUploadAuthorizationController.java`
- `LocalProcessorPoller.java`

### Why These Files Exist

They help the student test the whole app locally without full AWS deployment.

### What They Do

#### `LocalDevelopmentApplication.java`

- starts small HTTP server
- registers routes
- starts poller

#### `LocalUploadAuthorizationController.java`

- handles local upload authorization request
- calls upload authorization service

#### `LocalProcessorPoller.java`

- checks raw storage repeatedly
- triggers processing for new files

## Section 13: Build Browser Runtime Config

### Files

- `frontend/src/types/env.ts`
- `frontend/src/config/runtime.ts`

### What They Do

#### `env.ts`

Defines config shape and API data shapes.

#### `runtime.ts`

Loads runtime config from generated JSON file.

### Why These Files Matter

The browser should know:

- API URL
- upload path
- limits
- auth settings

without hardcoding them.

## Section 14: Build Browser API Client

### File

`frontend/src/api/uploadApi.ts`

### What It Does

Contains browser-side request functions:

- request upload authorization
- upload file directly

### Why This File Matters

It separates browser API logic from UI components.

## Section 15: Build Upload UI

### Files

- `UploadForm.tsx`
- `AssetStatusCard.tsx`
- `App.tsx`

### What Each File Does

#### `UploadForm.tsx`

- let user choose file
- validate file
- call upload API
- upload file directly

#### `AssetStatusCard.tsx`

- show upload result
- show output URLs
- explain async state

#### `App.tsx`

- load runtime config
- render main page
- manage upload state

## Section 16: Build Local Testing Setup

### Files

- `backend/Dockerfile.local`
- `frontend/Dockerfile.local`
- `docker-compose.local.yml`

### What They Do

#### Backend Dockerfile

- build local Java runner
- run local backend

#### Frontend Dockerfile

- start browser app locally

#### Docker Compose

- start frontend
- start backend
- start LocalStack

## Section 17: Build Order By Day

If the student is building while learning, use this order:

### Days 1-4

- understand architecture
- draw diagrams
- understand request flow

### Days 5-6

- create `openapi.yaml`
- create request and response models
- create config files

### Days 7-8

- create validator
- create AWS client factory
- create upload authorization service

### Days 9-10

- create upload handler
- create image processing service
- create image processor handler

### Days 11-12

- create browser API file
- create upload form
- create main UI flow

### Days 13-14

- create local runner
- create Docker local setup
- test the full flow

### Day 15

- revise
- explain project
- practice interview answers

## Section 18: Most Important Teacher Rule

Do not make her copy code blindly.

For every file, ask:

1. Why do we need this file?
2. What does this file do?
3. What data comes in?
4. What data goes out?
5. Which layer should own this logic?

## Final Success Check

She is ready if she can say:

- which file to create first
- what each backend file does
- what each browser file does
- how request flows through the system
- how to build a guided version without seeing the whole repo

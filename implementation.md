# Implementation Tracker

## Objective

Turn the documentation-first repository into an implementation-ready monorepo with:

- React frontend for upload orchestration and asset delivery UX
- Java Lambda backend for upload URL issuance and image processing
- AWS infrastructure templates aligned with the documented architecture
- One shared environment definition that drives frontend, backend, and deployment configuration
- Cognito as the authentication boundary for the control-plane API
- A 15-day beginner-friendly learning path for teaching the full project from scratch

## Source Guidance

The implementation is guided by the architecture documents under `serverless-content-delivery-docs/documentation`, especially:

- `03-system-architecture.md`
- `04-end-to-end-request-flow.md`
- `07-api-gateway-architecture.md`
- `08-lambda-architecture.md`
- `13-security-architecture.md`
- `17-java-engineering-best-practices.md`
- `20-testing-strategy.md`
- `22-production-improvements.md`

## Principles

- Keep the control plane and data plane separate.
- Use Cognito for login and control-plane token validation in production.
- Let clients upload directly to S3 through narrowly scoped pre-signed URLs.
- Treat all uploads as untrusted input.
- Keep Java Lambdas lightweight and framework-free.
- Centralize environment management in one shared config file.
- Make API and frontend flows self-documenting with descriptive comments.
- Prefer explicit contracts over implicit conventions.

## Planned Structure

- `frontend/`
  React client for authenticated upload initiation and delivery UX
- `backend/`
  Java multi-module Maven project for shared code, Lambda handlers, and a local integration runner
- `infra/`
  AWS SAM template and generated parameter artifacts
- `api/`
  OpenAPI contract for the control-plane API
- `config/`
  Shared environment source of truth with local override support
- `scripts/`
  Config rendering utilities
- `docker/`
  LocalStack bootstrap files for local end-to-end testing
- `serverless-content-delivery-docs/diagrams/`
  Markdown-based architecture diagrams for overview, local testing, and production AWS flow
- `learn/`
  Day-by-day teaching plan for beginners with simple explanations, diagrams, exercises, and interview preparation

## Progress

- [x] Review the architecture documentation and extract implementation constraints.
- [x] Define the target monorepo shape.
- [x] Add this implementation tracker to guide the build-out.
- [x] Add shared environment source and rendering pipeline.
- [x] Scaffold React frontend with runtime config consumption.
- [x] Scaffold Java backend with shared config, validation, and handlers.
- [x] Add infrastructure template for S3, API Gateway, Cognito, Lambda, and CloudFront.
- [x] Add OpenAPI descriptions for the upload control-plane API.
- [x] Verify generated config artifacts locally.
- [x] Verify frontend structure locally.
- [x] Build the frontend locally.
- [x] Add a Docker-based local integration path for frontend, upload API, storage, and processing.
- [x] Verify the shared backend modules compile locally.
- [ ] Verify the local Java runner build end to end inside Docker.
- [ ] Verify the Docker Compose local stack on a machine with Docker access.
- [x] Add architecture diagrams as markdown files in the repository.
- [x] Add a 15-day structured beginner learning track in markdown files.

## Open Decisions

- Keep image processing synchronous inside the processor Lambda for the first version.
- Use one shared environment definition for the current deployment model, with dummy Cognito values checked in as placeholders.
- Keep delivery private behind CloudFront origin access control.
- Leave advanced resilience improvements such as queue buffering as a later phase.
- Use a local Java runner with LocalStack for local end-to-end testing instead of trying to emulate Cognito and CloudFront exactly.

## Risks

- The local Java runner Docker image still needs one full successful build confirmation after the recent Maven and Dockerfile fixes.
- Image processing implementation is intentionally starter-grade until AWS credentials and build tooling are available.
- Docker Compose validation depends on Docker being available in the execution environment.

## Diagram Files

- `serverless-content-delivery-docs/diagrams/architecture-overview.md`
- `serverless-content-delivery-docs/diagrams/local-development-architecture.md`
- `serverless-content-delivery-docs/diagrams/production-aws-architecture.md`
- `serverless-content-delivery-docs/diagrams/request-sequence.md`
- `serverless-content-delivery-docs/diagrams/data-lifecycle.md`

## Learning Files

- `learn/README.md`
- `learn/day01.md` to `learn/day15.md`

## Next Steps

1. Keep `config/shared-environment.json` as the single deployment source of truth.
2. Run `node scripts/render-config.mjs` whenever config changes.
3. Run `node scripts/render-config.mjs --profile local` before launching the local Docker stack.
4. Run `docker compose -f docker-compose.local.yml up --build` for local end-to-end testing.
5. Confirm the backend local runner image builds successfully after the latest Maven fixes.
6. Upload a test image through the browser and verify optimized and thumbnail outputs.
7. Deploy the SAM stack with environment-specific parameter overrides generated from the shared config.
8. Use the `learn/` folder as the guided teaching path for the student.

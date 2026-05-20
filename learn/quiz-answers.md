# Quiz Answers

## Day 1

- Frontend is what the user sees.
- Backend contains the logic and rules.
- API is a way for frontend and backend to communicate.
- We avoid backend upload for large files because it increases backend load.
- Storage keeps raw and processed files safely.

## Day 2

- The project reads config like region, bucket names, prefixes, limits, and delivery URLs.
- Config is not hardcoded so the app can run in different environments.
- If bucket names change, config should change without rewriting the code.

## Day 3

- `uploadApi.ts` talks to the backend.
- `async` and `await` are used because network requests take time.
- Browser code should stay simple because the backend owns important rules.

## Day 4

- Before upload: login and upload authorization.
- During upload: browser sends file directly to storage.
- After upload: processing and final delivery happen.
- Backend should not carry the file body because direct upload scales better.

## Day 5

- The upload authorization API exists to safely create upload permission.
- Browser sends metadata first because backend only needs to validate and sign.
- Backend should validate type, size, and request shape.

## Day 6

- Authentication means who the user is.
- Authorization means what the user can do.
- Upload authorization should be protected because otherwise anyone could request upload URLs.

## Day 7

- S3 is cloud object storage.
- Direct upload means browser uploads straight to storage.
- Pre-signed URL is a short-lived permission link for one upload.

## Day 8

- Asynchronous processing means work happens after upload.
- It is useful because upload stays fast.
- The processor creates an optimized image and a thumbnail.

## Day 9

- Backend must validate again because browser checks are not enough for security.
- Backend should generate object keys so it controls storage paths.
- Final image may not be ready instantly because processing happens later.

## Day 10

- Handler should stay thin to keep transport logic separate from business logic.
- Validator owns request rule checking.
- Service owns upload authorization logic.

## Day 11

- The image processor reads raw input and writes derived outputs.
- There are two outputs because full optimized image and thumbnail serve different needs.
- Idempotent thinking helps repeated processing stay safe.

## Day 12

- Docker helps run the system in a controlled local environment.
- LocalStack helps simulate storage locally.
- A local runner helps test the full flow before real deployment.

## Day 13

- Local tools and production tools can differ, but the flow should stay similar.
- Production uses Cognito, API Gateway, Lambda, S3, and CloudFront.
- Local testing uses simpler replacements for learning and verification.

## Day 14

- Debug one request through one path.
- Check API, raw storage, processor, optimized output, and delivery path.
- Upload success does not guarantee processing success.

## Day 15

- A good explanation starts with the problem, then architecture, then flow.
- Clear and simple answers are better than overly advanced language.
- The student should be able to explain the project in 30 seconds, 1 minute, and 5 minutes.

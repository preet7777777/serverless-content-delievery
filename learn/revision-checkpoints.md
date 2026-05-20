# Revision Checkpoints

## Why This File Exists

The student should not only keep moving forward.
She should pause and revise in checkpoints so the base becomes strong.

## Checkpoint 1: After Day 4

She should be able to explain:

- what backend means
- what API means
- what storage means
- what authentication means
- full project flow in simple language

Ask these questions:

1. What happens before upload?
2. What happens during upload?
3. What happens after upload?
4. Why is backend not carrying the file body?

## Checkpoint 2: After Day 8

She should be able to explain:

- upload authorization API
- pre-signed URL
- S3 raw bucket
- async image processing
- Lambda role

Ask these questions:

1. Why do we use pre-signed URLs?
2. Why is processing asynchronous?
3. What is control plane in this project?
4. What is data plane in this project?

## Checkpoint 3: After Day 12

She should be able to explain:

- backend request lifecycle
- upload handler
- validation
- image processor
- local testing flow

Ask these questions:

1. What is the job of the upload handler?
2. What belongs in a service?
3. What belongs in validation?
4. How do we test the whole app locally?

## Final Checkpoint: After Day 15

She should be able to explain:

- complete architecture
- why each service exists
- one full upload journey
- local vs production flow
- major failure points

Ask these questions:

1. Explain the project in 1 minute.
2. Why not upload through backend?
3. What does Cognito do?
4. What does the image processor do?
5. How would you debug a missing final image?

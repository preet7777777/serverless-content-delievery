# Mock Interview

## How To Use This File

Use this after Day 15.

Ask the student the questions one by one.

Rules:

- let her answer first
- do not interrupt too early
- if she gets stuck, ask a smaller question
- after her answer, compare with the sample answer

## Warm-Up Round

### 1. Tell me about yourself and this project.

Sample answer:

I am learning backend and cloud development, and this project helped me understand how a browser uploads an image safely, how the backend gives a pre-signed URL, how storage keeps the raw image, how a processor creates optimized versions, and how the final image is delivered quickly.

### 2. What problem does this project solve?

Sample answer:

It solves the problem of large image uploads being slow and expensive. Instead of sending images through the backend, it uses direct upload and background processing.

### 3. What are the main parts of your system?

Sample answer:

The main parts are the browser client, Cognito login, upload authorization API, raw image storage, image processor, optimized image storage, and delivery layer.

## Backend Round

### 4. What does the upload authorization API do?

Sample answer:

It validates the file metadata and returns a short-lived pre-signed URL so the browser can upload directly to storage.

### 5. Why did you keep the handler thin?

Sample answer:

Because the handler should mostly deal with request and response. The business logic should stay in services so the code is easier to test and understand.

### 6. What does the validator do?

Sample answer:

It checks rules like allowed file type, allowed size, and required request fields before the backend creates upload permission.

### 7. Why should the backend generate the object key?

Sample answer:

Because the server should control naming and storage paths. Trusting the client too much can create security and organization problems.

## Storage And Processing Round

### 8. Why not upload the file through the backend?

Sample answer:

Because files can be large. If the backend carries every file, it becomes slower, more expensive, and harder to scale.

### 9. What is a pre-signed URL?

Sample answer:

It is a short-lived, limited permission URL that allows one upload directly to storage.

### 10. What happens after the upload succeeds?

Sample answer:

The raw image is stored first, then the image processor reads it, creates optimized outputs and a thumbnail, and stores those derived files.

### 11. Why is image processing asynchronous?

Sample answer:

So the user does not wait for heavy image work during upload. Upload stays fast and processing happens in the background.

## System Design Round

### 12. What is the control plane in this project?

Sample answer:

The control plane is the part that validates and decides, mainly the upload authorization API.

### 13. What is the data plane in this project?

Sample answer:

The data plane is the part where the actual file moves, which is the direct upload to storage.

### 14. What does Cognito do here?

Sample answer:

Cognito handles login and token issuance so the upload authorization API can be protected.

### 15. What does CloudFront do?

Sample answer:

CloudFront helps deliver the final optimized image quickly using caching and edge delivery.

## Debugging Round

### 16. A user says upload worked but image is not showing. What do you check?

Sample answer:

I would check the upload API response, then whether the raw file exists, then whether the processor ran, then whether optimized output was created, and finally whether the delivery path is correct.

### 17. What if the processor fails?

Sample answer:

The raw image may still exist but the final optimized image may be missing. I would inspect processor logs and output storage paths.

## Closing Round

### 18. What did you personally learn from this project?

Sample answer:

I learned how to separate browser logic from backend logic, how direct upload works, how pre-signed URLs improve scalability, and how asynchronous processing helps system design.

### 19. What would you improve next?

Sample answer:

I would add stronger tests, better failure handling, and complete production validation with real cloud deployment.

### 20. Explain the whole project in one minute.

Sample answer:

This project is a serverless image upload and delivery system. A user logs in, requests upload authorization, and receives a pre-signed URL. The browser uploads the image directly to storage. Then a background processor creates optimized versions and thumbnails. The final assets are delivered quickly through the delivery layer. This design keeps upload fast, reduces backend load, and teaches backend, storage, and system design fundamentals.

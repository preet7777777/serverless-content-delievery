# Interview Questions

## Beginner Level

### What is a backend?

Good answer:

The backend contains the logic of the application. It handles validation, APIs, storage rules, and business logic.

### What is an API?

Good answer:

An API is a contract that lets one part of the system talk to another part in a structured way.

### What is authentication?

Good answer:

Authentication checks who the user is.

### What is authorization?

Good answer:

Authorization checks what the user is allowed to do.

## Project-Specific Level

### What does this project do?

Good answer:

This project allows a user to upload an image safely, process it in the background, and deliver the optimized result quickly.

### Why use pre-signed URLs?

Good answer:

They give short-lived permission to upload one file directly to storage without exposing broad access.

### Why is direct upload better than backend upload?

Good answer:

Direct upload keeps the backend out of the large file transfer path, which improves scalability and reduces backend load.

### Why is image processing asynchronous?

Good answer:

So the upload path stays fast and the heavy image work happens in the background.

### What does Cognito do here?

Good answer:

Cognito handles login and token issuance so the upload authorization API can be protected.

## Intern-Level System Design

### What are the main parts of the system?

Good answer:

Browser client, Cognito login, upload authorization API, raw storage, image processor, optimized storage, and delivery layer.

### What is the control plane?

Good answer:

The control plane is the part that validates and decides, like the upload authorization API.

### What is the data plane?

Good answer:

The data plane is the part where the real file moves, like the direct upload to storage.

### What happens if the processor fails?

Good answer:

The raw file may exist but the optimized output may not be ready. We would debug the processor stage and check storage and logs.

### How would you debug a user complaint that the image is not visible?

Good answer:

I would trace the request step by step: API request, upload success, raw storage object, processing stage, and final optimized asset path.

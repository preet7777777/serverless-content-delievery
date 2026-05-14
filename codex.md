You are a senior Cloud Architect, Java Backend Engineer, and Technical Documentation Expert.

I want you to create a COMPLETE, PRODUCTION-GRADE, BEGINNER-FRIENDLY documentation repository in Markdown (.md) format for the following project:

# Project Title
Serverless Content Delivery & Optimization API (Media Focus)

# Project Goal
Build a serverless system where users upload images, which are then optimized, cached, and delivered globally using AWS.

# High-Level Architecture

1. Frontend (hosted on S3 static hosting)
2. Frontend requests secure pre-signed upload URL from API Gateway
3. API Gateway invokes Java AWS Lambda
4. Lambda generates S3 pre-signed upload URL
5. Client uploads image directly to S3 using the pre-signed URL
6. S3 Event Notification triggers another Java Lambda
7. Lambda resizes image, compresses it, and generates thumbnail
8. Optimized image is stored in optimized S3 bucket/folder
9. Users access images through CloudFront CDN for global caching and lower latency

# Tech Constraints

- Backend language: Java
- AWS SDK: AWS SDK for Java v2
- Build Tool: Maven
- Image processing library: Thumbnailator
- Architecture style: Serverless + Event Driven
- Avoid Spring Boot
- Use plain lightweight Java Lambda architecture
- NO ACTUAL CODE
- ONLY deep engineering knowledge and implementation understanding
- Must be beginner friendly but production-grade

# Audience
Assume the reader is:
- Beginner to intermediate
- Knows basic AWS concepts
- Wants deep understanding
- Wants to explain the project confidently in interviews
- Wants to implement the project later independently

# Extremely Important Rules

1. DO NOT GIVE CODE.
2. NO JAVA IMPLEMENTATION.
3. Explain everything conceptually and architecturally.
4. Focus on WHY, HOW, and TRADEOFFS.
5. Teach like a mentor and senior engineer.
6. Include diagrams using Mermaid.
7. Use real-world engineering practices.
8. Explain interview perspective.
9. Explain production considerations.
10. Explain failure scenarios.
11. Explain AWS free-tier safety.
12. Explain scalability.
13. Explain security deeply.
14. Explain Java-specific Lambda optimization deeply.

# Output Format Requirements

Generate a COMPLETE documentation repository with the following structure:

serverless-content-delivery-docs/
│
├── README.md
│
├── 01-project-overview.md
├── 02-business-problem.md
├── 03-system-architecture.md
├── 04-end-to-end-request-flow.md
├── 05-aws-services-deep-dive.md
├── 06-s3-architecture.md
├── 07-api-gateway-architecture.md
├── 08-lambda-architecture.md
├── 09-presigned-url-complete-guide.md
├── 10-image-processing-concepts.md
├── 11-thumbnail-generation.md
├── 12-cloudfront-cdn-guide.md
├── 13-security-architecture.md
├── 14-iam-roles-permissions.md
├── 15-observability-monitoring.md
├── 16-error-handling-strategy.md
├── 17-java-engineering-best-practices.md
├── 18-memory-cold-start-optimization.md
├── 19-maven-packaging-guide.md
├── 20-testing-strategy.md
├── 21-cost-optimization-free-tier.md
├── 22-production-improvements.md
├── 23-scaling-considerations.md
├── 24-interview-questions.md
├── 25-resume-explanation.md
│
└── diagrams/

# Documentation Depth Requirements

Each markdown file must contain:

1. Purpose of the component
2. Why this component exists
3. Why alternatives were not chosen
4. Request/response flow
5. Detailed architecture diagrams
6. Production considerations
7. Security concerns
8. Cost considerations
9. Scaling considerations
10. Common mistakes
11. Failure scenarios
12. Debugging mindset
13. Interview questions and answers
14. Real-world engineering best practices
15. Beginner-friendly explanation

# Mandatory Deep-Dive Topics

## README.md
Must include:
- Project overview
- Architecture image
- Technologies used
- End-to-end flow
- Learning outcomes
- Resume value
- Project milestones
- AWS services map

## Project Overview
Explain:
- Why content delivery matters
- Why media optimization matters
- Real-world examples (Instagram, Netflix, e-commerce, CDNs)

## Business Problem
Explain:
- Why direct uploads are needed
- Why backend upload is inefficient
- Why optimization reduces costs
- Why CDN matters

## System Architecture
Include:
- End-to-end architecture diagram
- Sequence diagrams
- Event-driven explanation
- Request lifecycle
- Data lifecycle

## AWS Services Deep Dive
For every AWS service explain:
- What it is
- Why we use it
- Why not alternatives
- Pricing
- Free tier
- Security
- Best practices
- Common interview questions

Services:
- S3
- API Gateway
- Lambda
- CloudFront
- IAM
- CloudWatch
- Event Notifications

## S3 Architecture
Explain:
- Bucket design
- Folder structure
- Naming strategy
- Lifecycle rules
- Versioning
- Encryption
- Access control
- Public vs private bucket

## API Gateway
Explain:
- REST APIs
- HTTP APIs
- Why API Gateway
- Rate limiting
- Authentication
- Request lifecycle
- Cost considerations

## Lambda Architecture
Explain deeply:
- Cold starts
- Execution lifecycle
- Statelessness
- Memory allocation
- Timeout strategy
- Event-driven compute
- Why avoid Spring Boot
- JVM behavior

## Pre-Signed URL
Explain deeply:
- Why secure upload matters
- How pre-signed URL works
- Security model
- Expiration
- MIME restrictions
- Upload abuse prevention

## Image Processing
Explain:
- Compression
- Resizing
- Thumbnail generation
- Quality tradeoffs
- Image formats
- WebP vs JPEG vs PNG

## CloudFront CDN
Explain deeply:
- CDN basics
- Edge locations
- Cache hit/miss
- TTL
- Invalidation
- Signed URLs
- Origin access
- Performance benefits

## Security Architecture
Explain:
- Least privilege IAM
- Bucket policy
- Private access
- Signed URLs
- Validation
- Malware concerns
- Upload restrictions
- DOS concerns

## Observability
Explain:
- CloudWatch Logs
- Metrics
- Monitoring
- Alerts
- Debugging failures
- Distributed tracing mindset

## Error Handling
Explain:
What happens if:
- upload fails
- Lambda timeout
- invalid image
- corrupt file
- S3 trigger failure
- CloudFront stale cache

## Java Engineering Best Practices
Explain:
- Plain Java Lambda architecture
- AWS SDK v2
- Dependency minimization
- Object reuse
- Static client reuse
- Packaging optimization
- Lightweight architecture

## Cold Start Optimization
Explain:
- Why Java cold starts happen
- GraalVM
- Quarkus vs plain Java
- Memory tradeoffs
- Startup optimization

## Maven Packaging
Explain:
- Uber JAR
- Shading
- Dependency minimization
- Package size optimization

## Testing Strategy
Explain:
- Unit testing mindset
- API testing
- Event testing
- Failure testing
- Load testing
- Integration testing

## Cost Optimization
Explain:
- Free tier limits
- Cost risks
- Ways to reduce Lambda cost
- CloudFront savings
- S3 optimization

## Production Improvements
Explain:
- retries
- DLQ
- event filtering
- logging strategy
- monitoring
- observability
- CI/CD
- Infrastructure as Code
- caching improvements

## Scaling
Explain:
- Millions of uploads
- Hot partitions
- CDN scaling
- Lambda concurrency
- Regional traffic spikes

## Interview Questions
Create:
- 50+ realistic interview questions
- Detailed strong answers

## Resume Explanation
Teach:
- How to explain project in interviews
- STAR method explanation
- Resume bullet points
- What to emphasize

# Writing Style

- Beginner friendly
- Deep but simple
- Use analogies
- Teach visually
- Use markdown tables
- Use Mermaid diagrams
- Explain tradeoffs
- Explain WHY behind decisions
- Include “Senior Engineer Notes”
- Include “Common Fresher Mistakes”
- Include “Interview Perspective”

Generate documentation one file at a time, starting with README.md.
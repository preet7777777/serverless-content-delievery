# Learn Serverless Content Delivery In 15 Days

## Goal

This folder is a complete beginner-friendly learning path for a student who:

- has little knowledge of Java
- has little knowledge of JavaScript
- knows only very basic programming concepts
- wants to become strong enough for internship interviews
- needs to understand both code and system design in simple language

## Big Promise

If she studies `4 hours every day` for `15 days`, she can build a strong foundation and understand this project end to end.

She will not become an expert in 15 days, but she can become:

- clear in fundamentals
- confident in explaining the project
- able to build the guided version of the app
- able to answer intern-level interview questions

## Total Study Time

- `15 days`
- `4 hours per day`
- `60 total hours`

## How To Use This Folder

1. Study one file per day.
2. Do not jump ahead.
3. First understand the concept.
4. Then read the diagram.
5. Then look at the code.
6. Then do the daily exercise.
7. End the day by explaining the topic in your own words.

## Support Files

- `teacher-guide.md`
- `revision-checkpoints.md`
- `interview-questions.md`
- `code-practice-plan.md`
- `mock-interview.md`
- `quiz-answers.md`
- `build-from-scratch-guide.md`
- `student-progress-tracker.md`
- `capstone-rubric.md`
- `homework-template.md`

## Daily Study Pattern

Use this pattern every day:

- `Hour 1`: learn concepts slowly
- `Hour 2`: read diagrams and understand flow
- `Hour 3`: connect concept to project code
- `Hour 4`: do exercise, revision, and interview practice

## What Was Added To Fill The Gaps

This learning path now includes:

- day-by-day lessons
- diagrams inside lesson files
- code reading guidance
- teacher guidance
- revision checkpoints
- interview questions
- code practice plan
- build-from-scratch guidance
- progress tracking
- final evaluation rubric
- day-by-day homework template

## 15-Day Learning Roadmap

| Day | File | Main Focus |
| --- | --- | --- |
| 1 | `day01.md` | Web basics, system basics, and project overview |
| 2 | `day02.md` | Java basics for backend work |
| 3 | `day03.md` | JavaScript basics needed to understand browser requests |
| 4 | `day04.md` | End-to-end system design of this project |
| 5 | `day05.md` | API thinking and upload authorization API |
| 6 | `day06.md` | Authentication, Cognito, and request security |
| 7 | `day07.md` | S3, direct upload, and pre-signed URLs |
| 8 | `day08.md` | Async processing, Lambda, and image workflow |
| 9 | `day09.md` | Full request lifecycle from browser to backend service |
| 10 | `day10.md` | Java upload URL backend implementation |
| 11 | `day11.md` | Image processor backend implementation |
| 12 | `day12.md` | Local testing with Docker and LocalStack |
| 13 | `day13.md` | Cloud architecture and deployment mindset |
| 14 | `day14.md` | Debugging, logging, testing, and failure cases |
| 15 | `day15.md` | Interview preparation and final project explanation |

## What She Should Be Able To Explain At The End

- What a backend is
- What an API is
- How a request moves through a backend
- Why direct upload is better than backend upload
- What S3 does in this project
- Why we use pre-signed URLs
- Why image processing is asynchronous
- What Lambda does
- What Cognito does
- Why CloudFront is useful
- How the full request flows from browser to final image
- How to explain this as a beginner system design project

## Important Rule

Do not memorize words only.

Always ask:

- What problem are we solving?
- Why are we using this service?
- What would break if we removed it?
- Where does this fit in the full system?

## Repo Files To Use Alongside Lessons

- [`implementation.md`](/home/preetsirohi/Desktop/serveless-content-delievery/implementation.md)
- [`api/openapi.yaml`](/home/preetsirohi/Desktop/serveless-content-delievery/api/openapi.yaml)
- [`backend/upload-url-lambda/src/main/java/com/serverless/contentdelivery/upload/UploadUrlHandler.java`](/home/preetsirohi/Desktop/serveless-content-delievery/backend/upload-url-lambda/src/main/java/com/serverless/contentdelivery/upload/UploadUrlHandler.java)
- [`backend/image-processor-lambda/src/main/java/com/serverless/contentdelivery/processor/ImageProcessorHandler.java`](/home/preetsirohi/Desktop/serveless-content-delievery/backend/image-processor-lambda/src/main/java/com/serverless/contentdelivery/processor/ImageProcessorHandler.java)
- [`backend/shared/src/main/java/com/serverless/contentdelivery/shared/service/UploadAuthorizationService.java`](/home/preetsirohi/Desktop/serveless-content-delievery/backend/shared/src/main/java/com/serverless/contentdelivery/shared/service/UploadAuthorizationService.java)
- [`backend/shared/src/main/java/com/serverless/contentdelivery/shared/service/ImageProcessingService.java`](/home/preetsirohi/Desktop/serveless-content-delievery/backend/shared/src/main/java/com/serverless/contentdelivery/shared/service/ImageProcessingService.java)
- [`frontend/src/api/uploadApi.ts`](/home/preetsirohi/Desktop/serveless-content-delievery/frontend/src/api/uploadApi.ts)

## Teacher Note

This folder is written in simple language on purpose.

The goal is not to sound advanced.
The goal is to build strong backend and system design basics first, then connect the browser side to that understanding.

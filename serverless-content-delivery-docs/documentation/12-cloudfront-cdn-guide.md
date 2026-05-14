# 12 CloudFront CDN Guide

## Purpose

This document explains why CloudFront is central to global image delivery, not just an optional acceleration layer.

## Beginner-Friendly Explanation

CloudFront is a global shortcut for image delivery. Instead of every user reaching all the way back to one storage location, a nearby edge server can return a cached copy much faster.

## Why This Component Exists

Media delivery is usually read-heavy. Once images are processed, the biggest long-term traffic pattern is repeated retrieval. CloudFront makes that path faster and cheaper by caching objects near users.

## CDN Basics

A CDN stores cached copies of content at distributed edge locations. Users are routed to nearby edges, which reduces latency and lowers repeated reads to the origin.

## Edge Locations

Edge locations matter because geography matters. A user far from the S3 region still gets low-latency delivery if the object is cached near them.

## Cache Hit Vs Cache Miss

- Cache hit:
  CloudFront already has the object and serves it immediately.
- Cache miss:
  CloudFront fetches the object from origin, then caches it for later requests.

## TTL

Time to live determines how long CloudFront keeps an object before revalidating or refetching. Longer TTLs improve cache hit rate, but they make content updates less immediate.

## Invalidation

Invalidation forces CloudFront to stop serving cached versions early. It is useful but should not be the primary content update strategy because it adds operational and cost complexity.

## Signed URLs And Signed Cookies

If delivery must be restricted, CloudFront can require signed access. That is separate from S3 upload pre-signed URLs and serves a different purpose.

## Origin Access

CloudFront should fetch from a private S3 origin through controlled origin access rather than relying on a public bucket when possible.

## Diagram

```mermaid
flowchart LR
    U[User] --> E[Nearest CloudFront Edge]
    E -->|Cache Hit| U
    E -->|Cache Miss| O[S3 Optimized Origin]
    O --> E
    E --> U
```

## Performance Benefits

- Lower latency for geographically distributed users.
- Reduced origin read load.
- Better resilience under flash traffic for popular assets.

## Why Alternatives Were Not Chosen

- Direct S3 access is simpler but weaker for caching control and origin protection.
- Building a custom CDN layer is unnecessary when CloudFront already provides the capability.

## Request And Response Flow

1. User requests an optimized image URL.
2. CloudFront checks the nearest edge cache.
3. On miss, CloudFront fetches from S3.
4. The response is cached according to policy.

## Production Considerations

- Decide whether object versioning or invalidation is your primary freshness strategy.
- Configure cache headers based on asset mutability.
- Monitor hit ratio and origin error rates.

## Security Concerns

- Prevent direct origin bypass where appropriate.
- Serve over HTTPS.
- Consider signed delivery if assets are not meant to be world-readable.

## Cost Considerations

- Better cache hit rate usually lowers origin cost and improves user experience.
- Poor cache policy can increase both CloudFront and origin cost.

## Scaling Considerations

- CloudFront is the main read-scaling layer.
- Popular content should be served almost entirely from edge caches once warmed.

## Common Mistakes

- Reusing the same object key for changed content without cache strategy.
- Making the S3 origin public and bypassable.
- Using very low TTLs that erase cache efficiency.

## Failure Scenarios

- CloudFront caches a 404 before processing completes.
- Origin access policy is wrong, causing 403 errors.
- Users see stale assets because object replacement was not versioned.

## Debugging Mindset

Look at:

- Cache hit or miss behavior
- Origin status codes
- Object key versioning
- TTL and invalidation history

## Interview Questions And Answers

- Why is CloudFront important even if S3 is highly available?
  Availability is not the same as low latency and efficient global delivery.
- What is usually better than frequent invalidation?
  Versioned object keys, because they make freshness explicit and CDN-friendly.

## Best Practices

- Prefer immutable object keys for changed content.
- Keep origins private and CDN policies intentional.

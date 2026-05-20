#!/bin/sh
set -eu

awslocal s3api create-bucket \
  --bucket scd-raw-assets-shared \
  --region ap-south-1 \
  --create-bucket-configuration LocationConstraint=ap-south-1 || true

awslocal s3api create-bucket \
  --bucket scd-optimized-assets-shared \
  --region ap-south-1 \
  --create-bucket-configuration LocationConstraint=ap-south-1 || true

awslocal s3api put-bucket-cors \
  --bucket scd-raw-assets-shared \
  --cors-configuration file:///etc/localstack/init/ready.d/raw-bucket-cors.json

awslocal s3api put-bucket-cors \
  --bucket scd-optimized-assets-shared \
  --cors-configuration file:///etc/localstack/init/ready.d/optimized-bucket-cors.json

awslocal s3api put-bucket-policy \
  --bucket scd-optimized-assets-shared \
  --policy file:///etc/localstack/init/ready.d/optimized-bucket-policy.json

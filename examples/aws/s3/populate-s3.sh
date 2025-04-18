#!/bin/bash
awslocal s3api create-bucket --bucket sample-bucket --region us-east-1
awslocal s3api put-bucket-acl --bucket sample-bucket --acl public-read
awslocal s3api get-bucket-acl --bucket sample-bucket
awslocal s3 cp /data/config/foo.yml s3://sample-bucket/
awslocal s3 cp /data/config/foo-db.yml s3://sample-bucket/
awslocal s3 cp /data/config/foo-dev.yml s3://sample-bucket/
awslocal s3 cp /data/config/foo-development.yml s3://sample-bucket/
awslocal s3 cp /data/config/foo-development-db.yml s3://sample-bucket/
awslocal s3 ls s3://sample-bucket/
#!/bin/bash
awslocal s3api create-bucket --bucket sample-bucket
awslocal s3api put-object --bucket sample-bucket --key foo.yml --body /data/config/foo.yml
awslocal s3api put-object --bucket sample-bucket --key foo-db.yml --body /data/config/foo-db.yml
awslocal s3api put-object --bucket sample-bucket --key foo-dev.yml --body /data/config/foo-dev.yml
awslocal s3api put-object --bucket sample-bucket --key foo-development.yml --body /data/config/foo-development.yml
awslocal s3api put-object --bucket sample-bucket --key foo-development-db.yml --body /data/config/foo-development-db.yml
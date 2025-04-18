#!/bin/bash
awslocal ssm put-parameter --name /config/application-default/foo --value "default" --type String
awslocal ssm put-parameter --name /config/application-development/bar --value "default" --type String
awslocal ssm put-parameter --name /config/foo-development/foo --value "bar" --type String
awslocal ssm put-parameter --name /config/foo-development/bar --value "foo" --type String

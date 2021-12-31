#!/bin/bash
awslocal ssm put-parameter --name /config/application-default/foo --value "default"
awslocal ssm put-parameter --name /config/application-development/bar --value "default"
awslocal ssm put-parameter --name /config/foo-development/foo --value "bar"
awslocal ssm put-parameter --name /config/foo-development/bar --value "foo"

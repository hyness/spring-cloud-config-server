#!/bin/bash
awslocal secretsmanager create-secret --name /secret/application-default/ --secret-string '{"foo": "default"}'
awslocal secretsmanager create-secret --name /secret/application-development/ --secret-string '{"bar": "default"}'
awslocal secretsmanager create-secret --name /secret/foo-development/ --secret-string '{"foo": "bar", "bar": "foo"}'

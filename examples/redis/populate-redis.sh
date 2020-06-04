#!/bin/bash
echo 'HMSET sample-app server.port "8100" sample.topic.name "test" test.property1 "property1"' | redis-cli

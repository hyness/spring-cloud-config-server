#!/bin/bash
echo 'HMSET redis-app-development server.port "8100" redis.topic.name "test" test.property1 "property1"' | redis-cli

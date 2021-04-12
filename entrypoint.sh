#!/bin/sh

java -cp /opt/spring-cloud-config-server ${JAVA_OPTS} ${BOOT_LAUNCHER} \
--server.port=8888 \
--spring.config.name=application "$@"

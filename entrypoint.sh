#!/bin/sh

java -cp /opt/spring-cloud-config-server ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher \
--server.port=8888 \
--spring.config.name=application "$@"
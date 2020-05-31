#!/bin/sh

java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar \
/opt/spring-cloud-config-server/spring-cloud-config-server.jar --server.port=8888 \
--spring.config.name=application "$@"

java -Djava.security.egd=file:/dev/./urandom -jar /opt/spring-cloud-config-server/spring-cloud-config-server.jar --server.port=8888 --spring.config.name=application
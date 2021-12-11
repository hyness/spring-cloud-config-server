ARG BUILD_TYPE=jdk
ARG JVM_TARGET=11
ARG JVM_TAG=${JVM_TARGET}-${BUILD_TYPE}-focal
ARG JVM_FROM=eclipse-temurin:${JVM_TAG}

FROM --platform=$BUILDPLATFORM $JVM_FROM as builder
LABEL org.opencontainers.image.authors="hyness <hyness@freshlegacycode.org>"
WORKDIR /build
COPY . ./
RUN sh gradlew -DjvmTarget=$JVM_TARGET -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .
RUN java -Djarmode=layertools -jar spring-cloud-config-server.jar extract

FROM $JVM_FROM
WORKDIR /opt/spring-cloud-config-server
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/application/ ./
COPY entrypoint.sh ./

ENV BOOT_LAUNCHER=org.springframework.boot.loader.JarLauncher

WORKDIR /
EXPOSE 8888
VOLUME /config
ENTRYPOINT ["sh", "/opt/spring-cloud-config-server/entrypoint.sh"]
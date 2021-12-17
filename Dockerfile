ARG JVM_TYPE=jre
ARG JVM_VERSION=11
ARG JVM_TARGET=${JVM_VERSION}
ARG JVM_BUILD_TAG=${JVM_VERSION}-jdk-focal
ARG JVM_RUN_TAG=${JVM_VERSION}-${JVM_TYPE}-focal
ARG BUILD_FROM=eclipse-temurin:${JVM_BUILD_TAG}
ARG RUN_FROM=eclipse-temurin:${JVM_RUN_TAG}

FROM --platform=$BUILDPLATFORM ${BUILD_FROM} as builder
LABEL org.opencontainers.image.authors="hyness <hyness@freshlegacycode.org>"
WORKDIR /build
COPY . ./
RUN sh gradlew -PjvmTarget=${JVM_TARGET} -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .
RUN java -Djarmode=layertools -jar spring-cloud-config-server.jar extract

FROM ${RUN_FROM}
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
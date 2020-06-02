FROM gradle:jdk8 as builder
LABEL maintainer="hyness <hyness@freshlegacycode.org>"
WORKDIR /build

COPY build.gradle.kts settings.gradle ./
COPY src/ src/
RUN gradle -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .

FROM openjdk:8-alpine
WORKDIR /
COPY --from=builder /build/spring-cloud-config-server.jar /opt/spring-cloud-config-server/
COPY entrypoint.sh /opt/spring-cloud-config-server/

EXPOSE 8888
VOLUME /config
ENTRYPOINT ["sh", "/opt/spring-cloud-config-server/entrypoint.sh"]
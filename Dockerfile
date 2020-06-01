FROM gradle:jdk8 as builder
LABEL maintainer="hyness <hyness@freshlegacycode.org>"
WORKDIR /opt/spring-cloud-config-server

COPY build.gradle.kts entrypoint.sh ./
COPY src/ src/
RUN gradle -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .

WORKDIR /opt/spring-cloud-config-server
COPY entrypoint.sh ./
RUN rm -rf .gradle build.gradle.kts src build

FROM openjdk:8-alpine
WORKDIR /
COPY --from=builder /opt/spring-cloud-config-server/spring-cloud-config-server.jar /opt/spring-cloud-config-server/
COPY entrypoint.sh /opt/spring-cloud-config-server/
EXPOSE 8888
VOLUME /config
ENTRYPOINT ["sh", "/opt/spring-cloud-config-server/entrypoint.sh"]


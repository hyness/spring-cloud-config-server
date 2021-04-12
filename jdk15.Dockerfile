FROM adoptopenjdk/openjdk15:alpine-slim as builder
LABEL maintainer="hyness <hyness@freshlegacycode.org>"
WORKDIR /build

COPY . ./
RUN sh gradlew -DjvmTarget=15 -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .
RUN java -Djarmode=layertools -jar spring-cloud-config-server.jar extract

FROM adoptopenjdk/openjdk15:alpine-slim
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

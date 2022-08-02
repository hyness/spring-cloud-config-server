ARG JVM_TARGET
ARG JVM_FROM=eclipse-temurin:$JVM_TARGET-focal
FROM $JVM_FROM as builder

ARG JVM_TARGET
ARG JVM_FROM
LABEL maintainer="hyness <hyness@freshlegacycode.org>"
WORKDIR /build

COPY . ./
RUN sh gradlew -DjvmTarget=$(if [ $JVM_TARGET -eq 8 ]; then echo '1.8'; else echo $JVM_TARGET; fi) -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .
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
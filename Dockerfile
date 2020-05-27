FROM gradle:jdk8
LABEL maintainer="hyness <hyness@freshlegacycode.org>"
WORKDIR /opt/spring-cloud-config-server

COPY build.gradle.kts entrypoint.sh ./
COPY src/ src/
RUN gradle -console verbose --no-build-cache --no-daemon assemble && mv build/libs/* .

WORKDIR /opt/spring-cloud-config-server
COPY entrypoint.sh ./
RUN rm -rf .gradle build.gradle.kts src build

EXPOSE 8888
VOLUME /config
ENTRYPOINT ["sh", "/opt/spring-cloud-config-server/entrypoint.sh"]

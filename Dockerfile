FROM gradle:jdk14
LABEL maintainer="hyness <hyness@freshlegacycode.org>"

COPY build.gradle.kts .
RUN gradle -console verbose --no-build-cache --no-daemon assemble

COPY entrypoint.sh build/libs/* /opt/spring-cloud-config-server/
RUN rm -rf build.gradle.kts build

EXPOSE 8888
VOLUME /config
ENTRYPOINT ["sh", "/opt/spring-cloud-config-server/entrypoint.sh"]

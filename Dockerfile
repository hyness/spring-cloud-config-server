FROM maven:alpine
MAINTAINER hyness <hyness@freshlegacycode.org>

EXPOSE 8888
COPY . /opt/spring-cloud-config-server/
WORKDIR /opt/spring-cloud-config-server/
RUN mvn package
VOLUME /config
WORKDIR /
ENTRYPOINT ["sh", "/opt/spring-cloud-config-server/entrypoint.sh"]

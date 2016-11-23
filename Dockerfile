FROM maven:alpine
MAINTAINER hyness <hyness@gmail.com>

EXPOSE 8888
COPY . /opt/spring-cloud-config-server/
WORKDIR /opt/spring-cloud-config-server/
RUN mvn package
VOLUME /config
WORKDIR /
CMD java -Djava.security.egd=file:/dev/./urandom -jar /opt/spring-cloud-config-server/target/spring-cloud-config-server-*.jar --server.port=8888 --spring.config.name=application

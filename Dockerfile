FROM java:8-jdk
MAINTAINER hyness <hyness@gmail.com>

ENV SCCS_VERSION 1.0.3.RELEASE
EXPOSE 8888
WORKDIR /opt/spring-cloud-config-server/
VOLUME /opt/spring-cloud-config-server/config
RUN curl -LO http://search.maven.org/remotecontent?filepath=org/springframework/cloud/spring-cloud-config-server/${SCCS_VERSION}/spring-cloud-config-server-${SCCS_VERSION}-exec.jar
CMD java -Djava.security.egd=file:/dev/./urandom -jar spring-cloud-config-server-${SCCS_VERSION}-exec.jar

FROM java:openjdk-7-jdk
EXPOSE 8888
WORKDIR /opt/spring-cloud-config-server/
RUN curl -LO http://search.maven.org/remotecontent?filepath=org/springframework/cloud/spring-cloud-config-server/1.0.0.RELEASE/spring-cloud-config-server-1.0.0.RELEASE-exec.jar
CMD java -Djava.security.egd=file:/dev/./urandom -jar spring-cloud-config-server-*.jar

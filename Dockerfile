FROM dockerfile/java:openjdk-7-jdk
EXPOSE 8888
WORKDIR /opt/spring-cloud-config-server/
RUN curl -O http://repo.spring.io/milestone/org/springframework/cloud/spring-cloud-config-server/1.0.0.RC3/spring-cloud-config-server-1.0.0.RC3-exec.jar
CMD java -Djava.security.egd=file:/dev/./urandom -jar spring-cloud-config-server-*.jar

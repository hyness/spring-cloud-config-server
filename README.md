# Spring Cloud Config Server

A docker image of [Spring Cloud Config Server](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html).

## Usage

Spring Cloud Config Server is a normal Spring Boot application, it can be configured through all the ways a Spring Boot 
application can be configured.  You may use environment variables or you can mount configuration in the usual places 
Spring Boot will look for them like the working directory (which is **/opt/spring-cloud-config-server**) 
or a subdirectory named **config**.  See the [Spring Boot documentation](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
for further information on how to use and configure Spring Boot.

### Running a Config Server Using Environment Variables
```
docker run -it -p 8888:8888 \
      -e spring_cloud_config_server_git_uri=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
### Running a Config Server Using a Mounted application.properties
```
docker run -it -p 8888:8888 \
      -v /path/to/application.properties:/opt/spring-cloud-config-server/application.properties \
      hyness/spring-cloud-config-server
```
### Running a Config Server Using a Mounted config Directory
```
docker run -it -p 8888:8888 \
      -v /path/to/config/dir:/opt/spring-cloud-config-server/config \
      hyness/spring-cloud-config-server
```
### Verify Samples Above
```
$ curl http://localhost:8888/foo/development
```

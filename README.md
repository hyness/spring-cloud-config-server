# Spring Cloud Config Server
A docker image of [Spring Cloud Config Server](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html).

[![Docker Stars](https://img.shields.io/docker/stars/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server/)
[![Docker Pulls](https://img.shields.io/docker/pulls/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server)

## Usage
```
docker run -it --name=spring-cloud-config-server \
      -p 8888:8888 \
      -v </path/to/config>:/config \
      hyness/spring-cloud-config-server
```

#### Parameters
* `-p 8888` Server port
* `-v /config` Mounted configuration

###  Configuring Spring Cloud Config Server
Spring Cloud Config Server is a normal Spring Boot application, it can be configured through all the ways a Spring Boot 
application can be configured.  You may use environment variables or you can mount configuration in the provided volume.  The configuration file must be named **application** and may be a properties or yaml file. See the [Spring Boot documentation](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config) for further information on how to use and configure Spring Boot.


#### Configuration examples
```
# Using a mounted config Directory
docker run -it -p 8888:8888 \
      -v /path/to/config/dir:/config \
      hyness/spring-cloud-config-server

# Using a mounted application.yml
docker run -it -p 8888:8888 \
      -v /path/to/application.properties:/config/application.yml \
      hyness/spring-cloud-config-server

# Configure through environment variables without a configuration file
docker run -it -p 8888:8888 \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server

```
#### Verify Samples Above
```
$ curl http://localhost:8888/foo/development
```

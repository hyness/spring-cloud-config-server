# Spring Cloud Config Server
A docker image of [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.2.1.RELEASE/reference/html/).

This a forked repository to upgrade to Java 11 and the corresponding maven Docker image.a

[![License](https://img.shields.io/github/license/hyness/spring-cloud-config-server)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Docker Automated build](https://img.shields.io/docker/automated/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server/builds/)
[![Docker Stars](https://img.shields.io/docker/stars/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server/)
[![Docker Pulls](https://img.shields.io/docker/pulls/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server)
[![Image Size](https://images.microbadger.com/badges/image/hyness/spring-cloud-config-server.svg)](https://microbadger.com/images/hyness/spring-cloud-config-server)

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

#### Environment Variables
* `JAVA_OPTS` Specify VM Options or System Properties

###  Configuring Spring Cloud Config Server
Spring Cloud Config Server is a normal Spring Boot application, it can be configured through all the ways a 
Spring Boot application can be configured.  You may use environment variables or you can mount configuration in 
the provided volume.  The configuration file must be named **application** and may be a properties or yaml file. 
See the [Spring Boot documentation](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config) 
for further information on how to use and configure Spring Boot.

#### Configuration examples

##### Using a mounted config directory
```
docker run -it -p 8888:8888 \
      -v /path/to/config/dir:/config \
      hyness/spring-cloud-config-server
```

##### Using a mounted application.yml
```
docker run -it -p 8888:8888 \
      -v /path/to/application.yml:/config/application.yml \
      hyness/spring-cloud-config-server
```
##### Configure through environment variables without a configuration file
```
docker run -it -p 8888:8888 \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
##### Configure through system properties without a configuration file
```
docker run -it -p 8888:8888 \
      -e JAVA_OPTS=-Dspring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
##### Configure through command line arguments without a configuration file
```
docker run -it -p 8888:8888 \
      hyness/spring-cloud-config-server \
      --spring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo
```
##### Verify Samples Above
```
$ curl http://localhost:8888/foo/development
```

### Required Backend Configuration
Spring Cloud Config Server **requires** that you configure a backend to serve your configuration files.  There are currently 3 backends to choose from...

#### Git
##### Remote git repo example
```
docker run -it -p 8888:8888 \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```

##### Local git repo example
```
docker run -it -p 8888:8888 \
      -v /path/to/config/files/dir:/config \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=file:/config/my-local-git-repo \
      hyness/spring-cloud-config-server
```

#### Filesystem
```
docker run -it -p 8888:8888 \
      -v /path/to/config/files/dir:/config \
      -e SPRING_PROFILES_ACTIVE=native \
      hyness/spring-cloud-config-server
```

#### Vault
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=vault \
      hyness/spring-cloud-config-server
```

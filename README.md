# Spring Cloud Config Server
A docker image of [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.2.3.RELEASE/reference/html/).

![Docker Image Version (latest semver)](https://img.shields.io/docker/v/hyness/spring-cloud-config-server?style=flat-square)
[![Docker Stars](https://img.shields.io/docker/stars/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server/)
[![Docker Pulls](https://img.shields.io/docker/pulls/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server)
[![Docker Automated build](https://img.shields.io/docker/automated/hyness/spring-cloud-config-server.svg?style=flat-square)](https://hub.docker.com/r/hyness/spring-cloud-config-server/builds/)
![Docker Build Status](https://img.shields.io/docker/build/hyness/spring-cloud-config-server?style=flat-square)
[![Image Size](https://images.microbadger.com/badges/image/hyness/spring-cloud-config-server.svg)](https://microbadger.com/images/hyness/spring-cloud-config-server)
[![License](https://img.shields.io/github/license/hyness/spring-cloud-config-server)](https://www.apache.org/licenses/LICENSE-2.0.html)

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
Spring Cloud Config Server **requires** that you configure a backend to serve your configuration files.  There are currently 6 backends to choose from...

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
      -e SPRING_CLOUD_CONFIG_SERVER_VAULT_HOST=localhost \
      -e SPRING_CLOUD_CONFIG_SERVER_VAULT_TOKEN=00000000-0000-0000-0000-000000000000 \
      hyness/spring-cloud-config-server
```
#### AWS S3
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=awss3 \
      -e SPRING_CLOUD_CONFIG_SERVER_AWSS3_REGION=us-east-1 \
      -e SPRING_CLOUD_CONFIG_SERVER_AWSS3_BUCKET=bucket \
      hyness/spring-cloud-config-server
```
#### Redis
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=redis \
      -e SPRING_REDIS_HOST=localhost
      -e SPRING_REDIS_PORT=6379
      hyness/spring-cloud-config-server
```
#### JDBC
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=jdbc \
      -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/database_name \
      -e SPRING_DATASOURCE_USERNAME=myuser \
      -e SPRING_DATASOURCE_PASSWORD=mypassword \
      hyness/spring-cloud-config-server
```
##### Supported Databases
* Firebird
* MariaDB
* MS-SQL
* Postgres

[See the docker-compose examples](https://github.com/hyness/spring-cloud-config-server/tree/master/examples) for more details

### Security
Spring Security can be enabled through the `security` profile.
##### Basic Security
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=security \
      -e SPRING_SECURITY_USER_NAME=myuser -e SPRING_SECURITY_USER_PASSWORD=mypassword \
      hyness/spring-cloud-config-server
```
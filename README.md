# Spring Cloud Config Server
A docker image of [Spring Cloud Config Server](https://docs.spring.io/spring-cloud-config/reference/).

![CI Build](https://github.com/hyness/spring-cloud-config-server/workflows/CI/badge.svg?branch=main)
![Docker Image Version (latest semver)](https://img.shields.io/docker/v/hyness/spring-cloud-config-server?sort=semver)
[![Docker Stars](https://img.shields.io/docker/stars/hyness/spring-cloud-config-server.svg?style=flat)](https://hub.docker.com/r/hyness/spring-cloud-config-server/)
[![Docker Pulls](https://img.shields.io/docker/pulls/hyness/spring-cloud-config-server.svg?style=flat)](https://hub.docker.com/r/hyness/spring-cloud-config-server)
[![Image Size](https://img.shields.io/docker/image-size/hyness/spring-cloud-config-server.svg?style=flat)](https://hub.docker.com/r/hyness/spring-cloud-config-server)
[![License](https://img.shields.io/github/license/hyness/spring-cloud-config-server)](https://www.apache.org/licenses/LICENSE-2.0.html)

### Supported tags
* `4.1.1-jre17`, `4.1-jre17`, `jre17`, `4.1.1`, `4.1`, `latest`
* `4.1.1-jdk17`, `4.1-jdk17`, `jdk17`
* `4.1.1-jre21`, `4.1-jre21`, `jre21`
* `4.1.1-jdk21`, `4.1-jdk21`, `jdk21`

#### Build updates for 4.x
* *Based on Spring Cloud Config Server 4.x & Spring Boot 3.x which is based on Java 17 baseline*
* *Some configuration has changed since 3.x, [see the updated examples](https://github.com/hyness/spring-cloud-config-server/tree/main/examples)*
* *Default image is based on Java 17.*
* *JRE builds are now available and the default for latest, 4.1, 4.1.1*

### Usage
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

#### Using a mounted config directory
```
docker run -it -p 8888:8888 \
      -v /path/to/config/dir:/config \
      hyness/spring-cloud-config-server
```

#### Using a mounted application.yml
```
docker run -it -p 8888:8888 \
      -v /path/to/application.yml:/config/application.yml \
      hyness/spring-cloud-config-server
```
#### Configure through environment variables without a configuration file
```
docker run -it -p 8888:8888 \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
#### Configure through system properties without a configuration file
```
docker run -it -p 8888:8888 \
      -e JAVA_OPTS=-Dspring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
#### Configure through command line arguments without a configuration file
```
docker run -it -p 8888:8888 \
      hyness/spring-cloud-config-server \
      --spring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo
```
#### Verify Samples Above
```
$ curl http://localhost:8888/foo/development
```

### Required Backend Configuration
Spring Cloud Config Server **requires** that you configure a backend to serve your configuration files

#### Git
#### Remote https git repo example
```
docker run -it -p 8888:8888 \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
#### Remote ssh git repo example
```
docker run -it -p 8888:8888 \
      -v /path/to/home/dir/with/ssh/keys:/home/cnb/.ssh  \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=git@github.com:spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```
#### Local git repo example
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
#### AWS Parameter Store
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=awsparamstore \
      -e SPRING_CLOUD_CONFIG_SERVER_AWSPARAMSTORE_REGION=us-east-1 \
      hyness/spring-cloud-config-server
```
#### AWS S3
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=awss3 \
      -e SPRING_CLOUD_CONFIG_SERVER_AWSS3_REGION=us-east-1 \
      -e SPRING_CLOUD_CONFIG_SERVER_AWSS3_BUCKET=sample-bucket \
      hyness/spring-cloud-config-server
```
#### AWS Secrets Manager
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=awssecretsmanager \
      -e SPRING_CLOUD_CONFIG_SERVER_AWSSECRETSMANAGER_REGION=us-east-1 \
      hyness/spring-cloud-config-server
```
#### Redis
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=redis \
      -e SPRING_DATA_REDIS_HOST=localhost
      -e SPRING_DATA_REDIS_PORT=6379
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
#### Supported Databases
* Firebird
* MariaDB
* MS-SQL
* Postgres

[See the docker-compose examples](https://github.com/hyness/spring-cloud-config-server/tree/main/examples) for more details

### Additional Features

#### Push notifications with Spring Cloud Bus

Spring Cloud Bus links the nodes of a distributed system with a lightweight message broker.  It allows clusters of 
applications configured with **RefreshScope** to automatically reload configuration without restarting.

[See the Spring Cloud Conifg](https://docs.spring.io/spring-cloud-config/docs/2.2.6.RELEASE/reference/html/#_push_notifications_and_spring_cloud_bus) and 
[Spring Cloud Bus](https://cloud.spring.io/spring-cloud-bus/reference/html) documentation for more details
    
#### Supported Message Brokers
#### Kafka
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=cloud-bus-kafka \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      -e SPRING_KAFKA_BOOTSTRAP-SERVERS=localhost:9092 \
      hyness/spring-cloud-config-server
```

#### RabbitMQ
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=cloud-bus-rabbit \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      -e SPRING_RABBITMQ_HOST=localhost \
      hyness/spring-cloud-config-server
```

### Actuator
The [Spring Boot actuator](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready) is 
enabled by default.  [See the Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator-properties) 
for configuration options.  The actuator can be disabled entirely by including the `no-actuator` profile

#### Prometheus Metrics
To enable support for an additional prometheus metrics endpoint, enable the `prometheus` endpoint, as well as [ensuring the 
endpoint is exposed](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.endpoints.exposing).
````
docker run -it -p 8888:8888 \
      -v /path/to/config/files/dir:/config \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=file:/config/my-local-git-repo \
      -e MANAGEMENT_ENDPOINT_PROMETHEUS_ENABLED=true \
      -e MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,prometheus \
      hyness/spring-cloud-config-server
````

### Spring Security
Spring Security can be enabled through the `security` profile for use with any backend of your choosing.

#### Basic / Form Security (with remote git backend)
```
docker run -it -p 8888:8888 \
      -e SPRING_PROFILES_ACTIVE=security \
      -e SPRING_SECURITY_USER_NAME=myuser \
      -e SPRING_SECURITY_USER_PASSWORD=mypassword \
      -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/spring-cloud-samples/config-repo \
      hyness/spring-cloud-config-server
```

### Docker Healthcheck
Starting with 4.1.x, this docker image includes an internal health checker for use with docker or docker compose
using [Tiny Health Checker](https://github.com/dmikusa/tiny-health-checker).

#### Example configuration
```yaml
    healthcheck:
      test: ["CMD", "health-check"]
      start_interval: 30s
      interval: 5s
      retries: 10
```

See [the full example](./examples/healthcheck/docker-compose.yml) for more details

### Thank You

#### Project Contributors
Thank you to all the contributors who have helped make this project better through code contributions and bug reports.

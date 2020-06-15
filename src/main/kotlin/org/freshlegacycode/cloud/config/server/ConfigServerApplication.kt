package org.freshlegacycode.cloud.config.server

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@EnableConfigServer
@SpringBootApplication(exclude = [
    DataSourceAutoConfiguration::class,
    RedisAutoConfiguration::class,
    ManagementContextAutoConfiguration::class,
    ManagementWebSecurityAutoConfiguration::class,
    SecurityAutoConfiguration::class
])
class ConfigServerApplication

@Profile("!no-actuator")
@Configuration
@Import(ManagementContextAutoConfiguration::class)
internal class ActuatorBackendConfiguration

@Profile("jdbc")
@Configuration
@Import(DataSourceAutoConfiguration::class)
internal class JdbcBackendConfiguration

@Profile("redis")
@Configuration
@Import(RedisAutoConfiguration::class)
internal class RedisBackendConfiguration

@Profile("security")
@Configuration
@Import(ManagementWebSecurityAutoConfiguration::class, SecurityAutoConfiguration::class)
internal class SecurityConfiguration

fun main(args: Array<String>) {
    runApplication<ConfigServerApplication>(*args)
}
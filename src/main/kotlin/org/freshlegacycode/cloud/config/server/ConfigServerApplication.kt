package org.freshlegacycode.cloud.config.server

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.bus.*
import org.springframework.cloud.bus.jackson.BusJacksonAutoConfiguration
import org.springframework.cloud.config.monitor.EnvironmentMonitorAutoConfiguration
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.cloud.stream.binder.kafka.config.ExtendedBindingHandlerMappingsProviderConfiguration as KafkaBinderAutoConfiguration
import org.springframework.cloud.stream.binder.rabbit.config.ExtendedBindingHandlerMappingsProviderConfiguration as RabbitBinderAutoConfiguration

@EnableConfigServer
@SpringBootApplication(exclude = [
    DataSourceAutoConfiguration::class,
    RedisAutoConfiguration::class,
    ManagementContextAutoConfiguration::class,
    ManagementWebSecurityAutoConfiguration::class,
    SecurityAutoConfiguration::class,
    RabbitBinderAutoConfiguration::class,
    KafkaBinderAutoConfiguration::class,
    BusAutoConfiguration::class,
    PathServiceMatcherAutoConfiguration::class,
    BusAutoConfiguration::class,
    BusRefreshAutoConfiguration::class,
    BusShutdownAutoConfiguration::class,
    BusStreamAutoConfiguration::class,
    BusJacksonAutoConfiguration::class,
    EnvironmentMonitorAutoConfiguration::class,
    KafkaAutoConfiguration::class,
    RabbitAutoConfiguration::class,
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

@Profile("cloud-bus-kafka")
@Configuration
@Import(KafkaAutoConfiguration::class, KafkaBinderAutoConfiguration::class)
internal class CloudBusKafkaConfiguration : CloudBusConfiguration()

@Profile("cloud-bus-rabbit")
@Configuration
@Import(RabbitAutoConfiguration::class, RabbitBinderAutoConfiguration::class)
internal class CloudBusRabbitConfiguration : CloudBusConfiguration()

@Import(BusAutoConfiguration::class,
        BusRefreshAutoConfiguration::class,
        BusShutdownAutoConfiguration::class,
        BusStreamAutoConfiguration::class,
        BusJacksonAutoConfiguration::class,
        PathServiceMatcherAutoConfiguration::class,
        EnvironmentMonitorAutoConfiguration::class)
internal abstract class CloudBusConfiguration

@Profile("redis")
@Configuration
@Import(RedisAutoConfiguration::class)
internal class RedisBackendConfiguration

@Profile("security")
@Configuration
@Import(SecurityAutoConfiguration::class, ManagementWebSecurityAutoConfiguration::class)
internal class SecurityConfiguration

@Profile("security")
@Configuration
@EnableConfigurationProperties(ConfigServerSecurityProperties::class)
class ConfigServerSecurityConfiguration(val properties: ConfigServerSecurityProperties) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(EndpointRequest.to(HealthEndpoint::class.java), permitAll)
                properties.allowedPaths.forEach {
                    authorize(it, permitAll)
                }
                authorize(anyRequest, authenticated)
            }
            formLogin {}
            httpBasic {}
            csrf {
                disable()
            }
        }
        return http.build()
    }
}

@ConfigurationProperties("spring.cloud.config.security")
data class ConfigServerSecurityProperties(val allowedPaths: List<String> = listOf())

fun main(args: Array<String>) {
    runApplication<ConfigServerApplication>(*args)
}

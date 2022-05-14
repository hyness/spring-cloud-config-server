package org.freshlegacycode.cloud.config.server

import org.assertj.core.api.Assertions.assertThat
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.cloudConfigWait
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logConsumer
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.freshlegacycode.cloud.config.server.ContainerType.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration
import kotlin.text.Charsets.UTF_8

@Testcontainers
class PrometheusActuatorTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with remote git backend and prometheus actuator, is valid`(type: ContainerType) {
        logger.info("Verifying ${type.label}")
        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(Duration.ofSeconds(10))
            .build()

        webClient.get()
            .uri("/actuator/prometheus")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentTypeCompatibleWith(TEXT_PLAIN)
            .expectBody()
            .consumeWith {
                val response = it.responseBodyContent?.toString(UTF_8)
                assertThat(response)
                    .contains("# HELP application_started_time_seconds Time taken (ms) to start the application")
            }
    }

    companion object {
        @Container
        val cloudConfigContainer: DockerComposeContainer<*> = DockerComposeContainer(File("examples/prometheus/docker-compose.yml"))
            .withExposedService(ENV_VARS.container, 8888, cloudConfigWait)
            .withExposedService(CONFIG_DIR.container, 8888, cloudConfigWait)
            .withExposedService(SYS_PROPS.container, 8888, cloudConfigWait)
            .withExposedService(COMMAND_LINE.container, 8888, cloudConfigWait)
            .withLogConsumer(ENV_VARS.container, logConsumer)
            .withLogConsumer(CONFIG_DIR.container, logConsumer)
            .withLogConsumer(SYS_PROPS.container, logConsumer)
            .withLogConsumer(COMMAND_LINE.container, logConsumer)
    }
}
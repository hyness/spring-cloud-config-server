package org.freshlegacycode.cloud.config.server

import org.assertj.core.api.Assertions.assertThat
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration
import kotlin.text.Charsets.UTF_8

@Testcontainers
@Tag("prometheus")
class PrometheusActuatorTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with remote git backend and prometheus actuator, is valid`(type: ContainerConfigurationType) {
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
        val cloudConfigContainer = "examples/prometheus/docker-compose.yml".toComposeContainer()
    }
}
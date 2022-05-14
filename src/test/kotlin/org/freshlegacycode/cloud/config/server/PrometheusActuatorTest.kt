package org.freshlegacycode.cloud.config.server

import org.assertj.core.api.Assertions.assertThat
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.text.Charsets.UTF_8

@Testcontainers
@Tags(Tag("integration"), Tag("prometheus"))
class PrometheusActuatorTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with remote git backend and prometheus actuator, is valid`(type: ContainerConfigurationType) {
        logger.info { "Verifying ${type.label}" }
        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(containerTimeout)
            .build()

        webClient.get()
            .uri("/actuator/prometheus")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentTypeCompatibleWith(TEXT_PLAIN)
            .expectBody()
            .consumeWith {
                assertThat(it.responseBodyContent?.toString(UTF_8))
                    .contains("# TYPE system_cpu_usage gauge")
            }
    }

    companion object {
        @Container
        val cloudConfigContainer = "examples/prometheus/docker-compose.yml".toComposeContainer()
    }
}
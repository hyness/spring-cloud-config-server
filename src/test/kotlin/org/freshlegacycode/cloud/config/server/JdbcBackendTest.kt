package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Tags(Tag("integration"), Tag("jdbc"))
abstract class JdbcBackendTest {
    abstract fun getContainer(): DockerComposeContainer<*>

    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with jdbc backend, is valid`(type: ContainerConfigurationType) {
        logger.info{ "Verifying ${type.label}" }
        val webClient = WebTestClient.bindToServer()
            .baseUrl(getContainer().getUrl(type))
            .responseTimeout(containerTimeout)
            .build()

        webClient.get()
            .uri("/jdbc-app/dev/latest")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo("jdbc-app")
            .jsonPath("$.propertySources[0].source.['sample key']").isEqualTo("a value")
    }
}
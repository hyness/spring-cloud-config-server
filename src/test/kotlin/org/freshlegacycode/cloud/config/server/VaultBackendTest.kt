package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@Testcontainers
@Tag("vault")
class VaultBackendTest {

    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with vault backend, is valid`(type: ContainerConfigurationType) {
        logger.info { "Verifying ${type.label}" }
        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(Duration.ofSeconds(10))
            .build()

        webClient.get()
            .uri("/myapp/default")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo("myapp")
            .jsonPath("$.propertySources[0].source.foo").isEqualTo("myappsbar")
    }

    companion object {
        @Container
        val cloudConfigContainer = "examples/vault/docker-compose.yml".toComposeContainer().apply {
            withExposedService("vault", 8200, Wait.forListeningPort())
        }

        @JvmStatic
        @BeforeAll
        internal fun populateData(): Unit {
            cloudConfigContainer.getContainerByServiceName("vault")
                .ifPresent {
                    it.execInContainer("sh", "/populate-vault.sh")
                }
        }
    }
}
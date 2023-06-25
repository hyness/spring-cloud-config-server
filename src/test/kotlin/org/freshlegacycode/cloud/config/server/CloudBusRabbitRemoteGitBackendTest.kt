package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@Testcontainers
@Tags(Tag("integration"), Tag("cloud-bus"), Tag("rabbitmq"), Tag("cloud-bus-rabbitmq"))
class CloudBusRabbitRemoteGitBackendTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with cloud bus and remote git backend, is valid`(type: ContainerConfigurationType) {
        logger.info("Verifying ${type.label}")
        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(Duration.ofSeconds(10))
            .build()

        webClient.get()
            .uri("/foo/development")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .consumeWith {
                println(it)
            }
            .jsonPath("$.name").isEqualTo("foo")
            .jsonPath("$.propertySources[0].name").isEqualTo("https://github.com/spring-cloud-samples/config-repo/foo-development.properties")
    }

    companion object {
        @Container
        val cloudConfigContainer = "examples/cloud-bus/rabbit/docker-compose.yml".toComposeContainer()
    }
}
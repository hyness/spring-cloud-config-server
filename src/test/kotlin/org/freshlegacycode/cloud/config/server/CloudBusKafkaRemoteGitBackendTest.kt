package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Tags(Tag("integration"), Tag("cloud-bus"),Tag("kafka"), Tag("cloud-bus-kafka"))
class CloudBusKafkaRemoteGitBackendTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with cloud bus and remote git backend, is valid`(type: ContainerConfigurationType) {
        logger.info { "Verifying ${type.label}" }
        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(containerTimeout)
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
        val cloudConfigContainer = "examples/cloud-bus/kafka/docker-compose.yml".toComposeContainer().apply {
            withExposedService("kafka", 9092, Wait.forListeningPort())
        }
    }
}
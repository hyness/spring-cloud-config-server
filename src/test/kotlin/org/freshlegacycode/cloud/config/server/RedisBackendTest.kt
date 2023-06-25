package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.freshlegacycode.cloud.config.server.ContainerConfigurationType.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.*
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@TestInstance(PER_CLASS)
@Testcontainers
class RedisBackendTest {
    @BeforeAll
    internal fun populateData() {
        cloudConfigContainer.getContainerByServiceName("redis")
            .ifPresent {
                it.execInContainer("sh", "/data/populate-redis.sh")
            }
    }

    @EnumSource
    @Tag("redis")
    @ParameterizedTest
    internal fun `given a config server, when configured with redis backend, is valid`(type: ContainerConfigurationType) {
        logger.info("Verifying ${type.label}")
        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(Duration.ofSeconds(10))
            .build()
        webClient.get()
            .uri("/redis-app/development")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .consumeWith { logger.info{ it } }
            .jsonPath("$.name").isEqualTo("redis-app")
            .jsonPath("$.propertySources[0].source.['server.port']").isEqualTo("8100")
    }

    companion object {
        @Container
        val cloudConfigContainer = "examples/redis/docker-compose.yml".toComposeContainer().apply {
            withExposedService("redis", 6379, Wait.forListeningPort())
        }
    }
}
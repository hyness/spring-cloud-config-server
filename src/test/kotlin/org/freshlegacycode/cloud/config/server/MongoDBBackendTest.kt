package org.freshlegacycode.cloud.config.server

import org.assertj.core.api.Assertions.assertThat
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logConsumer
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.jvm.optionals.getOrNull

@Testcontainers
@Tags(Tag("integration"), Tag("mongodb"))
class MongoDBBackendTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with mongodb backend, is valid`(type: ContainerConfigurationType) {
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
            .consumeWith { logger.info{ it } }
            .jsonPath("$.name").isEqualTo("foo")
            .jsonPath("$.profiles[0]").isEqualTo("development")
            .jsonPath("$.propertySources[0].name").isEqualTo("foo-development")
            .jsonPath("$.propertySources[0].source.property1").isEqualTo("value1")
            .jsonPath("$.propertySources[0].source.property2").isEqualTo("value2")
    }

    companion object {
        @Container
        val cloudConfigContainer: ComposeContainer = "examples/mongodb/compose.yml".toComposeContainer()
            .withExposedService("mongodb", 27017)
            .withLogConsumer("mongodb", logConsumer)

        @JvmStatic
        @BeforeAll
        internal fun populateData() {
            logger.info { "Populating test data" }
            val execResult = (cloudConfigContainer.getContainerByServiceName("mongodb")
                .getOrNull()
                ?.execInContainer("sh", "/docker-entrypoint-initdb.d/populate-mongo.sh")
                ?: throw RuntimeException("Could not populate mongodb test data"))

            assertThat(execResult.exitCode).isZero()
        }
    }
}
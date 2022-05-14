package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.BeforeAll
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
@Tags(Tag("integration"), Tag("aws"), Tag("aws-s3"))
class AwsS3BackendTest {

    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with aws s3 backend, is valid`(type: ContainerConfigurationType) {
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
            .jsonPath("$.name").isEqualTo("foo")
            .jsonPath("$.propertySources[0].name").isEqualTo("s3:foo-development")
    }

    companion object {
        @Container
        val cloudConfigContainer = "examples/aws/s3/docker-compose.yml".toComposeContainer().apply {
            withExposedService("localstack", 4566, Wait.forListeningPort())
        }

        @JvmStatic
        @BeforeAll
        internal fun populateData() {
            cloudConfigContainer.getContainerByServiceName("localstack")
                .ifPresent {
                    it.execInContainer("sh", "/data/populate-s3.sh")
                }
        }
    }
}
package org.freshlegacycode.cloud.config.server

import org.assertj.core.api.Assertions.assertThat
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
import kotlin.jvm.optionals.getOrNull

@Testcontainers
@Tags(Tag("integration"), Tag("aws"), Tag("aws-secrets-manager"))
class AwsSecretsManagerBackendTest {

    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with aws secrets manager backend, is valid`(type: ContainerConfigurationType) {
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
            .jsonPath("$.propertySources[0].name").isEqualTo("aws:secrets:/secret/foo-development/")
    }

    companion object {
        @Container
        val cloudConfigContainer = "examples/aws/secretsmanager/compose.yml".toComposeContainer().apply {
            withExposedService("localstack", 4566, "/_localstack/health".run(Wait::forHttp))
        }

        @JvmStatic
        @BeforeAll
        internal fun populateData() {
            logger.info { "Populating test data" }
            val execResult = (cloudConfigContainer.getContainerByServiceName("localstack")
                .getOrNull()
                ?.execInContainer("sh", "/data/populate-secretsmanager.sh")
                ?: throw RuntimeException("Could not populate secretsmanager test data"))

            assertThat(execResult.exitCode).isZero()
        }
    }
}
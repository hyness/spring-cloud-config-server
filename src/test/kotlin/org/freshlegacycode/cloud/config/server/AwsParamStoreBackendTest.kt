package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.cloudConfigWait
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logConsumer
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.freshlegacycode.cloud.config.server.ContainerType.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.*
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration

@Testcontainers
@TestInstance(PER_CLASS)
class AwsParamStoreBackendTest {
    @BeforeAll
    internal fun populateData() {
        cloudConfigContainer.getContainerByServiceName("localstack")
            .ifPresent {
                it.execInContainer("sh", "/data/populate-paramstore.sh")
            }
    }

    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with aws parameter store backend, is valid`(type: ContainerType) {
        logger.info { "Verifying ${type.label}" }
        val url = cloudConfigContainer.getContainerByServiceName(type.container)
            .orElseThrow()
            .let { "http://localhost:${it.firstMappedPort}" }
        val webClient = WebTestClient.bindToServer()
            .baseUrl(url)
            .responseTimeout(Duration.ofSeconds(10))
            .build()

        webClient.get()
            .uri("/foo/development")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.name").isEqualTo("foo")
            .jsonPath("$.propertySources[0].name").isEqualTo("aws:ssm:parameter:/config/foo-development/")
    }

    companion object {
        @Container
        val cloudConfigContainer: DockerComposeContainer<*> = DockerComposeContainer(File("examples/aws/paramstore/docker-compose.yml"))
            .withExposedService("localstack", 4566, Wait.forListeningPort())
            .withExposedService(ENV_VARS.container, 8888, cloudConfigWait)
            .withExposedService(CONFIG_DIR.container, 8888, cloudConfigWait)
            .withExposedService(SYS_PROPS.container, 8888, cloudConfigWait)
            .withExposedService(COMMAND_LINE.container, 8888, cloudConfigWait)
            .withLogConsumer(ENV_VARS.container, logConsumer)
            .withLogConsumer(CONFIG_DIR.container, logConsumer)
            .withLogConsumer(SYS_PROPS.container, logConsumer)
            .withLogConsumer(COMMAND_LINE.container, logConsumer)
    }
}
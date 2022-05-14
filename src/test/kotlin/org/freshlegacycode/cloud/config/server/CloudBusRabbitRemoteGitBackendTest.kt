package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.cloudConfigWait
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logConsumer
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.freshlegacycode.cloud.config.server.ContainerType.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration

@Testcontainers
class CloudBusRabbitRemoteGitBackendTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with cloud bus and remote git backend, is valid`(type: ContainerType) {
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
        val cloudConfigContainer: DockerComposeContainer<*> = DockerComposeContainer(File("examples/cloud-bus/rabbit/docker-compose.yml"))
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
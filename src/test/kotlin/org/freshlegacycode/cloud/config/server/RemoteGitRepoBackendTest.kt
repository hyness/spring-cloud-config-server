package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Tags(Tag("integration"), Tag("git"))
class RemoteGitRepoBackendTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with remote git backend, is valid`(type: ContainerConfigurationType) {
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
            .jsonPath("$.propertySources[0].name").isEqualTo("https://github.com/spring-cloud-samples/config-repo/foo-development.properties")
    }

    companion object {
        @Container
        val cloudConfigContainer: ComposeContainer = "examples/git/compose.yml".toComposeContainer()
    }
}
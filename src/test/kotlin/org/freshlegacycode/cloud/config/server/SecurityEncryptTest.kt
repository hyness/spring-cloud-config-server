package org.freshlegacycode.cloud.config.server

import org.assertj.core.api.Assertions.assertThat
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.containerTimeout
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logger
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.testcontainers.shaded.org.bouncycastle.util.encoders.Base64

@Testcontainers
@Tags(Tag("integration"), Tag("security"))
class SecurityEncryptTest {
    @EnumSource
    @ParameterizedTest
    internal fun `given a config server, when configured with remote git backend, is valid`(type: ContainerConfigurationType) {
        logger.info { "Verifying ${type.label}" }
        val encodedCredentials = "Basic ${Base64.toBase64String("user:password".toByteArray())}"
        val value = randomAlphanumeric(20)

        val webClient = WebTestClient.bindToServer()
            .baseUrl(cloudConfigContainer.getUrl(type))
            .responseTimeout(containerTimeout)
            .build()

        val encrypted = webClient.post()
            .uri("/encrypt")
            .contentType(TEXT_PLAIN)
            .header(AUTHORIZATION, encodedCredentials)
            .body(fromValue(value))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentTypeCompatibleWith(TEXT_PLAIN)
            .returnResult(String::class.java)
            .responseBody
            .blockLast()

        assertThat(encrypted).isNotBlank().isNotEqualTo(value)

        val decrypted = webClient.post()
            .uri("/decrypt")
            .contentType(TEXT_PLAIN)
            .header(AUTHORIZATION, encodedCredentials)
            .body(fromValue(encrypted!!))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentTypeCompatibleWith(TEXT_PLAIN)
            .returnResult(String::class.java)
            .responseBody
            .blockLast()

        assertThat(decrypted).isEqualTo(value)
    }

    companion object {
        @Container
        val cloudConfigContainer: DockerComposeContainer<*> = "examples/security/compose.yml".toComposeContainer()
    }
}
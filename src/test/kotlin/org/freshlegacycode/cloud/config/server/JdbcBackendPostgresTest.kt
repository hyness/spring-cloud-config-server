package org.freshlegacycode.cloud.config.server

import org.junit.jupiter.api.Tag
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Tag("postgres")
class JdbcBackendPostgresTest : JdbcBackendTest() {
    override fun getContainer() = cloudConfigContainer

    companion object {
        @Container
        val cloudConfigContainer = "examples/jdbc/postgres/compose.yml".toComposeContainer().apply {
            withExposedService("postgres", 5432, Wait.forListeningPort())
        }
    }
}
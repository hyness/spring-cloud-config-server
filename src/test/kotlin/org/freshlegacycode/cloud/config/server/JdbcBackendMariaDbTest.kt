package org.freshlegacycode.cloud.config.server

import org.junit.jupiter.api.Tag
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Tag("mariadb")
class JdbcBackendMariaDbTest : JdbcBackendTest() {
    override fun getContainer() = cloudConfigContainer

    companion object {
        @Container
        val cloudConfigContainer = "examples/jdbc/mariadb/docker-compose.yml".toComposeContainer().apply {
            withExposedService("mariadb", 3306, Wait.forListeningPort())
        }
    }
}
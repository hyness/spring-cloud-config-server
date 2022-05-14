package org.freshlegacycode.cloud.config.server

import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.cloudConfigWait
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logConsumer
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
class JdbcBackendPostgresTest : JdbcBackendTest() {
    override fun getContainer() = cloudConfigContainer

    companion object {
        @Container
        val cloudConfigContainer: DockerComposeContainer<*> = DockerComposeContainer(File("examples/jdbc/postgres/docker-compose.yml"))
            .withExposedService("postgres", 5432, Wait.forListeningPort())
            .withExposedService(ContainerType.ENV_VARS.container, 8888, cloudConfigWait)
            .withExposedService(ContainerType.CONFIG_DIR.container, 8888, cloudConfigWait)
            .withExposedService(ContainerType.SYS_PROPS.container, 8888, cloudConfigWait)
            .withExposedService(ContainerType.COMMAND_LINE.container, 8888, cloudConfigWait)
            .withLogConsumer(ContainerType.ENV_VARS.container, logConsumer)
            .withLogConsumer(ContainerType.CONFIG_DIR.container, logConsumer)
            .withLogConsumer(ContainerType.SYS_PROPS.container, logConsumer)
            .withLogConsumer(ContainerType.COMMAND_LINE.container, logConsumer)
    }
}
package org.freshlegacycode.cloud.config.server

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitStrategy

@SpringBootTest(properties = ["spring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo"])
class ConfigServerApplicationTests {
    @Test
    fun `context loads`() {}

    companion object {
        internal val logger = KotlinLogging.logger {}
        internal val logConsumer: Slf4jLogConsumer = Slf4jLogConsumer(logger).withSeparateOutputStreams()
        internal val cloudConfigWait: WaitStrategy = Wait.forHttp("/actuator/health")
    }
}

enum class ContainerType(val container: String, val label: String) {
    CONFIG_DIR("config-server-dir", "Cloud config with config dir"),
    COMMAND_LINE("config-server-args", "Cloud config with command line arguments"),
    ENV_VARS("config-server-env", "Cloud config with environment variables"),
    SYS_PROPS("config-server-props", "Cloud config with system properties")
}

fun DockerComposeContainer<*>.getUrl(type: ContainerType) = getContainerByServiceName(type.container)
    .orElseThrow()
    .let { "http://localhost:${it.firstMappedPort}" }

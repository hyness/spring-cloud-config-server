package org.freshlegacycode.cloud.config.server

import mu.KotlinLogging
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.cloudConfigWait
import org.freshlegacycode.cloud.config.server.ConfigServerApplicationTests.Companion.logConsumer
import org.freshlegacycode.cloud.config.server.ContainerConfigurationType.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitStrategy
import java.io.File
import java.time.Duration

@Tag("unit")
@SpringBootTest(properties = ["spring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo"])
class ConfigServerApplicationTests {
    @Test
    fun `context loads`() {}

    companion object {
        internal val logger = KotlinLogging.logger {}
        internal val logConsumer: Slf4jLogConsumer = Slf4jLogConsumer(logger).withSeparateOutputStreams()
        internal val cloudConfigWait: WaitStrategy = Wait.forHttp("/actuator/health")
        internal val containerTimeout = Duration.ofSeconds(20)
    }
}

enum class ContainerConfigurationType(val container: String, val label: String) {
    CONFIG_DIR("config-server-dir", "Cloud config with config dir"),
    COMMAND_LINE("config-server-args", "Cloud config with command line arguments"),
    ENV_VARS("config-server-env", "Cloud config with environment variables"),
    SYS_PROPS("config-server-props", "Cloud config with system properties")
}

internal fun DockerComposeContainer<*>.getUrl(type: ContainerConfigurationType) = getContainerByServiceName(type.container)
    .orElseThrow()
    .let { "http://localhost:${it.firstMappedPort}" }

internal fun String.toComposeContainer(): DockerComposeContainer<*> = DockerComposeContainer(run(::File))
    .withEnv("REGISTRY", System.getProperty("registry"))
    .withEnv("IMAGE_NAME", System.getProperty("name"))
    .withEnv("TAG", System.getProperty("tag"))
    .withExposedService(ENV_VARS.container, 8888, cloudConfigWait)
    .withExposedService(CONFIG_DIR.container, 8888, cloudConfigWait)
    .withExposedService(SYS_PROPS.container, 8888, cloudConfigWait)
    .withExposedService(COMMAND_LINE.container, 8888, cloudConfigWait)
    .withLogConsumer(ENV_VARS.container, logConsumer)
    .withLogConsumer(CONFIG_DIR.container, logConsumer)
    .withLogConsumer(SYS_PROPS.container, logConsumer)
    .withLogConsumer(COMMAND_LINE.container, logConsumer)

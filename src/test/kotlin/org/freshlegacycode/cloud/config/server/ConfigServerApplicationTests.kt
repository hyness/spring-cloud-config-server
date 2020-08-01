package org.freshlegacycode.cloud.config.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = ["spring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo"])
class ConfigServerApplicationTests {
    @Test
    fun `context loads`() {
    }
}

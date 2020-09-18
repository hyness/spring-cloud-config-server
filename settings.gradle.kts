rootProject.name = "spring-cloud-config-server"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version "1.0.10.RELEASE"
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
}
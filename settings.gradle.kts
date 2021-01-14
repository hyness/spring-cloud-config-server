rootProject.name = "spring-cloud-config-server"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagement: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagement
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
}
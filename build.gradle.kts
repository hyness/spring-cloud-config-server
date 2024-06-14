import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES as SPRING_BOOT_BOM

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
}

val jdkVersion: String by project
val jvmType: String by project
val dockerTags: String? by project
val imageRegistry: String? by project
val imageName: String? by project
val imageTag: String? by project
val testFilter: String? by project

tasks {
    version = versionCatalogs.firstNotNullOf {
        it.findVersion("spring.cloud.config").orElse(null)
    }

    compileKotlin {
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }

    test {
        systemProperties["registry"] = imageRegistry
        systemProperties["name"] = imageName
        systemProperties["tag"] = imageTag
        useJUnitPlatform {
            testFilter?.let { includeTags(it) }
        }
    }

    bootJar {
        manifest.attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }

    bootBuildImage {
        applicationDirectory = "/opt/spring-cloud-config-server"
        builder = "paketobuildpacks/builder-jammy-tiny"
        buildpacks = listOf(
            "paketo-buildpacks/ca-certificates",
            "paketo-buildpacks/bellsoft-liberica",
            "paketo-buildpacks/syft",
            "paketo-buildpacks/executable-jar",
            "paketo-buildpacks/dist-zip",
            "paketo-buildpacks/spring-boot",
            "paketo-buildpacks/environment-variables",
            "gcr.io/paketo-buildpacks/health-checker"
        )
        environment = mapOf(
            "BP_JVM_TYPE" to jvmType,
            "BP_JVM_VERSION" to jdkVersion,
            "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
            "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-Dspring.config.additional-location=optional:file:/config/",
            "BP_HEALTH_CHECKER_ENABLED" to "true",
            "BPE_THC_PATH" to "/actuator/health",
            "BPE_THC_PORT" to "8888"
        )
        imageName = "hyness/spring-cloud-config-server"
        tags = dockerTags?.split(',')
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(SPRING_BOOT_BOM))
    implementation(platform(libs.spring.cloud))
    implementation(libs.bundles.spring.cloud.config)
    implementation(libs.bundles.spring.cloud.bus)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.data.redis)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.jdbc)
    implementation(libs.spring.vault)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.aws)
    runtimeOnly(libs.bundles.jdbc.drivers)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit5)
    testImplementation(libs.kotlin.logging)
    testImplementation(libs.awaitility.kotlin)
}
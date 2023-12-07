import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES as SPRING_BOOT_BOM

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
}

val jdkVersion: String by project
val jvmType: String by project
val dockerUsername: String by project
val dockerPassword: String by project
val dockerTags: String? by project

kotlin {
    jvmToolchain(jdkVersion.toInt())
}

tasks {
    version = versionCatalogs.firstNotNullOf {
        it.findVersion("spring.cloud.config").orElse(null)
    }

    compileKotlin {
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }

    test {
        useJUnitPlatform()
    }

    bootJar {
        manifest.attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }

    bootBuildImage {
        applicationDirectory.set("/opt/spring-cloud-config-server")
        builder.set("paketobuildpacks/builder-jammy-tiny")
        docker.publishRegistry.username.set(dockerUsername)
        docker.publishRegistry.password.set(dockerPassword)
        environment.put("BP_JVM_TYPE", jvmType)
        environment.put("BP_JVM_VERSION", jdkVersion)
        environment.put("BPE_DELIM_JAVA_TOOL_OPTIONS", " ")
        environment.put("BPE_APPEND_JAVA_TOOL_OPTIONS", "-Dspring.config.additional-location=optional:file:/config/")
        imageName.set("hyness/spring-cloud-config-server")
        tags.set(dockerTags?.split(',') ?: listOf())
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
}
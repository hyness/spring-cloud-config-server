import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES as SPRING_BOOT_BOM

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
}

val jdkVersion: String? by project

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
}

kotlin {
    jvmToolchain(jdkVersion?.toInt() ?: 17)
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
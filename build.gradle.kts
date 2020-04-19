plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
}

group = "org.freshlegacycode"
version = ""

springBoot {
    mainClassName = "org.springframework.cloud.config.server.ConfigServerApplication"
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.SR3"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-config-server")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
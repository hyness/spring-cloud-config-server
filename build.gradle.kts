import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

val jvmType: String by project
val jvmVersion: String by project
val springCloudConfigVersion: String? by project

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = if (jvmVersion == "8") "1.8" else jvmVersion
        }
    }

    test {
        useJUnitPlatform()
    }

    bootJar {
        project.version = springCloudConfigVersion ?: project.dependencyManagement.importedProperties["spring-cloud-config.version"] as String

        manifest {
            attributes(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version
            )
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-config-server")
    implementation("org.springframework.cloud:spring-cloud-config-monitor")
    implementation("org.springframework.cloud:spring-cloud-bus")
    implementation("org.springframework.cloud:spring-cloud-starter-bus-kafka")
    implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.vault:spring-vault-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.amazonaws:aws-java-sdk-s3:${properties["aws.version"]}")
    implementation("com.amazonaws:aws-java-sdk-ssm:${properties["aws.version"]}")
    implementation("com.amazonaws:aws-java-sdk-sts:${properties["aws.version"]}")
    implementation("com.amazonaws:aws-java-sdk-secretsmanager:${properties["aws.version"]}")
    runtimeOnly("com.zaxxer:HikariCP")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    runtimeOnly("org.firebirdsql.jdbc:jaybird-jdk18")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${properties["springCloudVersion"]}")
    }
}

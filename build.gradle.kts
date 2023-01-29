plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

val awsVersion: String by project
val dockerGroup: String by project
val dockerRegistry: String by project
val jdkVersion: String by project
val springCloudConfigVersion: String? by project
val springCloudVersion: String by project

tasks {
    kotlin {
        compileKotlin {
            jvmToolchain(jdkVersion.toInt())
            kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    test {
        useJUnitPlatform()
    }

    bootJar {
        project.version = springCloudConfigVersion
            ?: project.dependencyManagement.importedProperties["spring-cloud-config.version"]!!

        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        }
    }

    bootBuildImage {
        imageName.set("$dockerRegistry/$dockerGroup/${project.name}:${project.version}")
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
    implementation("software.amazon.awssdk:s3:$awsVersion")
    implementation("software.amazon.awssdk:ssm:$awsVersion")
    implementation("software.amazon.awssdk:sts:$awsVersion")
    implementation("software.amazon.awssdk:secretsmanager:$awsVersion")
    runtimeOnly("com.zaxxer:HikariCP")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    runtimeOnly("org.firebirdsql.jdbc:jaybird")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

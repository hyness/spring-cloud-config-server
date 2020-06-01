plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
}

group = "org.freshlegacycode"
version = ""

springBoot {
    mainClassName = "org.freshlegacycode.cloud.config.server.ConfigServerApplication"
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.SR5"
extra["spring-cloud-config.version"] = "2.1.9.RELEASE"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-config-server")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.vault:spring-vault-core")
    implementation("com.amazonaws:aws-java-sdk-s3")
    runtimeOnly("com.zaxxer:HikariCP")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    runtimeOnly("org.firebirdsql.jdbc:jaybird-jdk18")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
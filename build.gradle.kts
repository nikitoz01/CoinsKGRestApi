import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

group = "kg.coins.backend"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-security:2.7.0")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.0")
    runtimeOnly("dev.miku:r2dbc-mysql:0.8.2.RELEASE")
    runtimeOnly("mysql:mysql-connector-java:8.0.29")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.6")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools:2.7.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.security:spring-security-test:5.7.1")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("io.projectreactor:reactor-test:3.4.18")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

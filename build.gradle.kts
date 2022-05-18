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

    implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.6.7")
    runtimeOnly("dev.miku:r2dbc-mysql:0.8.2.RELEASE")
    runtimeOnly("mysql:mysql-connector-java:8.0.29")
    

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.6")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.1-native-mt")

    developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
    testImplementation("org.springframework.security:spring-security-test:5.6.3")
    testImplementation("io.projectreactor:reactor-test:3.4.17")
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

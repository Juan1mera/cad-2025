plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
    id("application")
}

group = "ru.bsuedu.cad"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starter para JPA (incluye Hibernate y HikariCP)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.3")
    // H2 Database
    runtimeOnly("com.h2database:h2:2.2.224")
    // Logging (ya incluido en Spring Boot, pero lo mantenemos por claridad)
    implementation("ch.qos.logback:logback-classic:1.4.11")
    // Pruebas
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

application {
    mainClass.set("ru.bsuedu.cad.lab.App")
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    mainClass.set("ru.bsuedu.cad.lab.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
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
    implementation("org.springframework.boot:spring-boot-starter:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:3.2.3")
    implementation("com.h2database:h2")
    implementation("ch.qos.logback:logback-classic")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

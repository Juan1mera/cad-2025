plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
    application
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
    // Spring Boot Starter (incluye Spring Core, Context, etc.)
    implementation("org.springframework.boot:spring-boot-starter:3.2.3")
    
    // Spring Boot Starter AOP (para AspectJ)
    implementation("org.springframework.boot:spring-boot-starter-aop:3.2.3")
    
    // Jakarta Annotation para @PostConstruct
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    
    // Dependencias de prueba (opcional)
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // Guava (opcional, si lo necesitas)
    implementation("com.google.guava:guava:32.1.3-jre")
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
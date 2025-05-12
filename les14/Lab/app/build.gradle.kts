plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    // Pruebas
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Dependencias de la aplicación
    implementation(libs.guava)
    implementation(libs.spring.context)
    implementation(libs.spring.orm)
    implementation("org.springframework:spring-web:6.2.4")
    implementation(libs.spring.data.jpa)
    implementation(libs.hikari)
    implementation(libs.jakarta.persistence.api)
    implementation(libs.hibernate.hikaricp)
    implementation(libs.hibernate.core)
    implementation(libs.slf4j.api)
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // MySQL
    implementation("com.mysql:mysql-connector-j:8.0.33")

    // Servlet API para Tomcat
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")

    implementation("org.springframework:spring-webmvc:6.2.4")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")

    // Spring Security
    implementation("org.springframework.security:spring-security-web:6.2.4")
    implementation("org.springframework.security:spring-security-config:6.2.4")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.war {
    archiveFileName.set("university.war")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
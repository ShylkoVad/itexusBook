plugins {
    id("org.springframework.boot") version "3.4.1" // версия Spring Boot
    id("io.spring.dependency-management") version "1.1.7" // для управления зависимостями
    id("com.github.ben-manes.versions") version "0.51.0" // Плагин для проверки обновлений зависимостей
    id("java")
}

group = "com.itexus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.36") // добавление зависимостей lombok
    annotationProcessor("org.projectlombok:lombok:1.18.36") // для обработки аннотаций
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter") // добавление зависимости для написания и запуска тестов в Java

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.postgresql:postgresql:42.7.4")  // для добавления зависимости PostgreSQL JDBC драйвера
//    implementation("org.hibernate:hibernate-core:7.0.0.Beta3")  // Hibernate Core

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.4.2")

    implementation("org.springframework.boot:spring-boot-starter-logging")  // Spring Boot Starter для логирования
//    implementation("org.slf4j:slf4j-api:2.1.0-alpha1") // SLF4J API

    implementation("org.springframework.boot:spring-boot-starter-security:3.4.2")
    implementation("org.springframework.security:spring-security-test:6.4.2")
    implementation("org.springframework.security:spring-security-config:6.4.2")
    implementation("org.springframework.security:spring-security-web:6.4.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.2")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

    implementation("io.jsonwebtoken:jjwt:0.12.6")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform()
}

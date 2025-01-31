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
    implementation("org.hibernate:hibernate-core:7.0.0.Beta3")  // Hibernate Core

    implementation("org.mongodb:mongodb-driver-sync:5.3.1") // для mongoDB
    implementation("org.mongodb:mongodb-driver-core:5.3.1")

}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform()
}

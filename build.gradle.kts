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
    implementation("org.junit:junit-bom:5.11.4") // управление версиями зависимостей, связанных с JUnit, с помощью BOM, что позволяет определить, какие версии различных библиотек следует использовать, чтобы они были совместимыми друг с другом
    testImplementation("org.junit.jupiter:junit-jupiter") // добавление зависимости для написания и запуска тестов в Java

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.postgresql:postgresql:42.7.4")  // для добавления зависимости PostgreSQL JDBC драйвера
    implementation("org.slf4j:slf4j-api:1.7.36") // Стабильная версия. Зависимость для логирования с использованием SLF4J
    implementation("ch.qos.logback:logback-classic:1.5.16") // Стабильная версия. Реализация для SLF4J
    implementation("org.hibernate:hibernate-core:7.0.0.Beta3")  // Hibernate Core
}

tasks.test {
    useJUnitPlatform()
}

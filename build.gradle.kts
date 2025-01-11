plugins {
    id("org.springframework.boot") version "3.1.0" // версия Spring Boot
    id("io.spring.dependency-management") version "1.1.0" // для управления зависимостями
    id("java")
}

group = "com.itexus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.34") // добавление зависимостей lombok
    annotationProcessor("org.projectlombok:lombok:1.18.34") // для обработки аннотаций
    testImplementation(platform("org.junit:junit-bom:5.9.1")) // управление версиями зависимостей, связанных с JUnit, с помощью BOM, что позволяет определить, какие версии различных библиотек следует использовать, чтобы они были совместимыми друг с другом
    testImplementation("org.junit.jupiter:junit-jupiter") // добавление зависимости для написания и запуска тестов в Java
    testImplementation("junit:junit:4.13.2") // добавления библиотеки JUnit, для написания и выполнения автоматизированных тестов на Java
    implementation("org.springframework:spring-context:6.1.14") // добавление зависимости на библиотеку Spring Context,
    implementation("org.springframework:spring-core:6.1.12") // добавление зависимости на библиотеку Spring Core
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2") // основной модуль для сериализации и десериализации JSON
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2") // содержит аннотации, такие как @JsonProperty, которые можно использовать в коде для управления сериализацией и десериализацией
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2") // базовый модуль, который необходим для работы Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.17.2") // для добавления зависимости на библиотеку Jackson Dataformat для работы с CSV файлами
    implementation("org.springframework:spring-aop:6.1.13") // зависимость для AOP
    implementation("org.slf4j:slf4j-api:2.0.16") // зависимость для логирования с использованием SLF4J
    implementation("ch.qos.logback:logback-classic:1.5.12") // реализация для SLF4J
    implementation("org.aspectj:aspectjweaver:1.9.22") // зависимость AspectJ
    implementation("org.aspectj:aspectjrt:1.9.22.1") // зависимость на AspectJ Runtime

    implementation("org.postgresql:postgresql:42.7.4") // для добавления зависимости PostgreSQL JDBC драйвера
    implementation("org.apache.commons:commons-dbcp2:2.12.0") // добавляет зависимость Apache Commons DBCP
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("org.hibernate:hibernate-core:7.0.0.Beta2") // Hibernate Core
    implementation("org.hibernate:hibernate-entitymanager:5.3.36.Final")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-web") // Spring Web

}

tasks.test {
    useJUnitPlatform()
}
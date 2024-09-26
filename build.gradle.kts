plugins {
    id("java")
}

group = "com.itexus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.34") // добавление зависимостей lombok
    annotationProcessor ("org.projectlombok:lombok:1.18.34") // для обработки аннотаций
    testImplementation(platform("org.junit:junit-bom:5.9.1")) // управление версиями зависимостей, связанных с JUnit, с помощью BOM, что позволяет определить, какие версии различных библиотек следует использовать, чтобы они были совместимыми друг с другом
    testImplementation("org.junit.jupiter:junit-jupiter") // добавление зависимости для написания и запуска тестов в Java
    testImplementation("junit:junit:4.13.2") // добавления библиотеки JUnit, для написания и выполнения автоматизированных тестов на Java
    implementation("org.springframework:spring-context:6.1.12") // добавление зависимости на библиотеку Spring Context,
    implementation("org.springframework:spring-core:6.1.12") // добавление зависимости на библиотеку Spring Core
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2") // основной модуль для сериализации и десериализации JSON
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2") // содержит аннотации, такие как @JsonProperty, которые можно использовать в коде для управления сериализацией и десериализацией
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2") // базовый модуль, который необходим для работы Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.17.2") // для добавления зависимости на библиотеку Jackson Dataformat для работы с CSV файлами
    implementation ("org.springframework.boot:spring-boot-starter-aop") // зависимость для Spring AOP

}

tasks.test {
    useJUnitPlatform()
}
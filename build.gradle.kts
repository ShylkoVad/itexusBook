plugins {
    id("java")
}

group = "com.itexus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.26")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("junit:junit:4.13.2")
    implementation("org.springframework:spring-context:5.3.10")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:4.13.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
}

tasks.test {
    useJUnitPlatform()
}
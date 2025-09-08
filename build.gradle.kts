plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // Add Javalin dependency
    implementation("io.javalin:javalin:4.6.0")
    // Add slf4j Logger dependency
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("ServerKt")
}
plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

group = "org.example"
version = "1.0"

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
    jvmToolchain(12)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(12))
    }
}

application {
    mainClass.set("vierGewinnt_javalin.ServerKt")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "vierGewinnt_javalin.ServerKt"
    }
    // Optional: alle Abhängigkeiten ins JAR packen (Fat JAR)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
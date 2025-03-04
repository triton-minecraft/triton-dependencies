plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":common"))

    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
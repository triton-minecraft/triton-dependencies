plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.big-minecraft:bmc-api:97074e9964")
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    implementation("net.minestom:minestom-snapshots:698af959c8")
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("dev.lu15:luckperms-minestom:5.4-SNAPSHOT")
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.kyriji.minestom.DependencyLoader"
            attributes["Module-Name"] = "minestom-dependencies"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }
}

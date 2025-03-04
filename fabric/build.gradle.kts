plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName = project.property("archives_base_name") as String
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "Mojang"
        url = uri("https://libraries.minecraft.net/")
    }
    // You might need to add more repositories based on your dependencies
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    implementation(project(":common"))

    compileOnly("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    compileOnly("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    // You might need to adjust these dependencies since they're no longer managed by Loom
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version, "minecraft_version" to project.property("minecraft_version"), "loader_version" to project.property("loader_version"))
    }
}

val targetJavaVersion = 21
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
}

tasks {
    build {
        dependsOn(shadowJar)
        jar.get().enabled = false
    }

    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.property("archivesBaseName")}" }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }

        create<MavenPublication>("shadow") {
            project.shadow.component(this)
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}
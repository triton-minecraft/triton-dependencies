import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("idea")
    id("net.minecraftforge.gradle") version "6.0.29"
    id("com.gradleup.shadow") version "8.3.5"
}

// Create a shade configuration
val shadeConfiguration: Configuration by configurations.creating

// Extend implementation configuration to include shade dependencies in dev
configurations["implementation"].extendsFrom(shadeConfiguration)

group = project.property("mod_group_id") as String
version = project.property("mod_version") as String

base {
    archivesName = project.property("mod_id") as String
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

minecraft {
    val mapping_channel: String by project
    val mapping_version: String by project

    mappings(mapping_channel, mapping_version)

    reobf = false
    copyIdeResources.set(true)

    runs {
        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
        }

        create("client") {
            property("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("server") {
            property("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
            args("--nogui")
        }

        create("gameTestServer") {
            property("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args(
                "--mod", project.property("mod_id") as String,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }
    }
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
    }
}

repositories {
    maven { url = uri("https://maven.minecraftforge.net/") }

    maven {
        url = uri("https://libraries.minecraft.net")
        content {
            includeModule("org.lwjgl", "lwjgl-freetype")
        }
    }

    mavenCentral()

    mavenLocal {
        content {
            includeGroup("dev.kyriji")
        }
    }
}

dependencies {
    // Implementation of common project
    implementation(project(":common"))

    // Compile-only dependencies
    compileOnly("net.luckperms:api:5.4")

    //bson
    implementation("org.mongodb:bson:4.2.3")

    //gson
    implementation("com.google.code.gson:gson:2.8.8")

    //bmc api
    implementation("dev.kyriji:bmc-api:0.0.0")
//    shadeConfiguration("dev.kyriji:bmc-api:0.0.0")


    // Minecraft and Forge
    val minecraft_version: String by project
    val forge_version: String by project
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

    // Dependencies to be shaded
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4") {
        version {
            strictly("5.0.4")
        }
    }
}

tasks.named<ProcessResources>("processResources") {
    val minecraft_version: String by project
    val minecraft_version_range: String by project
    val forge_version: String by project
    val forge_version_range: String by project
    val loader_version_range: String by project
    val mod_id: String by project
    val mod_name: String by project
    val mod_license: String by project
    val mod_version: String by project
    val mod_authors: String by project
    val mod_description: String by project

    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "forge_version" to forge_version,
        "forge_version_range" to forge_version_range,
        "loader_version_range" to loader_version_range,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version,
        "mod_authors" to mod_authors,
        "mod_description" to mod_description
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + mapOf("project" to project))
    }
}


// Define the relocation package base
val modId = project.property("mod_id") as String
val relocateBase = "${project.property("mod_group_id")}.${modId}.shadow"

tasks.named<Jar>("jar") {
    dependsOn("shadowJar")
    manifest {
        attributes(mapOf(
            "Specification-Title" to project.property("mod_id"),
            "Specification-Vendor" to project.property("mod_authors"),
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to project.property("mod_authors"),
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named<ShadowJar>("shadowJar") {
    // Enable verbose logging
    doFirst {
        println("Starting ShadowJar task")
        println("Shade Configuration contents:")
        shadeConfiguration.dependencies.forEach {
            println("Shade Dependency: ${it.group}:${it.name}:${it.version}")
        }
    }

    dependencies {
        // Explicitly include shade configuration
        from(shadeConfiguration)

        // Ensure dev.kyriji dependencies are included
        include { dep ->
            val shouldInclude = dep.moduleGroup.startsWith("dev.kyriji") ||
                    dep.moduleGroup == "org.mongodb" ||
                    dep.moduleGroup == "org.bson" ||
                    dep.moduleGroup == "com.google"

            if (shouldInclude) {
                println("Including dependency: ${dep.moduleGroup}:${dep.moduleName}:${dep.moduleVersion}")
            }

            shouldInclude
        }

        // Explicit inclusion of BMC API
        include(dependency("dev.kyriji:bmc-api"))
    }

    // Relocate dependencies
    relocate("com.google.gson", "$relocateBase.gson")
    relocate("org.slf4j", "$relocateBase.slf4j")
    relocate("redis.clients.jedis", "$relocateBase.jedis")

    // Minimize the jar while keeping necessary dependencies
    minimize {
        exclude(dependency("org.mongodb:.*"))
        exclude(dependency("org.bson:.*"))
        // Explicitly exclude BMC API from minimization
        exclude(dependency("dev.kyriji:bmc-api"))
    }

    // Log the final jar contents
    doLast {
        println("ShadowJar created. Contents:")
        archiveFile.get().asFile.listFiles()?.forEach {
            println(it.absolutePath)
        }
    }
}

sourceSets.forEach {
    val dir = layout.buildDirectory.dir("sourcesSets/${it.name}")
    it.output.resourcesDir = dir.get().asFile
    it.java.destinationDirectory.set(dir)
}

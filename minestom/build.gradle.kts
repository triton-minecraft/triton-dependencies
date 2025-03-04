plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("maven-publish")
}

group = "dev.kyriji"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":common"))

    implementation("net.minestom:minestom-snapshots:698af959c8")
    implementation("dev.lu15:luckperms-minestom:5.4-SNAPSHOT")
    implementation("dev.kyriji:triton-stom:0.0.0")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.kyriji.minestom.DependencyLoader"
            attributes["Module-Name"] = "triton-dependencies"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.shadow.component(this)

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}

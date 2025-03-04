rootProject.name = "triton-dependencies"
include("common")
include("spigot")
include("velocity")
include("minestom")
include("fabric")
include("forge")

project(":spigot").name = "triton-dependencies-spigot"
project(":velocity").name = "triton-dependencies-velocity"
project(":minestom").name = "triton-dependencies-minestom"
project(":fabric").name = "triton-dependencies-fabric"
project(":forge").name = "triton-dependencies-forge"

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "MinecraftForge"
            url = uri("https://maven.minecraftforge.net/")
        }
        gradlePluginPortal()
    }
}
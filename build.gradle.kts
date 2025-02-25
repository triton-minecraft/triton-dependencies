plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
}

dependencies {

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}






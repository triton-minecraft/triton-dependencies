plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}






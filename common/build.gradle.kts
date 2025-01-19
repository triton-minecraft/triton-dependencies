plugins {
    id("java")
}

group = "dev.kyriji"

repositories {
    mavenCentral()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.big-minecraft:bmc-api:97074e9964")
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")

}

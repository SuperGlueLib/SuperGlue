plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

val groupName = "com.github.supergluelib"
val artifactName = "SuperGlue"
val libraryVersion = "1.1.2"
group = groupName
version = libraryVersion

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://www.jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    api("com.github.Revxrsal.Lamp:common:3.1.5")
    api("com.github.Revxrsal.Lamp:bukkit:3.1.5")
    api("com.github.Revxrsal.Lamp:brigadier:3.1.5")
}

kotlin {
    jvmToolchain(17)
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = groupName
    artifactId = artifactName
    version = libraryVersion
    from(components["java"])
}

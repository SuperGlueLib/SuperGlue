plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

val groupName = "com.github.supergluelib"
val artifactName = "SuperGlue"
val libraryVersion = "1.1.3"
group = groupName
version = libraryVersion

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://www.jitpack.io")
}

val lampVersion = "4.0.0-rc.2"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    api("io.github.revxrsal:lamp.common:$lampVersion")
    api("io.github.revxrsal:lamp.bukkit:$lampVersion")
    api("io.github.revxrsal:lamp.brigadier:$lampVersion")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude("org.bukkit")
    }
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

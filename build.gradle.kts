plugins {
    kotlin("jvm") version "2.2.20"
    id("org.jetbrains.dokka") version "2.0.0"
    `maven-publish`
}

val groupName = "com.github.supergluelib"
val artifactName = "SuperGlue"
group = groupName

repositories {
    mavenCentral()
    mavenLocal() // For DeYaml until I publish it officially.
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://www.jitpack.io")
}

val lampVersion = "4.0.0-rc.2"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    implementation("com.github.mlgpenguin:DeYaml:1.0.1") {
        exclude(group = "org.yaml")
    }

    api("io.github.revxrsal:lamp.common:$lampVersion")
    api("io.github.revxrsal:lamp.bukkit:$lampVersion")
    api("io.github.revxrsal:lamp.brigadier:$lampVersion")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude("org.bukkit")
    }

    testCompileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = groupName
    artifactId = artifactName
//    version = libraryVersion
    from(components["java"])
}

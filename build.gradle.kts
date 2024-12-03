import io.izzel.taboolib.gradle.*
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    java
    kotlin("jvm") version "2.1.0"
    id("io.izzel.taboolib") version "2.0.22"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

taboolib {
    env {
        install(Bukkit)
        install(BukkitUI)
        install(BukkitUtil)
        install(Basic)
        install(BukkitHook)
        install(MinecraftChat)
        install(MinecraftEffect)
        install(CommandHelper)
        install(I18n)
        install(Metrics)
        install(Database)
        install(IOC)
        install(DatabasePlayer)
        install(Ptc)
        install(PtcObject)
    }
    description {
        name = "WolfHunter"
        contributors {
            name("MikkoAyaka")
        }
    }
    version { taboolib = "6.2.1-f095116" }
}

group = "cn.wolfmc.minecraft.wolfhunter"
version = "1.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}
val scoreboardLibraryVersion = "2.2.1"
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly("org.xerial:sqlite-jdbc:3.47.0.0")

    compileOnly("net.megavex:scoreboard-library-api:$scoreboardLibraryVersion")
    compileOnly("net.megavex:scoreboard-library-extra-kotlin:$scoreboardLibraryVersion")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
    compilerOptions {
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

tasks {
    runServer {
        dependsOn("clean")
        minecraftVersion("1.18.2")
    }
    build {
        dependsOn("jar")
    }
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

configure<KtlintExtension> {
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
    }
}

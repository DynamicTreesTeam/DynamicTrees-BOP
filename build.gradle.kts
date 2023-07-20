import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseExtension
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import net.minecraftforge.gradle.common.util.RunConfig
import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()
fun optionalProperty(key: String) = project.findProperty(key)?.toString()

apply(from = "https://gist.githubusercontent.com/Harleyoc1/4d23d4e991e868d98d548ac55832381e/raw/applesiliconfg.gradle")

plugins {
    id("java")
    id("net.minecraftforge.gradle")
    id("org.parchmentmc.librarian.forgegradle")
    id("idea")
    id("maven-publish")
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("com.harleyoconnor.autoupdatetool") version "1.0.5"
}

repositories {
    maven("https://ldtteam.jfrog.io/ldtteam/modding/")
    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    maven("https://harleyoconnor.com/maven")
    maven("https://squiddev.cc/maven/")
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")
val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

minecraft {
    mappings("parchment", "${property("mappingsVersion")}-$mcVersion")

    runs {
        create("client") {
            applyDefaultConfiguration()

            if (project.hasProperty("mcUuid")) {
                args("--uuid", property("mcUuid"))
            }
            if (project.hasProperty("mcUsername")) {
                args("--username", property("mcUsername"))
            }
            if (project.hasProperty("mcAccessToken")) {
                args("--accessToken", property("mcAccessToken"))
            }
        }

        create("server") {
            applyDefaultConfiguration("run-server")
        }

        create("data") {
            applyDefaultConfiguration()

            args(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources"),
                "--existing-mod", "dynamictrees",
                "--existing-mod", "biomesoplenty"
            )
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    srcDir("src/localization/resources")
}

dependencies {
    minecraft("net.minecraftforge:forge:${mcVersion}-${property("forgeVersion")}")

    implementation(fg.deobf("com.ferreusveritas.dynamictrees:DynamicTrees-$mcVersion:${property("dynamicTreesVersion")}"))
    implementation(fg.deobf("curse.maven:biomes-o-plenty-220318:4473556"))

    runtimeOnly(fg.deobf("com.ferreusveritas.dynamictreesplus:DynamicTreesPlus-$mcVersion:${property("dynamicTreesPlusVersion")}"))
    runtimeOnly(fg.deobf("curse.maven:terrablender-563928:4618490"))
    runtimeOnly(fg.deobf("curse.maven:jade-324717:4433884"))
    runtimeOnly(fg.deobf("curse.maven:jei-238222:4615177"))
    runtimeOnly(fg.deobf("org.squiddev:cc-tweaked-$mcVersion:${property("ccVersion")}"))
    runtimeOnly(fg.deobf("com.harleyoconnor.suggestionproviderfix:SuggestionProviderFix-1.19:${property("suggestionProviderFixVersion")}"))
}

tasks.jar {
    manifest.attributes(
        "Specification-Title" to modName,
        "Specification-Vendor" to "mangoose",
        "Specification-Version" to "1",
        "Implementation-Title" to modName,
        "Implementation-Version" to project.version,
        "Implementation-Vendor" to "mangoose",
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    archiveBaseName.set(modName)
    finalizedBy("reobfJar")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val changelogFile = file("build/changelog.txt")

curseforge {
    if (!project.hasProperty("curseApiKey")) {
        project.logger.warn("API Key for CurseForge not detected; uploading will be disabled.")
        return@curseforge
    }

    apiKey = property("curseApiKey")

    project {
        id = "289529"

        addGameVersion(mcVersion)

        changelog = changelogFile
        changelogType = "markdown"
        releaseType = optionalProperty("versionType") ?: "release"

        addArtifact(tasks.findByName("sourcesJar"))

        mainArtifact(tasks.findByName("jar")) {
            relations {
                requiredDependency("dynamictrees")
                requiredDependency("biomes-o-plenty")
                optionalDependency("dynamictreesplus")
            }
        }
    }
}

val minecraftVersion = mcVersion

autoUpdateTool {
    mcVersion.set(minecraftVersion)
    version.set(modVersion)
    versionRecommended.set(property("versionRecommended") == "true")
    changelogOutputFile.set(changelogFile)
    updateCheckerFile.set(file(property("dynamictrees.version_info_repo.path") + File.separatorChar + property("updateCheckerPath")))
}

tasks.autoUpdate {
    finalizedBy("curseforge")
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven("file:///${project.projectDir}/mcmodsrepo")
    }
}

fun RunConfig.applyDefaultConfiguration(runDirectory: String = "run") {
    workingDirectory = file(runDirectory).absolutePath

    property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
    property("forge.logging.console.level", "debug")

    property("mixin.env.remapRefMap", "true")
    property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")

    mods {
        create(modId) {
            source(sourceSets.main.get())
        }
    }
}

fun CurseExtension.project(action: CurseProject.() -> Unit) {
    this.project(closureOf(action))
}

fun CurseProject.mainArtifact(artifact: Task?, action: CurseArtifact.() -> Unit) {
    this.mainArtifact(artifact, closureOf(action))
}

fun CurseArtifact.relations(action: CurseRelation.() -> Unit) {
    this.relations(closureOf(action))
}
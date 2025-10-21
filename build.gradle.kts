import java.util.*

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("me.modmuss50.mod-publish-plugin")
    id("com.github.johnrengelman.shadow")
}

val minecraft = stonecutter.current.version
val loader = loom.platform.get().name.lowercase()

version = "${mod.version}+$minecraft"
group = mod.group
base {
    archivesName.set("${mod.id}-$loader")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.prop("loom.platform")
})
repositories {
    maven("https://maven.neoforged.net/releases/")

    // kotlinforforge because apparently one of these next guys needs it
    maven("https://thedarkcolour.github.io/KotlinForForge")
    // cloth config
    maven("https://maven.shedaniel.me/")
    // modmenu
    maven("https://maven.terraformersmc.com/releases/")
    // yacl
    maven("https://maven.isxander.dev/releases")

    // modrinth maven
    maven("https://api.modrinth.com/maven")

    // placeholder api (modmenu depencency)
    maven("https://maven.nucleoid.xyz/")
}
dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    implementation("org.antlr:ST4:4.3.4")
    includeInternal("org.antlr:ST4:4.3.4")

    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        mappings("net.fabricmc:yarn:$minecraft+build.${mod.dep("yarn_build")}:v2")
        modApi("com.terraformersmc:modmenu:${mod.dep("modmenu_version")}")

        modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_version")}")

        modApi("me.shedaniel.cloth:cloth-config-fabric:${mod.dep("cloth_version")}")
        modApi("dev.isxander:yet-another-config-lib:${mod.dep("yacl_version")}")
        modImplementation("maven.modrinth:iris:${mod.dep("iris_version")}")
        modImplementation("maven.modrinth:sodium:${mod.dep("sodium_version")}")

        modApi("dev.architectury:architectury-fabric:${mod.dep("architectury_api")}")
    }
    if (loader == "neoforge") {
        "neoForge"("net.neoforged:neoforge:${mod.dep("neoforge_loader")}")
        mappings(loom.layered {
            mappings("net.fabricmc:yarn:$minecraft+build.${mod.dep("yarn_build")}:v2")
            mod.dep("neoforge_patch").takeUnless { it.startsWith('[') }?.let {
                mappings("dev.architectury:yarn-mappings-patch-neoforge:$it")
            }
        })

        modApi("me.shedaniel.cloth:cloth-config-fabric:${mod.dep("cloth_version")}")
        modApi("dev.isxander:yet-another-config-lib:${mod.dep("yacl_version")}")
        modImplementation("maven.modrinth:iris:${mod.dep("iris_version")}")
        modImplementation("maven.modrinth:sodium:${mod.dep("sodium_version")}")
        modApi("dev.architectury:architectury-neoforge:${mod.dep("architectury_api")}")

    }
}

loom {
    accessWidenerPath = rootProject.file("src/main/resources/screenshotsettings.accesswidener")

    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}

publishMods {
    var modrinthToken = System.getenv("MODRINTH_TOKEN")
    var curseforgeToken = System.getenv("CURSEFORGE_TOKEN")



    file = project.tasks.remapJar.get().archiveFile
    dryRun = modrinthToken == "" || curseforgeToken == ""

    displayName = "${mod.name} ${loader.replaceFirstChar { it.uppercase() }} ${property("mod.mc_title")}-${mod.version}"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = BETA

    modLoaders.add(loader)

    val targets = property("mod.mc_targets").toString().split(' ')


        modrinth {
            projectId = property("publish.modrinth").toString()
            accessToken = modrinthToken
            targets.forEach(minecraftVersions::add)
            if (loader == "fabric") {
                requires("fabric-api")
                requires("architectury-api")
                optional("modmenu")
            }
        }

        curseforge {
            projectId = property("publish.curseforge").toString()
            accessToken = curseforgeToken
            targets.forEach(minecraftVersions::add)
            if (loader == "fabric") {
                requires("fabric-api")
                requires("architectury-api")
                optional("modmenu")
            }
        }
    }


java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5")) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener = true
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier = "dev"
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
}

tasks.processResources {
    properties(
        listOf("fabric.mod.json"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_fabric")
    )
    properties(
        listOf("META-INF/neoforge.mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_forgelike")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}

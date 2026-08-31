plugins {
	id("mod-platform")
	id("dev.kikugie.loom-back-compat")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)
}

// YACL only publishes builds for 1.20.1 and newer; older targets fall back to Cloth Config.
val usesYacl = propOrNull("deps.yacl") != null

platform {
	loader = "fabric"

	entrypoint("client", "${prop("mod.group")}.${prop("mod.id")}.platform.fabric.FabricClientEntrypoint")
	entrypoint("modmenu", "${prop("mod.group")}.${prop("mod.id")}.platform.fabric.ModMenuIntegration")

	dependencies {
		required("minecraft") {
			fabricLikeVersionRange = minecraftFabricRange()
		}
		required("fabricloader") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
		}
		optional("fabric-api") {
			slug("fabric-api")
			fabricLikeVersionRange = "*"
		}
		optional("modmenu") {
			slug("modmenu")
			environment = "client"
		}
		if (usesYacl) optional("yet_another_config_lib_v3") {
			slug("yacl")
			environment = "client"
		} else optional("cloth-config") {
			slug("cloth-config")
			environment = "client"
		}
	}
}

loom {
	rootProject.file("src/main/resources/aw/${sc.current.version}.accesswidener")
		.takeIf { it.exists() }
		?.let { accessWidenerPath = it }

	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client (${sc.current.version})"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server (${sc.current.version})"
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://maven.isxander.dev/releases", "dev.isxander") { name = "Xander" }
	// YACL 3.6.x pulls org.quiltmc.parsers transitively.
	strictMaven("https://maven.quiltmc.org/repository/release/", "org.quiltmc", "org.quiltmc.parsers") { name = "QuiltMC" }
	strictMaven("https://maven.shedaniel.me/", "me.shedaniel", "me.shedaniel.cloth") { name = "Shedaniel" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

configurations.all {
	resolutionStrategy {
		force("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	// Minecraft 26+ ships usable names already; older versions need the official Mojang mappings.
	if (sc.current.parsed < "26") {
		mappings(loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment"))
				parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	}
	modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	// Only the ModMenuApi interface is needed at compile time; Mod Menu's own transitive
	// dependencies (placeholder-api and friends) would otherwise drag in extra mavens.
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}") { isTransitive = false }

	if (usesYacl) {
		modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")
	} else {
		modImplementation("me.shedaniel.cloth:cloth-config-fabric:${prop("deps.cloth-config")}") {
			exclude(group = "net.fabricmc.fabric-api")
		}
	}
}

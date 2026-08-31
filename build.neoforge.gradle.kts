plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)
}

platform {
	loader = "neoforge"
	dependencies {
		// `environment` is left unset throughout: dependencies inherit `mod.environment`,
		// which is `client` for this mod.
		required("minecraft") {
			forgeLikeVersionRange = minecraftForgeRange()
		}
		// Optional `deps.neoforge_range_lower` raises the floor on targets that need a NeoForge
		// feature younger than the oldest loader their Minecraft range admits. Unset means any.
		required("neoforge") {
			forgeLikeVersionRange.set("[${propOr("deps.neoforge_range_lower", "1")},)")
		}
		optional("yet_another_config_lib_v3") {
			slug("yacl")
			forgeLikeVersionRange.set("[1,)")
		}
	}
}

neoForge {
	version = prop("deps.neoforge")

	rootProject.file("src/main/resources/aw/${sc.current.version}.cfg")
		.takeIf { it.exists() }
		?.let {
			accessTransformers.from(it)
			validateAccessTransformers = true
		}

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${sc.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${sc.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.isxander.dev/releases", "dev.isxander") { name = "Xander" }
	// YACL 3.6.x pulls org.quiltmc.parsers transitively.
	strictMaven("https://maven.quiltmc.org/repository/release/", "org.quiltmc", "org.quiltmc.parsers") { name = "QuiltMC" }
	strictMaven("https://maven.shedaniel.me/", "me.shedaniel", "me.shedaniel.cloth") { name = "Shedaniel" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	compileOnly("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")
	runtimeOnly("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

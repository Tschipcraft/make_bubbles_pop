plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)
}

platform {
	loader = "forge"
	dependencies {
		// `environment` is left unset throughout: dependencies inherit `mod.environment`,
		// which is `client` for this mod.
		required("minecraft") {
			forgeLikeVersionRange = minecraftForgeRange()
		}
		required("forge") {
			forgeLikeVersionRange.set("[1,)")
		}
		optional("cloth_config") {
			slug("cloth-config")
			forgeLikeVersionRange.set("[1,)")
		}
	}
}

legacyForge {
	version = "${prop("deps.minecraft")}-${prop("deps.forge")}"

	rootProject.file("src/main/resources/aw/${sc.current.version}.cfg")
		.takeIf { it.exists() }
		?.let {
			accessTransformers.from(it)
			validateAccessTransformers = true
		}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${sc.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "Forge Server (${sc.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenCentral()
	strictMaven("https://maven.shedaniel.me/", "me.shedaniel", "me.shedaniel.cloth") { name = "Shedaniel" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")

	compileOnly("me.shedaniel.cloth:cloth-config-forge:${prop("deps.cloth-config")}")
	runtimeOnly("me.shedaniel.cloth:cloth-config-forge:${prop("deps.cloth-config")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

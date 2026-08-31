pluginManagement {
	repositories {
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
		maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
		maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
		maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
	}
	includeBuild("build-logic")
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
	id("dev.kikugie.stonecutter") version "0.9.2"
	id("dev.kikugie.loom-back-compat") version "0.4.1"
}

stonecutter {
	create(rootProject) {
		fun match(version: String, vararg loaders: String) =
			loaders.forEach { version("$version-$it", version).buildscript = "build.$it.gradle.kts" }

		// A target exists only where the produced jar has to differ. Fabric jars are remapped to
		// intermediary, which is stable across Minecraft versions, so renames and package moves
		// cost nothing at runtime and one build can cover a wide range (see the `minecraft-range`
		// entries in stonecutter.properties.toml). NeoForge and Forge run against Mojang names
		// directly, so every rename needs its own build.
		//
		// Forge is limited to <=1.20.1 because that is the ceiling of ModDevGradle's
		// `legacyforge` plugin, and ForgeGradle 6 (the only toolchain covering newer
		// MinecraftForge) refuses to run on Gradle 9, which Stonecutter requires.
		match("26.2", "fabric", "neoforge")
		match("26.1.2", "fabric", "neoforge")
		match("1.21.11", "fabric", "neoforge")
		match("1.21.8", "neoforge")
		match("1.21.5", "neoforge")
		match("1.21.4", "fabric", "neoforge")
		match("1.21.1", "fabric", "neoforge")
		match("1.20.1", "fabric", "forge")
		match("1.19.4", "fabric", "forge")
		match("1.19.2", "fabric", "forge")
		match("1.18.2", "fabric", "forge")

		vcsVersion = "1.21.1-fabric"
	}
}

@file:OptIn(dev.kikugie.stonecutter.StonecutterExperimentalAPI::class)

plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.loom.back.compat).apply(false)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.legacyforge.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.mod.publish.plugin).apply(false)
}

stonecutter active file(".sc_active_version")

tasks.register("runActiveClient") {
	group = "stonecutter"
	description = "Run client of the active Stonecutter version"
	dependsOn(stonecutter.current!!.project + ":runClient")
}

tasks.register("runActiveServer") {
	group = "stonecutter"
	description = "Run server of the active Stonecutter version"
	dependsOn(stonecutter.current!!.project + ":runServer")
}

// Fabric targets older than 1.20.1 predate YACL 3 and fall back to Cloth Config.
// Keep in sync with the `deps.yacl` / `deps.cloth-config` entries in stonecutter.properties.toml.
val clothOnlyFabricVersions = setOf("1.18.2", "1.19.2", "1.19.4")

stonecutter parameters {
	val loader = current.project.substringAfterLast('-')
	constants.match(loader, "fabric", "neoforge", "forge")

	// Which config-screen library this target compiles against. Storage is always platform-native
	// and independent of these; only the screen implementation varies.
	constants["yacl"] = loader == "neoforge" || (loader == "fabric" && current.version !in clothOnlyFabricVersions)
	constants["cloth"] = loader == "forge" || (loader == "fabric" && current.version in clothOnlyFabricVersions)

	swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
	swaps["mod_id"] = "\"${properties.get<String>("mod.id")}\";"
	swaps["mod_name"] = "\"${properties.get<String>("mod.name")}\";"
	swaps["minecraft"] = "\"${current.version}\";"
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
	group = "publishing"
	dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}

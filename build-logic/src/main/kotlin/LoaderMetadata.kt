import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class FabricManifest(
	val schemaVersion: Int = 1,
	val id: String,
	val name: String,
	val version: String,
	val authors: List<String>,
	val contributors: List<String>,
	val contact: Map<String, String>,
	val custom: JsonObject?,
	val description: String,
	val icon: String,
	val license: String,
	val environment: String = "*",
	/** Null when the mod ships no access widener for this version - the key is then omitted. */
	val accessWidener: String?,
	val entrypoints: Map<String, List<String>>,
	val mixins: List<String>,
	val depends: Map<String, String> = emptyMap(),
	val recommends: Map<String, String> = emptyMap(),
	val breaks: Map<String, String> = emptyMap(),
	val provides: List<String> = emptyList()
)

@Serializable
data class ForgeManifest(
	val modLoader: String = "javafml",
	/** `[1,)` rather than upstream's `[2,)` so legacy MinecraftForge targets are covered too. */
	val loaderVersion: String = "[1,)",
	val license: String,
	val issueTrackerURL: String,
	val mods: List<ForgeMod>,
	val dependencies: Map<String, List<ForgeDependency>> = emptyMap(),
	val mixins: List<ForgeMixin> = emptyList(),
	/** Null when the mod ships no access transformer - the key is then omitted entirely. */
	val accessTransformers: List<ForgeAccessTransformer>? = null,
	/**
	 * `[features.<modid>]` - load-time requirements FML checks before accepting the mod. Null when
	 * the declared range has none, so the key is dropped rather than written as `features = {  }`.
	 */
	val features: Map<String, Map<String, String>>? = null,
	val modproperties: Map<String, Map<String, String>> = emptyMap()
)

@Serializable
data class ForgeMod(
	val modId: String,
	val displayName: String,
	val version: String,
	val displayURL: String,
	val modUrl: String,
	val updateJSONURL: String? = null,
	val logoFile: String,
	val authors: String,
	val logoBlur: Boolean = false,
	val credits: String,
	val description: String,
	/**
	 * `IGNORE_ALL_VERSION` for client-only mods, so they do not fail the server-side version
	 * check for players joining vanilla servers. Null leaves the Forge default in place.
	 */
	val displayTest: String? = null
)

@Serializable
data class ForgeDependency(
	val modId: String,
	val side: String,
	val versionRange: String,
	val mandatory: Boolean,
	val type: String
)

@Serializable
data class ForgeMixin(val config: String)

@Serializable
data class ForgeAccessTransformer(val file: String)

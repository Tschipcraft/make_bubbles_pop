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
	/** Null omits the key - see [Context.accessWidenerPath]. */
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
	/** `[1,)` rather than upstream's `[2,)` to cover legacy MinecraftForge. */
	val loaderVersion: String = "[1,)",
	val license: String,
	/**
	 * Links the licence name on the NeoForge 26.2 mod screen. File-level, not per-mod:
	 * `DefaultModDisplayInfo.license()` reads it off the owning file.
	 */
	val licenseURL: String? = null,
	val issueTrackerURL: String,
	val mods: List<ForgeMod>,
	val dependencies: Map<String, List<ForgeDependency>> = emptyMap(),
	val mixins: List<ForgeMixin> = emptyList(),
	/** Null omits the key - see [Context.accessTransformerPath]. */
	val accessTransformers: List<ForgeAccessTransformer>? = null,
	/**
	 * `[features.<modid>]`, checked by FML before loading. Null drops the key rather than writing
	 * `features = {  }`.
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
	/**
	 * Deprecated by NeoForge 26.2 for [bannerFile] and [iconFile] but still emitted: older loaders
	 * read it, and NeoForge suppresses the deprecation warning when a replacement accompanies it.
	 */
	val logoFile: String,
	/** Wide image in the 26.2 info panel; falls back to [logoFile]. */
	val bannerFile: String? = null,
	/** Square image in the 26.2 mod list; no fallback, so omitting it means no icon. */
	val iconFile: String? = null,
	val authors: String,
	val logoBlur: Boolean = false,
	/** [logoBlur] for [iconFile]; 26.2 reads no blur key for the banner and ignores [logoBlur]. */
	val iconBlur: Boolean? = null,
	val credits: String,
	val description: String,
	/** See [Context.forgeDisplayTest]. */
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

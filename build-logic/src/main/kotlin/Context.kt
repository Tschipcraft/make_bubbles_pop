import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

@OptIn(StonecutterExperimentalAPI::class)
class Context(
	val project: Project,
	val extension: ModPlatformExtension,
	val loader: Loader,
	val stonecutter: StonecutterBuildExtension
) {
	private fun require(key: String): String =
		runCatching { project.sc.properties.get<String>(key) }.getOrNull()?.takeIf { it.isNotBlank() }
			?: error("Missing required property '$key' in stonecutter.properties.toml")

	private fun optional(key: String, fallback: String = ""): String =
		runCatching { project.sc.properties.get<String>(key) }.getOrNull()?.takeIf { it.isNotBlank() } ?: fallback

	val currentMcVersion: String by lazy {
		stonecutter.current.version
	}

	val modId: String by lazy { require("mod.id") }
	val modName: String by lazy { require("mod.name") }
	val modGroup: String by lazy { require("mod.group") }
	val modVersion: String by lazy { require("mod.version") }
	val channelTag: String by lazy { optional("mod.channel_tag") }
	val description: String by lazy { optional("mod.description") }
	val licenseName: String by lazy { require("mod.license.name") }
	val licenseUrl: String by lazy { require("mod.license.url") }
	val licenseDist: String by lazy { optional("mod.license.dist", "repo") }
	val inceptionYear: String by lazy { optional("mod.inception_year") }
	val icon: String by lazy { optional("mod.icon", "assets/icon.png") }

	/** One of `client`, `server` or `both`. */
	val environment: String by lazy { optional("mod.environment", "both") }

	/** The `environment` value understood by `fabric.mod.json`. */
	val fabricEnvironment: String by lazy {
		when (environment.lowercase()) {
			"client" -> "client"
			"server" -> "server"
			else -> "*"
		}
	}

	/** The `side` value understood by Forge-like dependency entries. */
	val forgeSide: String by lazy {
		when (environment.lowercase()) {
			"client" -> "CLIENT"
			"server" -> "SERVER"
			else -> "BOTH"
		}
	}

	/**
	 * `displayTest` for legacy Forge; null keeps the default `MATCH_VERSION`. Client-only mods get
	 * `IGNORE_ALL_VERSION` so vanilla servers accept them. Anything else cannot be inferred (a
	 * server-side mod shipping client assets is `both` yet wants `IGNORE_SERVER_VERSION`), so it
	 * is set explicitly through `mod.forge_display_test`.
	 */
	val forgeDisplayTest: String? by lazy {
		optional("mod.forge_display_test").takeIf { it.isNotBlank() }
			?: "IGNORE_ALL_VERSION".takeIf { environment.equals("client", ignoreCase = true) }
	}

	val authors: List<String> by lazy {
		runCatching {
			project.sc.properties.raw("mod", "authors").asList().map { it.toString() }
		}.getOrElse { error("Missing or malformed 'mod.authors' in stonecutter.properties.toml") }
	}

	val contributors: List<String> by lazy {
		runCatching {
			project.sc.properties.raw("mod", "contributors").asList().map { it.toString() }
		}.getOrElse { emptyList() }
	}

	val sourcesUrl: String by lazy { require("mod.sources_url") }
	val homepageUrl: String by lazy { require("mod.homepage_url") }
	val issuesUrl: String by lazy { optional("mod.issues_url", "$sourcesUrl/issues") }

	/** Free-form link map surfaced in the Mod Menu entry, keyed by translation key. */
	val links: Map<String, String> by lazy {
		runCatching {
			project.sc.properties.raw("mod", "links").asMap()
				.entries.associate { it.key to it.value.toString() }
		}.getOrElse { emptyMap() }
	}

	/** Modrinth project id, used to point Forge/NeoForge update checks at the Modrinth API. */
	val modrinthId: String by lazy { optional("mod.modrinth_id") }

	/**
	 * Declared via `entrypoint(...)` in `build.fabric.gradle.kts`. No default: a guessed set would
	 * name classes that do not exist and only fail at runtime.
	 */
	val entrypoints: Map<String, List<String>> by lazy {
		extension.entrypoints.get().ifEmpty {
			error("No Fabric entrypoints declared - add entrypoint(\"main\", \"...\") to the platform block")
		}
	}

	val isSnapshot: Boolean by lazy { !project.envTrue("MOD_IS_RELEASE") }
	val baseVersion: String by lazy { "$modVersion$channelTag" }
	val snapshotSuffix: String by lazy { if (isSnapshot) "-SNAPSHOT" else "" }
	val basicVersion: String by lazy { "$baseVersion$snapshotSuffix" }

	/** `mc1.21.5-1.21.11`, `mc1.21.5+` or `mc26.1.2` - see [Project.minecraftRangeSlug]. */
	val minecraftRangeSlug: String by lazy { project.minecraftRangeSlug() }

	/** Oldest Minecraft version the jar advertises - see [Project.minecraftRangeLower]. */
	val minecraftRangeLower: String by lazy { project.minecraftRangeLower() }

	/** Newest Minecraft version the jar advertises, blank when open-ended - see [reachesMinecraft]. */
	val minecraftRangeUpper: String by lazy { project.minecraftRangeUpper() }

	/**
	 * Whether the advertised range reaches [version]; a blank upper bound is open-ended.
	 *
	 * Which bound a manifest key gates on depends on how loaders treat it when unknown. FML ignores
	 * unknown manifest keys, so emitting early is free and the *newest* loader in range decides:
	 * gate on the upper bound, here. An unknown `[features]` entry is a hard rejection, so there
	 * the *oldest* loader decides and [Loader.Forge.javaVersionFeature] gates on
	 * [minecraftRangeLower] instead.
	 */
	fun reachesMinecraft(version: String): Boolean =
		minecraftRangeUpper.isBlank() || stonecutter.eval(minecraftRangeUpper, ">=$version")

	/**
	 * Jar name, Maven version and published version. Uses the range slug rather than the compiled
	 * version so the jar states what it runs on, joined with `-` rather than upstream's `+` because
	 * an open-ended slug already ends in `+`.
	 */
	val fullVersion: String by lazy { "$baseVersion-${loader.id}-$minecraftRangeSlug$snapshotSuffix" }

	val publishAdditionalVersions: List<String> by lazy {
		project.sc.properties.rawOrNull("publish", "additionalVersions")?.to<List<String>>().orEmpty()
	}

	/** Null when this version ships no access widener/transformer; the manifest key is omitted. */
	val accessWidenerPath: String? by lazy {
		"aw/$currentMcVersion.accesswidener".takeIf {
			project.rootProject.file("src/main/resources/$it").exists()
		}
	}

	val accessTransformerPath: String? by lazy {
		"aw/$currentMcVersion.cfg".takeIf {
			project.rootProject.file("src/main/resources/$it").exists()
		}
	}

	val javaVersion: JavaVersion by lazy {
		when {
			stonecutter.eval(currentMcVersion, ">=26") -> JavaVersion.VERSION_25
			stonecutter.eval(currentMcVersion, ">=1.20.5") -> JavaVersion.VERSION_21
			stonecutter.eval(currentMcVersion, ">=1.18") -> JavaVersion.VERSION_17
			stonecutter.eval(currentMcVersion, ">=1.17") -> JavaVersion.VERSION_16
			else -> JavaVersion.VERSION_1_8
		}
	}
}

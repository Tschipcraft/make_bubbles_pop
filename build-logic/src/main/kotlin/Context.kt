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
	 * `displayTest` for the Forge-like manifests. Null leaves the Forge default
	 * (`MATCH_VERSION`) in place, which requires the mod on both sides at the same version.
	 *
	 * `mod.environment = "client"` implies `IGNORE_ALL_VERSION`, because a client-only mod must
	 * not fail the version check for players joining vanilla servers. Everything else is a
	 * genuine per-mod choice and cannot be inferred - a server-side mod that still ships client
	 * assets is `environment = "both"` yet usually wants `IGNORE_SERVER_VERSION` - so
	 * `mod.forge_display_test` overrides it explicitly.
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
	 * Declared in `build.fabric.gradle.kts` via `entrypoint(...)`. Deliberately has no default:
	 * guessing a conventional set would silently generate a manifest naming classes the source
	 * set does not contain, which only fails at runtime.
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

	/**
	 * Drives the jar name, the Maven version and the published version string. The range slug is
	 * used rather than the compiled version so a jar advertises the span it actually supports.
	 *
	 * Joined with `-` rather than the `+` build-metadata separator upstream uses, because an
	 * open-ended slug already ends in `+` and two of them in one version string reads badly.
	 */
	val fullVersion: String by lazy { "$baseVersion-${loader.id}-$minecraftRangeSlug$snapshotSuffix" }

	val publishAdditionalVersions: List<String> by lazy {
		project.sc.properties.rawOrNull("publish", "additionalVersions")?.to<List<String>>().orEmpty()
	}

	/**
	 * Access wideners/transformers are optional - when the file for the current version is absent
	 * the manifest key is omitted entirely rather than pointing at nothing.
	 */
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

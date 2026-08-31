@file:Suppress("unused")

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import dev.eav.tomlkt.Toml
import org.gradle.api.NamedDomainObjectContainer
import java.util.*

// kotlinx defaults to a four-space indent; two matches the hand-written fabric.mod.json
// convention, so a generated manifest still diffs cleanly against one checked in before.
@OptIn(ExperimentalSerializationApi::class)
private val JSON = Json {
	prettyPrint = true
	prettyPrintIndent = "  "
	encodeDefaults = true
	explicitNulls = false
}
private val TOML = Toml { explicitNulls = false }

/** Store-only dependencies are skipped - see [Dependency.declareInManifest]. */
private fun NamedDomainObjectContainer<Dependency>.manifestEntries(): List<Dependency> =
	filter { it.declareInManifest.get() }

sealed class Loader(val id: String) {
	abstract val modManifestPath: String
	abstract val excludedResources: List<String>

	open val isFabricLike: Boolean = false

	abstract fun generateManifest(ctx: Context): String

	object Fabric : Loader("fabric") {
		override val isFabricLike = true
		override val modManifestPath = "fabric.mod.json"
		override val excludedResources = listOf(
			"META-INF/mods.toml", "META-INF/neoforge.mods.toml", "aw/*.cfg", ".cache", "pack.mcmeta"
		)

		/** Mod Menu link entries plus the Catalogue banner, driven by the `[mod.links]` table. */
		private fun customBlock(ctx: Context): JsonObject? = buildJsonObject {
			if (ctx.links.isNotEmpty()) putJsonObject("modmenu") {
				putJsonObject("links") {
					ctx.links.forEach { (key, url) -> put(key, url) }
				}
			}
			putJsonObject("catalogue") { put("banner", ctx.icon) }
		}.takeIf { it.isNotEmpty() }

		override fun generateManifest(ctx: Context): String {
			val manifest = FabricManifest(
				id = ctx.modId,
				name = ctx.modName,
				version = ctx.baseVersion,
				authors = ctx.authors,
				contributors = ctx.contributors,
				contact = mapOf(
					"sources" to ctx.sourcesUrl, "issues" to ctx.issuesUrl, "homepage" to ctx.homepageUrl
				),
				custom = customBlock(ctx),
				description = ctx.description,
				icon = ctx.icon,
				license = ctx.licenseName,
				environment = ctx.fabricEnvironment,
				accessWidener = ctx.accessWidenerPath,
				entrypoints = ctx.entrypoints,
				mixins = listOf("${ctx.modId}.mixins.json"),
				depends = ctx.extension.dependencies.required.manifestEntries()
					.associate { it.modid.get() to it.fabricLikeVersionRange.get() },
				recommends = ctx.extension.dependencies.optional.manifestEntries()
					.associate { it.modid.get() to it.fabricLikeVersionRange.get() },
				breaks = ctx.extension.dependencies.incompatible.manifestEntries()
					.associate { it.modid.get() to it.fabricLikeVersionRange.get() },
				provides = ctx.extension.dependencies.embeds.manifestEntries().map { it.modid.get() }
			)
			return JSON.encodeToString(manifest)
		}
	}

	sealed class ForgeLike(id: String) : Loader(id) {
		override val excludedResources = listOf(
			"fabric.mod.json", "aw/*.accesswidener", ".cache"
		)

		/** Query parameter Modrinth's update-check endpoint expects for this loader. */
		protected open val updateCheckLoader: String get() = id

		/**
		 * NeoForge retired `displayTest` - it is no longer part of the default
		 * `neoforge.mods.toml` - so only legacy MinecraftForge still gets the key.
		 */
		protected open val supportsDisplayTest: Boolean get() = true

		/**
		 * Name FML registers the Java version feature under, or null where nothing in the declared
		 * range registers one. This is not cosmetic: an unrecognised name falls back to
		 * `MissingFeatureTest`, which reports its value as `NONE` and fails on both sides, so
		 * naming it wrong rejects the mod outright rather than being ignored.
		 *
		 * Every NeoForge build knows `javaVersion` - NeoForge forked MinecraftForge at 1.20.1 in
		 * July 2023, after the rename [Forge.javaVersionFeature] describes - so no gate is needed
		 * here.
		 */
		protected open fun javaVersionFeature(ctx: Context): String? = "javaVersion"

		override fun generateManifest(ctx: Context): String {
			val forgeDeps = mutableListOf<ForgeDependency>()

			fun addDeps(container: NamedDomainObjectContainer<Dependency>, type: String) {
				container.manifestEntries().forEach {
					forgeDeps.add(
						ForgeDependency(
							modId = it.modid.get(),
							side = it.environment.orNull?.uppercase(Locale.getDefault()) ?: ctx.forgeSide,
							versionRange = it.forgeLikeVersionRange.get(),
							mandatory = type == "required",
							type = type
						)
					)
				}
			}

			addDeps(ctx.extension.dependencies.required, "required")
			addDeps(ctx.extension.dependencies.optional, "optional")
			addDeps(ctx.extension.dependencies.incompatible, "incompatible")

			val manifest = ForgeManifest(
				license = ctx.licenseName,
				issueTrackerURL = ctx.issuesUrl,
				mods = listOf(
					ForgeMod(
						modId = ctx.modId,
						displayName = ctx.modName,
						version = ctx.baseVersion,
						displayURL = ctx.homepageUrl,
						modUrl = ctx.homepageUrl,
						updateJSONURL = ctx.modrinthId.takeIf { it.isNotEmpty() }?.let {
							"https://api.modrinth.com/updates/$it/forge_updates.json?$updateCheckLoader=only"
						},
						logoFile = ctx.icon,
						authors = ctx.authors.joinToString(", "),
						credits = ctx.contributors.joinToString(", "),
						description = ctx.description,
						displayTest = if (supportsDisplayTest) ctx.forgeDisplayTest else null
					)
				),
				dependencies = mapOf(ctx.modId to forgeDeps),
				mixins = listOf(ForgeMixin("${ctx.modId}.mixins.json")),
				accessTransformers = ctx.accessTransformerPath?.let { listOf(ForgeAccessTransformer(it)) },
				features = javaVersionFeature(ctx)
					?.let { mapOf(ctx.modId to mapOf(it to "[${ctx.javaVersion.majorVersion},)")) },
				modproperties = mapOf(ctx.modId to mapOf("catalogueImageIcon" to ctx.icon))
			)

			return TOML.encodeToString(manifest)
		}
	}

	object NeoForge : ForgeLike("neoforge") {
		override val modManifestPath = "META-INF/neoforge.mods.toml"
		override val excludedResources = (super.excludedResources + "META-INF/mods.toml") + "pack.mcmeta"
		override val supportsDisplayTest = false
	}

	object Forge : ForgeLike("forge") {
		override val modManifestPath = "META-INF/mods.toml"
		override val excludedResources = super.excludedResources + "META-INF/neoforge.mods.toml"
		val mixinConfigAttribute = "MixinConfigs"

		/**
		 * MinecraftForge registered `java_version` from 41.0.16 (1.19) and renamed it to
		 * `javaVersion` in 47.0.17 (1.20.1, 2023-06-22). 1.20 never got the rename: that line
		 * ended at 46.0.14 eleven days earlier, so no Forge build for 1.20 knows the new name.
		 *
		 * The gate reads the range's lower bound rather than the compiled version, because a jar
		 * advertising 1.20-1.20.4 is loaded by 46.x, where `javaVersion` is a hard rejection.
		 *
		 * Below 1.20.1 the table is omitted rather than emitted as `java_version`: a range
		 * straddling the rename cannot name both, and where the old name does apply (1.19-1.20) it
		 * was registered `DependencySide.SERVER`, so the check passes trivially on clients and
		 * buys too little to be worth the risk. The residual gap is 1.20.1 on Forge 47.0.0-47.0.16,
		 * three weeks of June 2023 builds that were never the recommended one.
		 */
		override fun javaVersionFeature(ctx: Context): String? =
			"javaVersion".takeIf { ctx.stonecutter.eval(ctx.minecraftRangeLower, ">=1.20.1") }
	}

	companion object {
		fun of(id: String): Loader = when (id) {
			"fabric" -> Fabric
			"neoforge" -> NeoForge
			"forge" -> Forge
			else -> error("Unknown loader: '$id'")
		}
	}
}

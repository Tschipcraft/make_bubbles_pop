@file:Suppress("unused", "DuplicatedCode")

import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.extensions.stdlib.toDefaultLowerCase
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.util.Properties
import javax.inject.Inject

val Project.sc: StonecutterBuildExtension
	get() = extensions.getByType<StonecutterBuildExtension>()

@OptIn(StonecutterExperimentalAPI::class)
fun Project.prop(name: String): String = (project.sc.properties.get<String>(name))

/** Like [prop], but null when the key is absent or blank - for keys only set on some targets. */
@OptIn(StonecutterExperimentalAPI::class)
fun Project.propOrNull(name: String): String? =
	runCatching { project.sc.properties.get<String>(name) }.getOrNull()?.takeIf { it.isNotBlank() }

/** Like [propOrNull], but substituting [fallback] instead of returning null. */
fun Project.propOr(name: String, fallback: String): String = propOrNull(name) ?: fallback

// Minecraft compatibility range. `deps.minecraft` is what the target compiles against; the
// optional `deps.minecraft_range_lower` / `_upper` pair widens what the jar advertises, a blank
// upper bound meaning open-ended. The Fabric range, the Forge range and the jar slug all derive
// from this one pair, so they cannot disagree.

/** Lowest advertised version. Gate on this where an unknown key is *rejected* (`[features]`). */
fun Project.minecraftRangeLower(): String = propOr("deps.minecraft_range_lower", prop("deps.minecraft"))

/**
 * Highest advertised version, blank when open-ended. Gate on this where an unknown key is merely
 * *ignored* - see [Context.reachesMinecraft].
 */
fun Project.minecraftRangeUpper(): String = propOr("deps.minecraft_range_upper", "")

/** Semver-style range consumed by Fabric's dependency resolver, e.g. `>=1.21.5 <=1.21.11`. */
fun Project.minecraftFabricRange(): String {
	val lower = minecraftRangeLower()
	val upper = minecraftRangeUpper()
	return if (upper.isNotBlank()) ">=$lower <=$upper" else ">=$lower"
}

/**
 * Maven-style range for Forge / NeoForge, e.g. `[1.21.5,1.21.11]`. Coinciding bounds collapse to
 * `[1.21.5]`: maven-artifact before 3.8.8 rejects `[x,x]` and FML makes that a hard load error.
 * Every targeted MinecraftForge ships 3.6.3 or 3.8.5; NeoForge (3.9.x) accepts either form.
 */
fun Project.minecraftForgeRange(): String {
	val lower = minecraftRangeLower()
	val upper = minecraftRangeUpper()
	return when {
		upper.isBlank() -> "[$lower,)"
		upper == lower -> "[$lower]"
		else -> "[$lower,$upper]"
	}
}

/** Filename segment for the range: `mc1.21.5-1.21.11`, `mc1.21.5+` (open-ended) or `mc26.1.2`. */
fun Project.minecraftRangeSlug(): String {
	val lower = minecraftRangeLower()
	val upper = minecraftRangeUpper()
	return when {
		upper.isBlank() -> "mc$lower+"
		upper == lower -> "mc$lower"
		else -> "mc$lower-$upper"
	}
}

fun Project.env(variable: String): String? {
	providers.environmentVariable(variable).orNull?.let { return it }
	return rootProject.file(".env").takeIf { it.exists() }?.let { f ->
		Properties().apply { f.inputStream().use(::load) }.getProperty(variable)
	}
}

fun Project.envTrue(variable: String): Boolean = env(variable)?.toDefaultLowerCase() == "true"

/**
 * Inserts `"refmap"` after the opening brace of a mixin config. Legacy Forge runs on SRG names, so
 * Mixin needs the refmap to resolve the Mojang names in the annotations; without it every
 * `@Accessor` throws and, under a permissive `defaultRequire`, every injector silently misses.
 * ModDevGradle builds the refmap but never writes the key, and the config is shared with NeoForge
 * and Loom, which must not carry it - so it is added here, on the Forge path only.
 */
private fun FileCopyDetails.declareRefmap(refmapName: String) {
	var declared = false
	filter(Transformer<String?, String> { line ->
		if (declared || line.trim() != "{") line
		else {
			declared = true
			"$line\n  \"refmap\": \"$refmapName\","
		}
	})
}

fun RepositoryHandler.strictMaven(
	url: String, vararg groups: String, configure: MavenArtifactRepository.() -> Unit = {}
) = exclusiveContent {
	forRepository { maven(url) { configure() } }
	filter { groups.forEach(::includeGroup) }
}

abstract class GenerateModManifestTask : DefaultTask() {
	@get:Input
	abstract val content: Property<String>

	@get:OutputFile
	abstract val outputFile: RegularFileProperty

	@TaskAction
	fun generate() {
		val file = outputFile.get().asFile
		file.parentFile.mkdirs()
		file.writeText(content.get())
	}
}

abstract class ModPlatformPlugin @Inject constructor() : Plugin<Project> {
	override fun apply(project: Project) = with(project) {
		val inferredLoader = Loader.of(project.buildFile.name.substringAfter('.').replace(".gradle.kts", ""))

		val extension = extensions.create("platform", ModPlatformExtension::class.java).apply {
			loader.convention(inferredLoader.id)
		}

		when (inferredLoader) {
			is Loader.Fabric -> {
				extension.jarTask.convention(providers.provider {
					extensions.getByType<dev.kikugie.loomx.LoomCompatProjectExtension>().modJar.name
				})
				extension.sourcesJarTask.convention(providers.provider {
					extensions.getByType<dev.kikugie.loomx.LoomCompatProjectExtension>().modSourcesJar.name
				})
			}

			is Loader.Forge -> {
				extension.jarTask.convention("reobfJar")
				extension.sourcesJarTask.convention("sourcesJar")
			}

			else -> {
				extension.jarTask.convention("jar")
				extension.sourcesJarTask.convention("sourcesJar")
			}
		}

		afterEvaluate {
			val ctx = Context(
				project = this,
				extension = extension,
				loader = Loader.of(extension.loader.get()),
				stonecutter = project.sc
			)
			configureProject(ctx)
		}
	}

	private fun Project.configureProject(ctx: Context) {
		listOf("java", "me.modmuss50.mod-publish-plugin", "idea").forEach { apply(plugin = it) }

		version = ctx.fullVersion
		ctx.extension.requiredJava.set(ctx.javaVersion)

		if (ctx.loader.isFabricLike) {
			ctx.extension.dependencies {
				required("java") { fabricLikeVersionRange = ">=${ctx.javaVersion.majorVersion}" }
			}
		}

		registerGenerateManifestTask(ctx)
		configureJarTask(ctx)
		configureIdea()
		configureProcessResources(ctx)
		configureJava(ctx)
		registerBuildAndCollectTask(ctx)

		configureModPublishing(ctx)

		if (envTrue("PUB_MAVEN_ENABLE")) {
			configureMavenPublishing(ctx)
		}
	}

	private fun Project.configureJava(ctx: Context) {
		extensions.configure<JavaPluginExtension>("java") {
			withSourcesJar()
			withJavadocJar()
			sourceCompatibility = ctx.javaVersion
			targetCompatibility = ctx.javaVersion
		}
	}

	private fun Project.registerGenerateManifestTask(ctx: Context) {
		val manifestOutputDir = layout.buildDirectory.dir("generated/modManifest")
		val generateTask = tasks.register<GenerateModManifestTask>("generateModManifest") {
			content.set(ctx.loader.generateManifest(ctx))
			outputFile.set(layout.buildDirectory.file("generated/modManifest/${ctx.loader.modManifestPath}"))
		}

		the<JavaPluginExtension>().sourceSets.named("main") { resources.srcDir(manifestOutputDir) }
		tasks.named<ProcessResources>("processResources") { dependsOn(generateTask) }
	}

	private fun Project.configureProcessResources(ctx: Context) {
		tasks.named<ProcessResources>("processResources") {
			dependsOn(tasks.named("stonecutterGenerate"))
			filesMatching("*.mixins.json") {
				expand("java" to "JAVA_${ctx.javaVersion.majorVersion}")
				if (ctx.loader is Loader.Forge) declareRefmap("${ctx.modId}.mixins.refmap.json")
			}
			exclude(ctx.loader.excludedResources)

			// A config that slipped past the anchor builds green and fails in-game, so refuse it here.
			if (ctx.loader is Loader.Forge) {
				val processed = destinationDir
				doLast {
					processed.walkTopDown().filter { it.name.endsWith(".mixins.json") }.forEach {
						check(it.readText().contains("\"refmap\"")) {
							"${it.name} kept no refmap declaration; declareRefmap expects the " +
								"config to open with a line holding nothing but `{`."
						}
					}
				}
			}
		}
	}

	private fun Project.configureJarTask(ctx: Context) {
		val generateTask = tasks.named("generateModManifest")
		// Bundle the root LICENSE in every jar, sources and javadoc included. Optional, like the
		// access widener.
		val license = rootProject.file("LICENSE").takeIf { it.exists() }
		tasks.withType<Jar>().configureEach {
			archiveBaseName.set(ctx.modId)
			dependsOn(generateTask)
			license?.let { from(it) { duplicatesStrategy = DuplicatesStrategy.EXCLUDE } }
			if (ctx.loader is Loader.Forge) {
				manifest.attributes(ctx.loader.mixinConfigAttribute to "${ctx.modId}.mixins.json")
			}
		}
	}

	private fun Project.configureIdea() {
		extensions.configure<IdeaModel>("idea") {
			module {
				isDownloadJavadoc = true
				isDownloadSources = true
			}
		}
	}

	private fun Project.registerBuildAndCollectTask(ctx: Context) {
		tasks.register<Copy>("buildAndCollect") {
			from(
				tasks.named(ctx.extension.jarTask.get()),
				tasks.named(ctx.extension.sourcesJarTask.get()),
				tasks.named("javadocJar")
			)
			into(rootProject.layout.buildDirectory.file("libs/${ctx.basicVersion}"))
			dependsOn("build")
			group = "build"
		}
	}
}

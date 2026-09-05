@file:Suppress("unused")

import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import javax.inject.Inject

abstract class ModPlatformExtension {

	abstract val requiredJava: Property<JavaVersion>
	abstract val loader: Property<String>
	abstract val jarTask: Property<String>
	abstract val sourcesJarTask: Property<String>

	/**
	 * Fabric entrypoints for the generated `fabric.mod.json`, declared with [entrypoint].
	 * No default - see [Context.entrypoints].
	 */
	abstract val entrypoints: MapProperty<String, List<String>>

	@get:Nested
	abstract val dependencies: DependenciesConfig

	init {
		requiredJava.convention(JavaVersion.VERSION_21)
		entrypoints.convention(emptyMap())
	}

	fun dependencies(action: Action<DependenciesConfig>) {
		action.execute(dependencies)
	}

	fun entrypoint(name: String, vararg classes: String) {
		entrypoints.put(name, classes.toList())
	}
}

abstract class DependenciesConfig @Inject constructor(val objects: ObjectFactory) {

	private fun container() = objects.domainObjectContainer(Dependency::class.java)

	val required: NamedDomainObjectContainer<Dependency> = container()
	val optional: NamedDomainObjectContainer<Dependency> = container()
	val incompatible: NamedDomainObjectContainer<Dependency> = container()
	val embeds: NamedDomainObjectContainer<Dependency> = container()

	fun required(modid: String, action: Action<Dependency>): Dependency? = required.create(modid, action)
	fun optional(modid: String, action: Action<Dependency>): Dependency? = optional.create(modid, action)
	fun incompatible(modid: String, action: Action<Dependency>): Dependency? = incompatible.create(modid, action)
	fun embeds(modid: String, action: Action<Dependency>): Dependency? = embeds.create(modid, action)
}

abstract class Dependency @Inject constructor(val name: String) {

	abstract val modid: Property<String>
	abstract val modrinth: Property<String>
	abstract val curseforge: Property<String>
	abstract val fabricLikeVersionRange: Property<String>
	abstract val forgeLikeVersionRange: Property<String>
	abstract val environment: Property<String>

	/**
	 * Whether the dependency is written into the mod manifest. Stores treat "incompatible" as an
	 * advisory tag; in a manifest it refuses to launch. Set false to advertise on the stores only.
	 */
	abstract val declareInManifest: Property<Boolean>

	init {
		modid.convention(name)
		fabricLikeVersionRange.convention("*")
		forgeLikeVersionRange.convention("(,]")
		declareInManifest.convention(true)
		// `environment` deliberately has no convention: null means "unset", so Loader can fall back
		// to `mod.environment` (Context.forgeSide) instead of BOTH. Only the Forge-like manifests
		// read it; fabric.mod.json has no per-dependency side.
	}

	fun slug(slug: String) {
		modrinth.set(slug)
		curseforge.set(slug)
	}

	fun slug(modrinthSlug: String? = null, curseforgeSlug: String? = null) {
		if (modrinthSlug != null) {
			modrinth.set(modrinthSlug)
		}
		if (curseforgeSlug != null) {
			curseforge.set(curseforgeSlug)
		}
	}
}

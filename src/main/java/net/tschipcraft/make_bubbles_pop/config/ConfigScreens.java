package net.tschipcraft.make_bubbles_pop.config;

import net.minecraft.client.gui.screens.Screen;

/**
 * Single entry point for building the config screen.
 *
 * <p>Exactly one screen implementation is compiled into any given target, picked by the
 * {@code yacl} / {@code cloth} Stonecutter constants (see {@code stonecutter.gradle.kts}).
 * Callers never reference {@code YACLScreen} or {@code ClothScreen} directly, so the platform
 * entrypoints stay free of any library-specific branching.
 */
public final class ConfigScreens {

	private ConfigScreens() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * The mod id of the config library this build expects, for runtime presence checks.
	 *
	 * <p>Cloth Config ships under {@code cloth-config} on Fabric but {@code cloth_config} on Forge,
	 * so the id depends on the loader as well as the library. The {@code yacl} branch covers
	 * Fabric 1.20.1+ and NeoForge, which leaves the Fabric branch to Cloth-on-Fabric and the Forge
	 * branch to Cloth-on-Forge.
	 */
	//? yacl {
	public static final String LIBRARY_MOD_ID = "yet_another_config_lib_v3";
	//?} fabric {
	/*public static final String LIBRARY_MOD_ID = "cloth-config";
	*///?} forge {
	/*public static final String LIBRARY_MOD_ID = "cloth_config";
	*///?}

	/** Display name of that library, substituted into the "config library missing" prompt. */
	//? yacl {
	public static final String LIBRARY_NAME = "Yet Another Config Lib";
	//?} cloth {
	/*public static final String LIBRARY_NAME = "Cloth Config";
	*///?}

	/** Modrinth page used in the "config library missing" prompt. */
	//? yacl {
	public static final String LIBRARY_URL = "https://modrinth.com/mod/yacl/versions";
	//?} cloth {
	/*public static final String LIBRARY_URL = "https://modrinth.com/mod/cloth-config/versions";
	*///?}

	public static Screen create(Screen parent, PlatformConfig config) {
		//? yacl {
		return YACLScreen.create(parent, config);
		//?} cloth {
		/*return ClothScreen.create(parent, config);
		*///?}
	}

}

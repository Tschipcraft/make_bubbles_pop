package net.tschipcraft.make_bubbles_pop.config;

/**
 * Bridge between a config screen and whatever storage the current platform uses.
 *
 * <p>Screens never touch storage directly: they read and write the static fields on
 * {@code MakeBubblesPopConfig} and call {@link #load()} / {@link #save()} around that. This keeps
 * the YACL and Cloth screens identical on every loader, and keeps Fabric's JSON file and the
 * Forge/NeoForge native config specs out of the shared code entirely.
 */
public interface PlatformConfig {

	void load();

	void save();

}

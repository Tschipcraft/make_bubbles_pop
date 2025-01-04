package net.tschipcraft.make_bubbles_pop.config;

/**
 * Interface used for passing platform-specific config classes to ConfigScreen.getScreen().
 */
public interface PlatformConfig {

    void load();

    void save();

}

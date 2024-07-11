package net.tschipcraft.make_bubbles_pop;

import eu.midnightdust.lib.config.MidnightConfig;

public class MakeBubblesPopConfig extends MidnightConfig {

    @Entry
    public static boolean POP_PARTICLE_ENABLED = true;

    // Show this entry with percentage if possible
    @Entry(min = 0F, max = 1F)
    public static float BUBBLE_POP_VOLUME = 0.1F;

    @Entry
    public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = false;


    @Comment
    public static Comment empty;

    @Comment
    public static Comment additional_features;

    @Entry
    public static boolean BUBBLE_PHYSICS_ENABLED = true;

    @Entry(min = 1D, max = 100D)
    public static double BUBBLE_LIFETIME_MULTIPLIER = 32D;

    @Entry
    public static boolean BIOME_COLORS_ENABLED = true;

    // Show this entry with percentage if possible
    @Entry(min = 0F, max = 1F)
    public static float BIOME_COLOR_INTENSITY = 0.65F;


    @Entry
    public static boolean CHEST_BUBBLES_ENABLED = true;

    @Entry
    public static boolean BARREL_BUBBLES_ENABLED = true;

    @Entry
    public static boolean CONTAINER_SOUND_ENABLED = true;

    @Entry
    public static boolean EXPLOSION_BUBBLES_ENABLED = true;

}

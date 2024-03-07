package net.tschipcraft.make_bubbles_pop;

import eu.midnightdust.lib.config.MidnightConfig;

public class MakeBubblesPopConfig extends MidnightConfig {

    @Entry
    public static boolean BUBBLE_PHYSICS_ENABLED = true;

    @Entry
    public static boolean CHEST_BUBBLES_ENABLED = true;

    @Entry
    public static boolean BARREL_BUBBLES_ENABLED = true;

    @Entry
    public static boolean EXPLOSION_BUBBLES_ENABLED = true;

    @Entry(isSlider = true, min = 1.0D, max = 100.0D, precision = 2)
    public static double BUBBLE_LIFETIME_MULTIPLIER = 32.0D;

    @Entry
    public static boolean POP_PARTICLE_ENABLED = true;

    @Entry(isSlider = true, min = 0F, max = 1F, precision = 100)
    public static float BUBBLE_POP_VOLUME = 0.1F;

    @Entry
    public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = true;

    @Entry
    public static boolean BIOME_COLORS_ENABLED = true;

    @Entry(isSlider = true, min = 0F, max = 1F, precision = 100)
    public static float BIOME_COLOR_INTENSITY = 0.35F;

    public static boolean BUBBLE_BEHAVIOR_FOR_SOUL_SAND_BUBBLES = true;

}

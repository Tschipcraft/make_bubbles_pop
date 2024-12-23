package net.tschipcraft.make_bubbles_pop;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MakeBubblesPop.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MakeBubblesPopConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue POP_PARTICLE_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.POP_PARTICLE_ENABLED")
            .comment("Default: true")
            .define("POP_PARTICLE_ENABLED", true);

    private static final ForgeConfigSpec.DoubleValue BUBBLE_POP_VOLUME_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BUBBLE_POP_VOLUME")
            .comment("Default: §b10%§f")
            .defineInRange("BUBBLE_POP_VOLUME", 0.1D, 0D, 1D);

    private static final ForgeConfigSpec.BooleanValue POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.POPPED_BUBBLES_MAINTAIN_VELOCITY")
            .comment("If enabled, popped bubbles will maintain their velocity. Default: false")
            .define("POPPED_BUBBLES_MAINTAIN_VELOCITY", false);

    private static final ForgeConfigSpec.BooleanValue BUBBLE_PHYSICS_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BUBBLE_PHYSICS_ENABLED")
            .comment("If enabled, bubbles will be affected by physics when colliding with blocks or entities. Default: true")
            .define("BUBBLE_PHYSICS_ENABLED", true);

    private static final ForgeConfigSpec.DoubleValue BUBBLE_LIFETIME_MULTIPLIER_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BUBBLE_LIFETIME_MULTIPLIER")
            .comment("The time it takes for a bubble to pop underwater. \nBy default, this is set longer than in vanilla to allow bubbles to reach the water surface. \nDefault: §b32.0§f §7(Vanilla: 8.0)§f")
            .defineInRange("BUBBLE_LIFETIME_MULTIPLIER", 32D, 1D, 100D);

    private static final ForgeConfigSpec.BooleanValue BIOME_COLORS_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BIOME_COLORS_ENABLED")
            .comment("If enabled, bubbles will have a color based on the biome they are in. Default: true")
            .define("BIOME_COLORS_ENABLED", true);

    private static final ForgeConfigSpec.DoubleValue BIOME_COLOR_INTENSITY_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BIOME_COLOR_INTENSITY")
            .comment("Default: §b65%§f")
            .defineInRange("BIOME_COLOR_INTENSITY", 0.65D, 0D, 1D);

    private static final ForgeConfigSpec.BooleanValue CHEST_BUBBLES_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.CHEST_BUBBLES_ENABLED")
            .comment("If enabled, bubbles will appear when opening chests underwater. Default: true")
            .define("CHEST_BUBBLES_ENABLED", true);

    private static final ForgeConfigSpec.BooleanValue BARREL_BUBBLES_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BARREL_BUBBLES_ENABLED")
            .comment("If enabled, bubbles will appear when opening barrels underwater. Default: true")
            .define("BARREL_BUBBLES_ENABLED", true);

    private static final ForgeConfigSpec.BooleanValue CONTAINER_SOUND_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.CONTAINER_SOUND_ENABLED")
            .comment("If enabled, a sound will play when opening chests or barrels underwater. Default: true")
            .define("CONTAINER_SOUND_ENABLED", true);

    private static final ForgeConfigSpec.BooleanValue EXPLOSION_BUBBLES_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.EXPLOSION_BUBBLES_ENABLED")
            .comment("If enabled, bubbles will appear when an explosion occurs underwater. Default: true")
            .define("EXPLOSION_BUBBLES_ENABLED", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();


    // Config values
    public static boolean POP_PARTICLE_ENABLED;
    public static float BUBBLE_POP_VOLUME;
    public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY;

    public static boolean BUBBLE_PHYSICS_ENABLED;
    public static double BUBBLE_LIFETIME_MULTIPLIER;
    public static boolean BIOME_COLORS_ENABLED;
    public static float BIOME_COLOR_INTENSITY;

    public static boolean CHEST_BUBBLES_ENABLED;
    public static boolean BARREL_BUBBLES_ENABLED;
    public static boolean CONTAINER_SOUND_ENABLED;
    public static boolean EXPLOSION_BUBBLES_ENABLED;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        POP_PARTICLE_ENABLED = POP_PARTICLE_ENABLED_CONF.get();
        BUBBLE_POP_VOLUME = BUBBLE_POP_VOLUME_CONF.get().floatValue();
        POPPED_BUBBLES_MAINTAIN_VELOCITY = POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF.get();
        BUBBLE_PHYSICS_ENABLED = BUBBLE_PHYSICS_ENABLED_CONF.get();
        BUBBLE_LIFETIME_MULTIPLIER = BUBBLE_LIFETIME_MULTIPLIER_CONF.get();
        BIOME_COLORS_ENABLED = BIOME_COLORS_ENABLED_CONF.get();
        BIOME_COLOR_INTENSITY = BIOME_COLOR_INTENSITY_CONF.get().floatValue();
        CHEST_BUBBLES_ENABLED = CHEST_BUBBLES_ENABLED_CONF.get();
        BARREL_BUBBLES_ENABLED = BARREL_BUBBLES_ENABLED_CONF.get();
        CONTAINER_SOUND_ENABLED = CONTAINER_SOUND_ENABLED_CONF.get();
        EXPLOSION_BUBBLES_ENABLED = EXPLOSION_BUBBLES_ENABLED_CONF.get();
    }

}

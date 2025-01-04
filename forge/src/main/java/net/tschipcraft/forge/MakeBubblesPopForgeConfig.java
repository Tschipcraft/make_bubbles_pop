package net.tschipcraft.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

@Mod.EventBusSubscriber(modid = MakeBubblesPop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MakeBubblesPopForgeConfig extends MakeBubblesPopConfig {

    private MakeBubblesPopForgeConfig() {
        throw new IllegalStateException("Config class cannot be instantiated");
    }

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue POP_PARTICLE_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.POP_PARTICLE_ENABLED")
            .comment("Default: true")
            .define("POP_PARTICLE_ENABLED", true);

    private static final ForgeConfigSpec.IntValue BUBBLE_POP_VOLUME_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BUBBLE_POP_VOLUME")
            .comment("Default: 10")
            .defineInRange("BUBBLE_POP_VOLUME", 10, 0, 100);

    private static final ForgeConfigSpec.BooleanValue POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.POPPED_BUBBLES_MAINTAIN_VELOCITY")
            .comment("If enabled, popped bubbles will maintain their velocity. Default: false")
            .define("POPPED_BUBBLES_MAINTAIN_VELOCITY", false);

    private static final ForgeConfigSpec.BooleanValue BUBBLE_PHYSICS_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BUBBLE_PHYSICS_ENABLED")
            .comment("If enabled, bubbles will be affected by physics when colliding with blocks or entities. Default: true")
            .define("BUBBLE_PHYSICS_ENABLED", true);

    private static final ForgeConfigSpec.IntValue BUBBLE_LIFETIME_MULTIPLIER_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BUBBLE_LIFETIME_MULTIPLIER")
            .comment("The time it takes for a bubble to pop underwater. By default, this is set longer than in vanilla to allow bubbles to reach the water surface. Default: 32 (Vanilla: 8)")
            .defineInRange("BUBBLE_LIFETIME_MULTIPLIER", 32, 1, 100);

    private static final ForgeConfigSpec.BooleanValue BIOME_COLORS_ENABLED_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BIOME_COLORS_ENABLED")
            .comment("If enabled, bubbles will have a color based on the biome they are in. Default: true")
            .define("BIOME_COLORS_ENABLED", true);

    private static final ForgeConfigSpec.IntValue BIOME_COLOR_INTENSITY_CONF = BUILDER
            .translation("make_bubbles_pop.midnightconfig.BIOME_COLOR_INTENSITY")
            .comment("Default: 65")
            .defineInRange("BIOME_COLOR_INTENSITY", 65, 0, 100);

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

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        MakeBubblesPopConfig.POP_PARTICLE_ENABLED = POP_PARTICLE_ENABLED_CONF.get();
        MakeBubblesPopConfig.BUBBLE_POP_VOLUME = BUBBLE_POP_VOLUME_CONF.get() / 100.0F;
        MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF.get();
        MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = BUBBLE_PHYSICS_ENABLED_CONF.get();
        MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = BUBBLE_LIFETIME_MULTIPLIER_CONF.get().doubleValue();
        MakeBubblesPopConfig.BIOME_COLORS_ENABLED = BIOME_COLORS_ENABLED_CONF.get();
        MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = BIOME_COLOR_INTENSITY_CONF.get() / 100.0F;
        MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = CHEST_BUBBLES_ENABLED_CONF.get();
        MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = BARREL_BUBBLES_ENABLED_CONF.get();
        MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = CONTAINER_SOUND_ENABLED_CONF.get();
        MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = EXPLOSION_BUBBLES_ENABLED_CONF.get();
    }
}

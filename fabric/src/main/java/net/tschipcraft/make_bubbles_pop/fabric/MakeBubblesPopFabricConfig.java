package net.tschipcraft.make_bubbles_pop.fabric;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.config.PlatformConfig;

import java.nio.file.Path;
import com.google.gson.GsonBuilder;

public class MakeBubblesPopFabricConfig implements PlatformConfig {

    // Use the ConfigClassHandler from YACL to handle the config file
    public static final ConfigClassHandler<MakeBubblesPopFabricConfig> HANDLER = ConfigClassHandler.createBuilder(MakeBubblesPopFabricConfig.class)
            .id(YACLPlatform.rl(MakeBubblesPop.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("make_bubbles_pop.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // not needed, pretty print by default
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(value = "POP_PARTICLE_ENABLED", comment = "Default: true")
    private static boolean POP_PARTICLE_ENABLED_CONF = true;
    @SerialEntry(value = "BUBBLE_POP_VOLUME", comment = "Default: 0.1 (10%)")
    public static float BUBBLE_POP_VOLUME_CONF = 0.1F;
    @SerialEntry(value = "POPPED_BUBBLES_MAINTAIN_VELOCITY", comment = "If enabled, popped bubbles will maintain their velocity. Default: false")
    public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF = false;

    @SerialEntry(value = "BUBBLE_PHYSICS_ENABLED", comment = "If enabled, bubbles will be affected by physics when colliding with blocks or entities. Default: true")
    public static boolean BUBBLE_PHYSICS_ENABLED_CONF = true;
    @SerialEntry(value = "BUBBLE_LIFETIME_MULTIPLIER", comment = "The time it takes for a bubble to pop underwater. By default, this is set longer than in vanilla to allow bubbles to reach the water surface. Default: 32 (Vanilla: 8)")
    public static double BUBBLE_LIFETIME_MULTIPLIER_CONF = 32D;
    @SerialEntry(value = "BIOME_COLORS_ENABLED", comment = "If enabled, bubbles will have a color based on the biome they are in. Default: true")
    public static boolean BIOME_COLORS_ENABLED_CONF = true;
    @SerialEntry(value = "BIOME_COLOR_INTENSITY", comment = "Default: 0.65 (65%)")
    public static float BIOME_COLOR_INTENSITY_CONF = 0.65F;
    @SerialEntry(value = "CHEST_BUBBLES_ENABLED", comment = "If enabled, bubbles will appear when opening chests underwater. Default: true")
    public static boolean CHEST_BUBBLES_ENABLED_CONF = true;
    @SerialEntry(value = "BARREL_BUBBLES_ENABLED", comment = "If enabled, bubbles will appear when opening barrels underwater. Default: true")
    public static boolean BARREL_BUBBLES_ENABLED_CONF = true;
    @SerialEntry(value = "CONTAINER_SOUND_ENABLED", comment = "If enabled, bubbles will appear when opening containers underwater. Default: true")
    public static boolean CONTAINER_SOUND_ENABLED_CONF = true;
    @SerialEntry(value = "EXPLOSION_BUBBLES_ENABLED", comment = "If enabled, bubbles will appear when explosions occur underwater. Default: true")
    public static boolean EXPLOSION_BUBBLES_ENABLED_CONF = true;

    public void save() {
        POP_PARTICLE_ENABLED_CONF = MakeBubblesPopConfig.POP_PARTICLE_ENABLED;
        BUBBLE_POP_VOLUME_CONF = MakeBubblesPopConfig.BUBBLE_POP_VOLUME;
        POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF = MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY;
        BUBBLE_PHYSICS_ENABLED_CONF = MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED;
        BUBBLE_LIFETIME_MULTIPLIER_CONF = MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER;
        BIOME_COLORS_ENABLED_CONF = MakeBubblesPopConfig.BIOME_COLORS_ENABLED;
        BIOME_COLOR_INTENSITY_CONF = MakeBubblesPopConfig.BIOME_COLOR_INTENSITY;
        CHEST_BUBBLES_ENABLED_CONF = MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED;
        BARREL_BUBBLES_ENABLED_CONF = MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED;
        CONTAINER_SOUND_ENABLED_CONF = MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED;
        EXPLOSION_BUBBLES_ENABLED_CONF = MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED;
        HANDLER.save();
    }

    public void load() {
        HANDLER.load();
        MakeBubblesPopConfig.POP_PARTICLE_ENABLED = POP_PARTICLE_ENABLED_CONF;
        MakeBubblesPopConfig.BUBBLE_POP_VOLUME = BUBBLE_POP_VOLUME_CONF;
        MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = POPPED_BUBBLES_MAINTAIN_VELOCITY_CONF;
        MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = BUBBLE_PHYSICS_ENABLED_CONF;
        MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = BUBBLE_LIFETIME_MULTIPLIER_CONF;
        MakeBubblesPopConfig.BIOME_COLORS_ENABLED = BIOME_COLORS_ENABLED_CONF;
        MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = BIOME_COLOR_INTENSITY_CONF;
        MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = CHEST_BUBBLES_ENABLED_CONF;
        MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = BARREL_BUBBLES_ENABLED_CONF;
        MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = CONTAINER_SOUND_ENABLED_CONF;
        MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = EXPLOSION_BUBBLES_ENABLED_CONF;
    }

    public Path getLegacyConfigFilePath() {
        return YACLPlatform.getConfigDir().resolve("make_bubbles_pop.json");
    }

}

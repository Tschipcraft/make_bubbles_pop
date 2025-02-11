package net.tschipcraft.make_bubbles_pop.config;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

public class YACLScreen {

    /**
     * Create the config screen for Make Bubbles Pop using Yet Another Config Lib.
     *
     * @param parent The parent screen
     * @param config The config handler (Can be either a new PlatformConfig or a direct MakeBubblesPopConfig implementing PlatformConfig)
     * @return The config screen
     */
    public static Screen getScreen(Screen parent, PlatformConfig config) {
        // Reload config from file before opening the screen
        config.load();

        // Create options
        var POP_PARTICLE_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.POP_PARTICLE_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.POP_PARTICLE_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.POP_PARTICLE_ENABLED, newVal -> MakeBubblesPopConfig.POP_PARTICLE_ENABLED = newVal)
                .build();

        var BUBBLE_POP_VOLUME = Option.<Float>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.BUBBLE_POP_VOLUME"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.BUBBLE_POP_VOLUME.tooltip")))
                .customController(opt -> FloatSliderControllerBuilder.create(opt).range(0F, 1.0F).step(0.01F).formatValue(aFloat -> {
                    // Format as percentage
                    return Text.of(String.format("%.0f%%", aFloat * 100));
                }).build())
                .binding(0.1F, () -> MakeBubblesPopConfig.BUBBLE_POP_VOLUME, newVal -> MakeBubblesPopConfig.BUBBLE_POP_VOLUME = newVal)
                .build();

        var POPPED_BUBBLES_MAINTAIN_VELOCITY = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.POPPED_BUBBLES_MAINTAIN_VELOCITY"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.POPPED_BUBBLES_MAINTAIN_VELOCITY.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(false, () -> MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY, newVal -> MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = newVal)
                .build();

        var BUBBLE_PHYSICS_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.BUBBLE_PHYSICS_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.BUBBLE_PHYSICS_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED, newVal -> MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = newVal)
                .build();

        var BUBBLE_LIFETIME_MULTIPLIER = Option.<Double>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.BUBBLE_LIFETIME_MULTIPLIER"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.BUBBLE_LIFETIME_MULTIPLIER.tooltip")))
                .customController(opt -> DoubleSliderControllerBuilder.create(opt).range(1D, 100D).step(0.25D).build())
                .binding(32D, () -> MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER, newVal -> MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = newVal)
                .build();

        var BIOME_COLORS_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.BIOME_COLORS_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.BIOME_COLORS_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.BIOME_COLORS_ENABLED, newVal -> MakeBubblesPopConfig.BIOME_COLORS_ENABLED = newVal)
                .build();

        var BIOME_COLOR_INTENSITY = Option.<Float>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.BIOME_COLOR_INTENSITY"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.BIOME_COLOR_INTENSITY.tooltip")))
                .customController(opt -> FloatSliderControllerBuilder.create(opt).range(0F, 1.0F).step(0.01F).formatValue(aFloat -> {
                    // Format as percentage
                    return Text.of(String.format("%.0f%%", aFloat * 100));
                }).build())
                .binding(0.65F, () -> MakeBubblesPopConfig.BIOME_COLOR_INTENSITY, newVal -> MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = newVal)
                .build();

        var CHEST_BUBBLES_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.CHEST_BUBBLES_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.CHEST_BUBBLES_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED, newVal -> MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = newVal)
                .build();

        var BARREL_BUBBLES_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.BARREL_BUBBLES_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.BARREL_BUBBLES_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED, newVal -> MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = newVal)
                .build();

        var CONTAINER_SOUND_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.CONTAINER_SOUND_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.CONTAINER_SOUND_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED, newVal -> MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = newVal)
                .build();

        var EXPLOSION_BUBBLES_ENABLED = Option.<Boolean>createBuilder()
                .name(Text.translatable("make_bubbles_pop.config.EXPLOSION_BUBBLES_ENABLED"))
                .description(OptionDescription.of(Text.translatable("make_bubbles_pop.config.EXPLOSION_BUBBLES_ENABLED.tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, () -> MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED, newVal -> MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = newVal)
                .build();

        // Build screen
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("make_bubbles_pop.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("make_bubbles_pop.config.title"))
                        // Add options
                        .option(POP_PARTICLE_ENABLED)
                        .option(BUBBLE_POP_VOLUME)
                        .option(POPPED_BUBBLES_MAINTAIN_VELOCITY)
                        .option(BUBBLE_PHYSICS_ENABLED)
                        .option(BUBBLE_LIFETIME_MULTIPLIER)
                        .option(BIOME_COLORS_ENABLED)
                        .option(BIOME_COLOR_INTENSITY)
                        .option(CHEST_BUBBLES_ENABLED)
                        .option(BARREL_BUBBLES_ENABLED)
                        .option(CONTAINER_SOUND_ENABLED)
                        .option(EXPLOSION_BUBBLES_ENABLED)
                        .build())
                .save(config::save) // Link save button to config save method
                .build()
        .generateScreen(parent);
    }

}

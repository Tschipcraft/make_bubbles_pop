package net.tschipcraft.make_bubbles_pop.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

public class ClothScreen {

    public static Screen getScreen(Screen parent, PlatformConfig config) {
        // Reload config from file before opening the screen
        config.load();

        // Create screen
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("make_bubbles_pop.config.title"))
                .setSavingRunnable(config::save); // Link save button to config save method

        // Create category
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("make_bubbles_pop.config.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Create options
        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.POP_PARTICLE_ENABLED"), MakeBubblesPopConfig.POP_PARTICLE_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.POP_PARTICLE_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.POP_PARTICLE_ENABLED = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(
                Text.translatable("make_bubbles_pop.config.BUBBLE_POP_VOLUME"), (int) (MakeBubblesPopConfig.BUBBLE_POP_VOLUME * 100), 0, 100)
                .setTooltip(Text.translatable("make_bubbles_pop.config.BUBBLE_POP_VOLUME.tooltip"))
                .setTextGetter(value -> Text.of(String.format("%d%%", value)))
                .setDefaultValue(10)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.BUBBLE_POP_VOLUME = newValue / 100.0F)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.POPPED_BUBBLES_MAINTAIN_VELOCITY"), MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY)
                .setTooltip(Text.translatable("make_bubbles_pop.config.POPPED_BUBBLES_MAINTAIN_VELOCITY.tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.BUBBLE_PHYSICS_ENABLED"), MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.BUBBLE_PHYSICS_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(
                Text.translatable("make_bubbles_pop.config.BUBBLE_LIFETIME_MULTIPLIER"), (int) MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER, 1, 100)
                .setTooltip(Text.translatable("make_bubbles_pop.config.BUBBLE_LIFETIME_MULTIPLIER.tooltip"))
                .setDefaultValue(32)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = newValue.doubleValue())
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.BIOME_COLORS_ENABLED"), MakeBubblesPopConfig.BIOME_COLORS_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.BIOME_COLORS_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.BIOME_COLORS_ENABLED = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(
                Text.translatable("make_bubbles_pop.config.BIOME_COLOR_INTENSITY"), (int) (MakeBubblesPopConfig.BIOME_COLOR_INTENSITY * 100), 0, 100)
                .setTooltip(Text.translatable("make_bubbles_pop.config.BIOME_COLOR_INTENSITY.tooltip"))
                .setTextGetter(value -> Text.of(String.format("%d%%", value)))
                .setDefaultValue(65)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = newValue / 100.0F)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.CHEST_BUBBLES_ENABLED"), MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.CHEST_BUBBLES_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.BARREL_BUBBLES_ENABLED"), MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.BARREL_BUBBLES_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.CONTAINER_SOUND_ENABLED"), MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.CONTAINER_SOUND_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("make_bubbles_pop.config.EXPLOSION_BUBBLES_ENABLED"), MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED)
                .setTooltip(Text.translatable("make_bubbles_pop.config.EXPLOSION_BUBBLES_ENABLED.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = newValue)
                .build());

        // Build screen
        return builder.build();
    }

}

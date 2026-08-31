package net.tschipcraft.make_bubbles_pop.config;

// Config screen built with Cloth Config. Compiled on Fabric below 1.20.1 (where YACL 3 has no
// builds) and on Forge. Only line comments are used in this file: when the `cloth` constant is off
// Stonecutter wraps the whole body in a block comment, which cannot nest another one.
//? cloth {
/*import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.Texts;

public final class ClothScreen {

	private ClothScreen() {
		throw new IllegalStateException("Utility class");
	}

	private static Component title(String key) {
		return Texts.translatable("make_bubbles_pop.config." + key);
	}

	private static Component tooltip(String key) {
		return Texts.translatable("make_bubbles_pop.config." + key + ".tooltip");
	}

	public static Screen create(Screen parent, PlatformConfig config) {
		// Reload config from storage before opening the screen
		config.load();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(title("title"))
				.setSavingRunnable(config::save);

		ConfigCategory general = builder.getOrCreateCategory(title("title"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		general.addEntry(entryBuilder.startBooleanToggle(title("POP_PARTICLE_ENABLED"), MakeBubblesPopConfig.POP_PARTICLE_ENABLED)
				.setTooltip(tooltip("POP_PARTICLE_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.POP_PARTICLE_ENABLED = newValue)
				.build());

		general.addEntry(entryBuilder.startIntSlider(title("BUBBLE_POP_VOLUME"), (int) (MakeBubblesPopConfig.BUBBLE_POP_VOLUME * 100), 0, 100)
				.setTooltip(tooltip("BUBBLE_POP_VOLUME"))
				.setTextGetter(value -> Texts.literal(String.format("%d%%", value)))
				.setDefaultValue(10)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.BUBBLE_POP_VOLUME = newValue / 100.0F)
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("POPPED_BUBBLES_MAINTAIN_VELOCITY"), MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY)
				.setTooltip(tooltip("POPPED_BUBBLES_MAINTAIN_VELOCITY"))
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = newValue)
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("BUBBLE_PHYSICS_ENABLED"), MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED)
				.setTooltip(tooltip("BUBBLE_PHYSICS_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = newValue)
				.build());

		general.addEntry(entryBuilder.startIntSlider(title("BUBBLE_LIFETIME_MULTIPLIER"), (int) MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER, 1, 100)
				.setTooltip(tooltip("BUBBLE_LIFETIME_MULTIPLIER"))
				.setDefaultValue(32)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = newValue.doubleValue())
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("BIOME_COLORS_ENABLED"), MakeBubblesPopConfig.BIOME_COLORS_ENABLED)
				.setTooltip(tooltip("BIOME_COLORS_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.BIOME_COLORS_ENABLED = newValue)
				.build());

		general.addEntry(entryBuilder.startIntSlider(title("BIOME_COLOR_INTENSITY"), (int) (MakeBubblesPopConfig.BIOME_COLOR_INTENSITY * 100), 0, 100)
				.setTooltip(tooltip("BIOME_COLOR_INTENSITY"))
				.setTextGetter(value -> Texts.literal(String.format("%d%%", value)))
				.setDefaultValue(65)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = newValue / 100.0F)
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("CHEST_BUBBLES_ENABLED"), MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED)
				.setTooltip(tooltip("CHEST_BUBBLES_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = newValue)
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("BARREL_BUBBLES_ENABLED"), MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)
				.setTooltip(tooltip("BARREL_BUBBLES_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = newValue)
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("CONTAINER_SOUND_ENABLED"), MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED)
				.setTooltip(tooltip("CONTAINER_SOUND_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = newValue)
				.build());

		general.addEntry(entryBuilder.startBooleanToggle(title("EXPLOSION_BUBBLES_ENABLED"), MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED)
				.setTooltip(tooltip("EXPLOSION_BUBBLES_ENABLED"))
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = newValue)
				.build());

		return builder.build();
	}

}
*///?}

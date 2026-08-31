package net.tschipcraft.make_bubbles_pop.config;

//? yacl {
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.Texts;

/**
 * Config screen built with Yet Another Config Lib. Compiled on Fabric 1.20.1+ and on NeoForge.
 */
public final class YACLScreen {

	private YACLScreen() {
		throw new IllegalStateException("Utility class");
	}

	private static Component title(String key) {
		return Texts.translatable("make_bubbles_pop.config." + key);
	}

	private static OptionDescription tooltip(String key) {
		return OptionDescription.of(Texts.translatable("make_bubbles_pop.config." + key + ".tooltip"));
	}

	private static Option<Boolean> toggle(String key, boolean defaultValue, java.util.function.Supplier<Boolean> getter, java.util.function.Consumer<Boolean> setter) {
		return Option.<Boolean>createBuilder()
				.name(title(key))
				.description(tooltip(key))
				.controller(TickBoxControllerBuilder::create)
				.binding(defaultValue, getter, setter)
				.build();
	}

	private static Option<Float> percent(String key, float defaultValue, java.util.function.Supplier<Float> getter, java.util.function.Consumer<Float> setter) {
		return Option.<Float>createBuilder()
				.name(title(key))
				.description(tooltip(key))
				.customController(opt -> FloatSliderControllerBuilder.create(opt)
						.range(0F, 1.0F)
						.step(0.01F)
						.formatValue(value -> Texts.literal(String.format("%.0f%%", value * 100)))
						.build())
				.binding(defaultValue, getter, setter)
				.build();
	}

	public static Screen create(Screen parent, PlatformConfig config) {
		// Reload config from storage before opening the screen
		config.load();

		Option<Boolean> popParticle = toggle("POP_PARTICLE_ENABLED", true,
				() -> MakeBubblesPopConfig.POP_PARTICLE_ENABLED, v -> MakeBubblesPopConfig.POP_PARTICLE_ENABLED = v);

		Option<Float> popVolume = percent("BUBBLE_POP_VOLUME", 0.1F,
				() -> MakeBubblesPopConfig.BUBBLE_POP_VOLUME, v -> MakeBubblesPopConfig.BUBBLE_POP_VOLUME = v);

		Option<Boolean> maintainVelocity = toggle("POPPED_BUBBLES_MAINTAIN_VELOCITY", false,
				() -> MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY, v -> MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = v);

		Option<Boolean> physics = toggle("BUBBLE_PHYSICS_ENABLED", true,
				() -> MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED, v -> MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = v);

		Option<Double> lifetime = Option.<Double>createBuilder()
				.name(title("BUBBLE_LIFETIME_MULTIPLIER"))
				.description(tooltip("BUBBLE_LIFETIME_MULTIPLIER"))
				.customController(opt -> DoubleSliderControllerBuilder.create(opt).range(1D, 100D).step(0.25D).build())
				.binding(32D, () -> MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER, v -> MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = v)
				.build();

		Option<Boolean> biomeColors = toggle("BIOME_COLORS_ENABLED", true,
				() -> MakeBubblesPopConfig.BIOME_COLORS_ENABLED, v -> MakeBubblesPopConfig.BIOME_COLORS_ENABLED = v);

		Option<Float> biomeIntensity = percent("BIOME_COLOR_INTENSITY", 0.65F,
				() -> MakeBubblesPopConfig.BIOME_COLOR_INTENSITY, v -> MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = v);

		Option<Boolean> chestBubbles = toggle("CHEST_BUBBLES_ENABLED", true,
				() -> MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED, v -> MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = v);

		Option<Boolean> barrelBubbles = toggle("BARREL_BUBBLES_ENABLED", true,
				() -> MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED, v -> MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = v);

		Option<Boolean> containerSound = toggle("CONTAINER_SOUND_ENABLED", true,
				() -> MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED, v -> MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = v);

		Option<Boolean> explosionBubbles = toggle("EXPLOSION_BUBBLES_ENABLED", true,
				() -> MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED, v -> MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = v);

		return YetAnotherConfigLib.createBuilder()
				.title(title("title"))
				.category(ConfigCategory.createBuilder()
						.name(title("title"))
						.option(popParticle)
						.option(popVolume)
						.option(maintainVelocity)
						.option(physics)
						.option(lifetime)
						.option(biomeColors)
						.option(biomeIntensity)
						.option(chestBubbles)
						.option(barrelBubbles)
						.option(containerSound)
						.option(explosionBubbles)
						.build())
				.save(config::save)
				.build()
				.generateScreen(parent);
	}

}
//?}

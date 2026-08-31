package net.tschipcraft.make_bubbles_pop.platform.neoforge;

// Native NeoForge config spec. Storage only - the screen comes from ConfigScreens.
// Only line comments in this file: Stonecutter block-comments the whole body on other loaders.
//? neoforge {
/*import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.config.PlatformConfig;

// NeoForge 21.8 (Minecraft 1.21.6) merged the mod and game event buses, removing both the
// `bus` attribute and the EventBusSubscriber.Bus enum. There is only one bus to subscribe to now.
//? <1.21.6 {
@EventBusSubscriber(modid = MakeBubblesPop.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
//?} >=1.21.6 {
@EventBusSubscriber(modid = MakeBubblesPop.MOD_ID)
//?}
public final class NeoForgeConfig {

	private NeoForgeConfig() {
		throw new IllegalStateException("Config class cannot be instantiated");
	}

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.BooleanValue POP_PARTICLE_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.POP_PARTICLE_ENABLED")
			.comment("Default: true")
			.define("POP_PARTICLE_ENABLED", true);

	private static final ModConfigSpec.IntValue BUBBLE_POP_VOLUME = BUILDER
			.translation("make_bubbles_pop.config.BUBBLE_POP_VOLUME")
			.comment("Default: 10")
			.defineInRange("BUBBLE_POP_VOLUME", 10, 0, 100);

	private static final ModConfigSpec.BooleanValue POPPED_BUBBLES_MAINTAIN_VELOCITY = BUILDER
			.translation("make_bubbles_pop.config.POPPED_BUBBLES_MAINTAIN_VELOCITY")
			.comment("If enabled, popped bubbles will maintain their velocity. Default: false")
			.define("POPPED_BUBBLES_MAINTAIN_VELOCITY", false);

	private static final ModConfigSpec.BooleanValue BUBBLE_PHYSICS_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.BUBBLE_PHYSICS_ENABLED")
			.comment("If enabled, bubbles will be affected by physics when colliding with blocks or entities. Default: true")
			.define("BUBBLE_PHYSICS_ENABLED", true);

	private static final ModConfigSpec.IntValue BUBBLE_LIFETIME_MULTIPLIER = BUILDER
			.translation("make_bubbles_pop.config.BUBBLE_LIFETIME_MULTIPLIER")
			.comment("The time it takes for a bubble to pop underwater. Default: 32 (Vanilla: 8)")
			.defineInRange("BUBBLE_LIFETIME_MULTIPLIER", 32, 1, 100);

	private static final ModConfigSpec.BooleanValue BIOME_COLORS_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.BIOME_COLORS_ENABLED")
			.comment("If enabled, bubbles will have a color based on the biome they are in. Default: true")
			.define("BIOME_COLORS_ENABLED", true);

	private static final ModConfigSpec.IntValue BIOME_COLOR_INTENSITY = BUILDER
			.translation("make_bubbles_pop.config.BIOME_COLOR_INTENSITY")
			.comment("Default: 65")
			.defineInRange("BIOME_COLOR_INTENSITY", 65, 0, 100);

	private static final ModConfigSpec.BooleanValue CHEST_BUBBLES_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.CHEST_BUBBLES_ENABLED")
			.comment("If enabled, bubbles will appear when opening chests underwater. Default: true")
			.define("CHEST_BUBBLES_ENABLED", true);

	private static final ModConfigSpec.BooleanValue BARREL_BUBBLES_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.BARREL_BUBBLES_ENABLED")
			.comment("If enabled, bubbles will appear when opening barrels underwater. Default: true")
			.define("BARREL_BUBBLES_ENABLED", true);

	private static final ModConfigSpec.BooleanValue CONTAINER_SOUND_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.CONTAINER_SOUND_ENABLED")
			.comment("If enabled, a sound will play when opening chests or barrels underwater. Default: true")
			.define("CONTAINER_SOUND_ENABLED", true);

	private static final ModConfigSpec.BooleanValue EXPLOSION_BUBBLES_ENABLED = BUILDER
			.translation("make_bubbles_pop.config.EXPLOSION_BUBBLES_ENABLED")
			.comment("If enabled, bubbles will appear when an explosion occurs underwater. Default: true")
			.define("EXPLOSION_BUBBLES_ENABLED", true);

	public static final ModConfigSpec SPEC = BUILDER.build();

	// Adapter handed to ConfigScreens; NeoForge itself owns reading the file.
	public static final PlatformConfig PLATFORM = new PlatformConfig() {
		@Override
		public void load() {
			// NeoForge keeps the spec in sync for us.
		}

		@Override
		public void save() {
			NeoForgeConfig.save();
		}
	};

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		MakeBubblesPopConfig.POP_PARTICLE_ENABLED = POP_PARTICLE_ENABLED.get();
		MakeBubblesPopConfig.BUBBLE_POP_VOLUME = BUBBLE_POP_VOLUME.get() / 100.0F;
		MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = POPPED_BUBBLES_MAINTAIN_VELOCITY.get();
		MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = BUBBLE_PHYSICS_ENABLED.get();
		MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = BUBBLE_LIFETIME_MULTIPLIER.get().doubleValue();
		MakeBubblesPopConfig.BIOME_COLORS_ENABLED = BIOME_COLORS_ENABLED.get();
		MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = BIOME_COLOR_INTENSITY.get() / 100.0F;
		MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = CHEST_BUBBLES_ENABLED.get();
		MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = BARREL_BUBBLES_ENABLED.get();
		MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = CONTAINER_SOUND_ENABLED.get();
		MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = EXPLOSION_BUBBLES_ENABLED.get();
	}

	static void save() {
		POP_PARTICLE_ENABLED.set(MakeBubblesPopConfig.POP_PARTICLE_ENABLED);
		BUBBLE_POP_VOLUME.set((int) (MakeBubblesPopConfig.BUBBLE_POP_VOLUME * 100));
		POPPED_BUBBLES_MAINTAIN_VELOCITY.set(MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY);
		BUBBLE_PHYSICS_ENABLED.set(MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED);
		BUBBLE_LIFETIME_MULTIPLIER.set((int) MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER);
		BIOME_COLORS_ENABLED.set(MakeBubblesPopConfig.BIOME_COLORS_ENABLED);
		BIOME_COLOR_INTENSITY.set((int) (MakeBubblesPopConfig.BIOME_COLOR_INTENSITY * 100));
		CHEST_BUBBLES_ENABLED.set(MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED);
		BARREL_BUBBLES_ENABLED.set(MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED);
		CONTAINER_SOUND_ENABLED.set(MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED);
		EXPLOSION_BUBBLES_ENABLED.set(MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED);
		SPEC.save();
	}

}
*///?}

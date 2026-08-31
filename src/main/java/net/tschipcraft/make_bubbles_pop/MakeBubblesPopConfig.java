package net.tschipcraft.make_bubbles_pop;

/**
 * Main value holder config class for Make Bubbles Pop. Actual values are set in the respective
 * platform-specific config classes under {@code net.tschipcraft.make_bubbles_pop.platform}.
 */
public final class MakeBubblesPopConfig {

	private MakeBubblesPopConfig() {
		throw new IllegalStateException("Main value holder config class cannot be instantiated");
	}

	public static boolean POP_PARTICLE_ENABLED = true;
	public static float BUBBLE_POP_VOLUME = 0.1F;
	public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = false;

	public static boolean BUBBLE_PHYSICS_ENABLED = true;
	public static double BUBBLE_LIFETIME_MULTIPLIER = 32D;
	public static boolean BIOME_COLORS_ENABLED = true;
	public static float BIOME_COLOR_INTENSITY = 0.65F;
	public static boolean CHEST_BUBBLES_ENABLED = true;
	public static boolean BARREL_BUBBLES_ENABLED = true;
	public static boolean CONTAINER_SOUND_ENABLED = true;
	public static boolean EXPLOSION_BUBBLES_ENABLED = true;

	public static double getInitialVelocity(double original) {
		return POPPED_BUBBLES_MAINTAIN_VELOCITY ? original : 0D;
	}

}

package net.tschipcraft.make_bubbles_pop.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.mixin.client.SingleQuadParticleAccessor;

//? >=1.21.9 {
import net.minecraft.client.particle.SingleQuadParticle;
//?} <1.21.9 {
/*import net.minecraft.client.particle.TextureSheetParticle;
*///?}

// 26 moved BlockAndTintGetter under client.renderer.block and Level no longer satisfies the
// BiomeColors parameter without an explicit cast.
//? >=26 {
import net.minecraft.client.renderer.block.BlockAndTintGetter;
//?}

public final class BubbleUtil {

	private BubbleUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Vanilla's default attenuation distance, squared. {@code SoundEngine} hands OpenAL a linear
	 * rolloff over {@code max(volume, 1) * Sound.getAttenuationDistance()}, so a pop further out
	 * than this is mixed at zero gain.
	 */
	private static final double POP_SOUND_RADIUS_SQR = 16D * 16D;

	/**
	 * How many pop sounds may start per tick.
	 *
	 * <p>Every sound holds its channel for at least 20 ticks, so a saturated budget occupies about
	 * {@code MAX_POP_SOUNDS_PER_TICK * 20} channels.
	 */
	private static final int MAX_POP_SOUNDS_PER_TICK = 3;

	/** Game time {@link #popSoundBudget} was last refilled for. */
	private static long popSoundTick = Long.MIN_VALUE;

	/** Eligible pop sounds left this tick. Client thread only. */
	private static int popSoundBudget;

	/**
	 * Builds a {@link BlockPos} from world coordinates.
	 *
	 * <p>1.19.4 replaced the {@code BlockPos(double, double, double)} constructor with
	 * {@code BlockPos.containing}.
	 */
	public static BlockPos blockPos(double x, double y, double z) {
		//? >=1.19.4 {
		return BlockPos.containing(x, y, z);
		//?} <1.19.4 {
		/*return new BlockPos(x, y, z);
		*///?}
	}

	/**
	 * Add a bubble pop particle and sound at the specified location in the world.
	 *
	 * @param scale the quad size of the bubble that popped, carried over so the pop particle
	 *              matches the size of the bubble it replaces
	 */
	public static void popBubble(Level level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scale) {
		if (MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
			Particle bubble = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.BUBBLE_POP, x, y, z,
					MakeBubblesPopConfig.getInitialVelocity(velocityX),
					MakeBubblesPopConfig.getInitialVelocity(velocityY),
					MakeBubblesPopConfig.getInitialVelocity(velocityZ)
			);
			if (bubble != null) {
				((SingleQuadParticleAccessor) bubble).makeBubblesPop$setQuadSize(scale * 2F);
			}
			float volume = MakeBubblesPopConfig.BUBBLE_POP_VOLUME - (level.getRandom().nextFloat() * 0.1F);
			if (volume > 0F && claimPopSound(level, x, y, z)) {
				level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, volume, 0.85F + (level.getRandom().nextFloat() * 0.3F), false);
			}
		}
	}

	/**
	 * Whether a bubble popping at these coordinates may start a sound this tick.
	 */
	private static boolean claimPopSound(Level level, double x, double y, double z) {
		long tick = level.getGameTime();
		if (tick != popSoundTick) {
			popSoundTick = tick;
			popSoundBudget = MAX_POP_SOUNDS_PER_TICK;
		} else if (popSoundBudget <= 0) {
			return false;
		}

		Entity listener = Minecraft.getInstance().getCameraEntity();
		if (listener == null || listener.distanceToSqr(x, y, z) > POP_SOUND_RADIUS_SQR) {
			return false;
		}

		popSoundBudget--;
		return true;
	}

	/**
	 * Tint the calling particle based on the water color of the biome it sits in.
	 */
	//? >=1.21.9 {
	public static void tintBubble(Level level, double x, double y, double z, SingleQuadParticle particle) {
	//?} <1.21.9 {
	/*public static void tintBubble(Level level, double x, double y, double z, TextureSheetParticle particle) {
	*///?}
		if (MakeBubblesPopConfig.BIOME_COLORS_ENABLED) {
			//? <26 {
			/*int waterColor = BiomeColors.getAverageWaterColor(level, blockPos(x, y, z));
			*///?} >=26 {
			int waterColor = BiomeColors.getAverageWaterColor((BlockAndTintGetter) level, blockPos(x, y, z));
			//?}

			float intensity = MakeBubblesPopConfig.BIOME_COLOR_INTENSITY;
			float whiteMix = 1F - intensity;
			float cr = whiteMix + intensity * ((waterColor >> 16 & 0xFF) / 255F);
			float cg = whiteMix + intensity * ((waterColor >> 8 & 0xFF) / 255F);
			float cb = whiteMix + intensity * ((waterColor & 0xFF) / 255F);
			particle.setColor(cr, cg, cb);
		}
	}

}

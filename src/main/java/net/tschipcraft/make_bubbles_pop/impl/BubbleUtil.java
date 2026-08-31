package net.tschipcraft.make_bubbles_pop.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
			level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, MakeBubblesPopConfig.BUBBLE_POP_VOLUME - (level.getRandom().nextFloat() * 0.1F), 0.85F + (level.getRandom().nextFloat() * 0.3F), false);
		}
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

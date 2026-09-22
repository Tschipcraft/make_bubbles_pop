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

// Sulfur bubbles were added in 26.2.
//? >=26.2 {
import com.google.common.collect.ImmutableList;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.mixin.client.BubblePopParticleAccessor;

import java.util.List;
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
	 * @return the pop particle, or {@code null} if none was spawned
	 */
	public static Particle popBubble(Level level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scale) {
		Particle bubble = null;

		if (MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
			bubble = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.BUBBLE_POP, x, y, z,
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

		return bubble;
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

	/**
	 * Swap a bubble pop to white frames take from {@code textures/particle/}, since a tint can only
	 * darken vanilla's sprite.
	 *
	 * <p>The frames are vanilla's desaturated by {@code (luma + max) / 2}.
	 */
	public static void whiten(Particle pop) {
		//? >=26.2 {
		if (!(pop instanceof BubblePopParticleAccessor accessor)) {
			return;
		}

		SpriteSet frames = whitePopFrames();
		accessor.makeBubblesPop$setSprites(frames);
		// The constructor already took a sprite from vanilla's set; replace it before it is drawn
		((SingleQuadParticle) pop).setSpriteFromAge(frames);
		((SingleQuadParticle) pop).setColor(1F, 1F, 1F);
		//?}
	}

	//? >=26.2 {
	/** Look up on every call. */
	private static SpriteSet whitePopFrames() {
		// 26 deprecated TextureAtlas.LOCATION_PARTICLES for lookups by AtlasIds
		TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES);

		// Add 5 frames
		ImmutableList.Builder<TextureAtlasSprite> frames = ImmutableList.builder();
		for (int i = 0; i < 5; i++) {
			frames.add(atlas.getSprite(Identifier.fromNamespaceAndPath(MakeBubblesPop.MOD_ID, "bubble_pop_white_" + i)));
		}
		return new PopFrames(frames.build());
	}

	/** Same frame arithmetic as vanilla's {@code MutableSpriteSet}. */
	private record PopFrames(List<TextureAtlasSprite> sprites) implements SpriteSet {

		@Override
		public TextureAtlasSprite get(int index, int max) {
			return this.sprites.get(index * (this.sprites.size() - 1) / max);
		}

		@Override
		public TextureAtlasSprite get(RandomSource random) {
			return this.sprites.get(random.nextInt(this.sprites.size()));
		}

		@Override
		public TextureAtlasSprite first() {
			return this.sprites.getFirst();
		}

	}
	//?}

}

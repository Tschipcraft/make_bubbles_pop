package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Thins out the bubble particle amount a guardian spawns from its attack beam.
 */
@Mixin(Guardian.class)
public abstract class GuardianBeamBubble {

	// Ordinal 0 is the trail of a swimming guardian, ordinal 1 the beam
	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
	private void makeBubblesPop$thinBeamBubbles(Level level, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
		// Vanilla's mean bubble lifetime, 10 ln 5 ticks from 8 / (r * 0.8 + 0.2), over BubblePop's per
		// unit of multiplier, ln 8 / 0.7 ticks from multiplier / (r * 0.7 + 0.1)
		if (level.getRandom().nextDouble() < 5.42D / MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER) {
			level.addParticle(particle, x, y, z, xd, yd, zd);
		}
	}

}

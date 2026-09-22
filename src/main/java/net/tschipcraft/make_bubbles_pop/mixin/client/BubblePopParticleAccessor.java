package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.SpriteSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the pop particle's sprite set. The whole set is swapped, since the particle re-picks its
 * sprite from it every tick.
 */
@Mixin(BubblePopParticle.class)
public interface BubblePopParticleAccessor {

	@Mutable
	@Accessor("sprites")
	void makeBubblesPop$setSprites(SpriteSet sprites);

}

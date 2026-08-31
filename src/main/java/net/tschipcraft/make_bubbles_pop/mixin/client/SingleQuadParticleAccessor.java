package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the particle quad size so a popping bubble can hand its size to the pop particle it
 * spawns, keeping the two visually consistent.
 */
@Mixin(SingleQuadParticle.class)
public interface SingleQuadParticleAccessor {

	@Accessor("quadSize")
	void makeBubblesPop$setQuadSize(float quadSize);

}

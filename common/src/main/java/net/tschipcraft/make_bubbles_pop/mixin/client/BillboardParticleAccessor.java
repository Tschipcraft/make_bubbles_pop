package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.BillboardParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BillboardParticle.class)
public interface BillboardParticleAccessor {

    @Accessor
    void setScale(float scale);

}

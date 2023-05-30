package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionSeedParticle;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HugeExplosionSeedParticle.class)
public abstract class ExplosionEmitterBubble extends NoRenderParticle {

    protected ExplosionEmitterBubble(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", shift = At.Shift.BEFORE))
    protected void injectBubbleParticle(CallbackInfo info) {
        // Add bubble particles to explosions
        if (this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            for (int i = 0; i < 2; i++) {
                double d = (this.random.nextDouble() - this.random.nextDouble());
                double e = (this.random.nextDouble() - this.random.nextDouble());
                double f = (this.random.nextDouble() - this.random.nextDouble());
                double d2 = this.x + d * 4.0;
                double e2 = this.y + e * 4.0;
                double f2 = this.z + f * 4.0;
                this.level.addParticle(ParticleTypes.BUBBLE, d2, e2, f2, d * 2, e * 2, f * 2);
            }
        }
    }
}

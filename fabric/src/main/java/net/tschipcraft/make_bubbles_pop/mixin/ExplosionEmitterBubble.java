package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.particle.ExplosionEmitterParticle;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin injects into the ExplosionEmitter particle class to add bubbles to explosions underwater.
 */
@Mixin(ExplosionEmitterParticle.class)
public abstract class ExplosionEmitterBubble extends NoRenderParticle {

    protected ExplosionEmitterBubble(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", shift = At.Shift.BEFORE))
    protected void injectBubbleParticle(CallbackInfo info) {
        // Add bubble particles to explosions underwater
        if (this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
            for (int i = 0; i < 2; i++) {
                double d = (this.random.nextDouble() - this.random.nextDouble());
                double e = (this.random.nextDouble() - this.random.nextDouble());
                double f = (this.random.nextDouble() - this.random.nextDouble());
                double d2 = this.x + d * 4.0;
                double e2 = this.y + e * 4.0;
                double f2 = this.z + f * 4.0;
                this.world.addParticle(ParticleTypes.BUBBLE, d2, e2, f2, d * 2, e * 2, f * 2);
            }
        }
    }
}

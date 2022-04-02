package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnPop extends SpriteBillboardParticle {

    protected BubbleColumnPop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;markDead()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
    }
}

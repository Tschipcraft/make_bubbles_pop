package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnPop extends SpriteBillboardParticle {
    // TODO: Add support for overhauled bubble behavior (#3) - next version

    protected BubbleColumnPop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    // Tint bubble based on water color
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
        BubbleUtil.tintBubble(world, this.x, this.y, this.z, this);
    }

    // Inject pop particle
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;markDead()V", shift = At.Shift.AFTER))
    protected void makeBubblesPop$injectPopParticle(CallbackInfo info) {
        BubbleUtil.popBubble(world, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
    }

    // Catch age removal
    // This is needed since the tick() method of the super class handles removing the particle when it reaches its lifetime
    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    protected void makeBubblesPop$injectPopParticleToSuper(CallbackInfo info) {
        if ((this.age + 1) >= this.maxAge) {
            this.markDead();
            BubbleUtil.popBubble(world, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    /*
    // Increase upward motion to compensate for the increased momentum of normal bubble particles - looks weird (maybe add as an option?)
    @Inject(method = "tick()V", at = @At("TAIL"))
    protected void makeBubblesPop$injectUpwardsMotion(CallbackInfo info) {
        // Upwards motion
        this.velocityY += 0.01;
    }
     */
}

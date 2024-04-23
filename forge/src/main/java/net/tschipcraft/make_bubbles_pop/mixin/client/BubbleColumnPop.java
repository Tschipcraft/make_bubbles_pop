package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnPop extends TextureSheetParticle {
    // TODO: Add support for overhauled bubble behavior (#3) - next version

    protected BubbleColumnPop(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    // Tint bubble based on water color
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, CallbackInfo ci) {
        BubbleUtil.tintBubble(this.level, this.x, this.y, this.z, this);
    }

    // Inject pop particle
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;remove()V", shift = At.Shift.AFTER))
    protected void makeBubblesPop$injectPopParticle(CallbackInfo info) {
        BubbleUtil.popBubble(level, this.x, this.y, this.z, this.xd, this.yd, this.zd);
    }

    // Catch age removal
    // This is needed since the tick() method of the super class handles removing the particle when it reaches its lifetime
    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    protected void makeBubblesPop$injectPopParticleToSuper(CallbackInfo info) {
        if ((this.age + 1) >= this.lifetime) {
            this.remove();
            BubbleUtil.popBubble(level, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        }
    }

    /*
    // Increase upward motion to compensate for the increased momentum of normal bubble particles - looks weird (maybe add as an option?)
    @Inject(method = "tick()V", at = @At("TAIL"))
    protected void makeBubblesPop$injectUpwardsMotion(CallbackInfo info) {
        // Upwards motion
        this.yd += 0.01;
    }
     */
}

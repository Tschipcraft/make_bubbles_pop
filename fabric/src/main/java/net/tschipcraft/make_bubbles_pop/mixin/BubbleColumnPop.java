package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
        this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 0.1f, 1f, true);
    }

    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    protected void injectPopParticletoSuper(CallbackInfo info) {
        if ((this.age + 1) >= this.maxAge) {
            this.markDead();
            this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    /*
    // Increase upward motion to compensate for the increased momentum of normal bubble particles - looks weird
    @Inject(method = "tick()V", at = @At("TAIL"))
    protected void injectUpwardsMotion(CallbackInfo info) {
        // Upwards motion
        this.velocityY += 0.01;
    }
     */
}

package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnPop extends SpriteBillboardParticle {
    // TODO: Add support for overhauled bubble behavior (already implemented in config)

    protected BubbleColumnPop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;markDead()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        // TODO: Global bubble pop
        if (MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
            this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                    MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityX : 0,
                    MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityY : 0,
                    MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityZ : 0
            );
            this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 0.1f - (world.random.nextFloat() * 0.1f), 0.85f + (world.random.nextFloat() * 0.3f), false);
        }
    }

    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    protected void injectPopParticleToSuper(CallbackInfo info) {
        if ((this.age + 1) >= this.maxAge) {
            this.markDead();
            if (MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
                this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                        MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityX : 0,
                        MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityY : 0,
                        MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityZ : 0
                );
                // TODO: Sound?
            }
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

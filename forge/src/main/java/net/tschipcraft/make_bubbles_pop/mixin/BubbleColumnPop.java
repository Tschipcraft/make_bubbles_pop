package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnPop extends TextureSheetParticle {

    protected BubbleColumnPop(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;remove()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        if (MakeBubblesPop.POP_PARTICLE_ENABLED) {
            this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                    MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.xd : 0,
                    MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.yd : 0,
                    MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.zd : 0
            );
        }
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, 0.1f, 0.85f + (level.random.nextFloat() * 0.3f), false);
    }

    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    protected void injectPopParticleToSuper(CallbackInfo info) {
        if ((this.age + 1) >= this.lifetime) {
            this.remove();
            if (MakeBubblesPop.POP_PARTICLE_ENABLED) {
                this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                        MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.xd : 0,
                        MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.yd : 0,
                        MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.zd : 0
                );
            }
        }
    }

    /*
    // Increase upward motion to compensate for the increased momentum of normal bubble particles - looks weird
    @Inject(method = "tick()V", at = @At("TAIL"))
    protected void injectUpwardsMotion(CallbackInfo info) {
        // Upwards motion
        this.yd += 0.01;
    }
     */
}

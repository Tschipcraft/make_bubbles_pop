package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.particle.CurrentDownParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CurrentDownParticle.class)
public abstract class CurrentDownPop extends SpriteBillboardParticle {

    protected CurrentDownPop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/CurrentDownParticle;markDead()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        //TODO: Add check for pop particles?
        this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityX : 0,
                MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityY : 0,
                MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.velocityZ : 0
        );
    }

}

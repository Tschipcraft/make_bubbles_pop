package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.WaterCurrentDownParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WaterCurrentDownParticle.class)
public abstract class CurrentDownPop extends TextureSheetParticle {

    protected CurrentDownPop(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterCurrentDownParticle;remove()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        //TODO: Add check for pop particles?
        this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.xd : 0,
                MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.yd : 0,
                MakeBubblesPop.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.zd : 0
        );
    }

}

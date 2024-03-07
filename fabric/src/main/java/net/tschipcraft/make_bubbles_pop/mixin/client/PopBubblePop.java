package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubblePopParticle.class)
public abstract class PopBubblePop extends SpriteBillboardParticle {

    protected PopBubblePop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    // Tint bubble pop particle based on water color
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        BubbleUtil.tintBubble(this.world, this.x, this.y, this.z, this);
    }

}

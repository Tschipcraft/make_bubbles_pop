package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubblePopParticle.class)
public abstract class PopBubblePop extends TextureSheetParticle {

    protected PopBubblePop(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    // Tint bubble pop particle based on water color
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites, CallbackInfo ci) {
        BubbleUtil.tintBubble(this.level, this.x, this.y, this.z, this);
    }

}

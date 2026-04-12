package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubblePopParticle.class)
public abstract class PopBubblePop extends SingleQuadParticle {

    protected PopBubblePop(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
    }

    // Tint bubble pop particle based on water color
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites, CallbackInfo ci) {
        BubbleUtil.tintBubble(this.level, this.x, this.y, this.z, this);
    }

}

package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubblePopParticle.class)
public abstract class PopBubblePop extends BillboardParticle {

    protected PopBubblePop(ClientWorld world, double x, double y, double z, Sprite sprite) {
        super(world, x, y, z, sprite);
    }

    // Tint bubble pop particle based on water color
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        BubbleUtil.tintBubble(this.world, this.x, this.y, this.z, this);
    }

}

package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Potent sulfur was added in 26.2.
//? >=26.2 {
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SulfurBubbleParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//?} <26.2 {
/*import net.minecraft.client.particle.BubblePopParticle;
*///?}

/**
 * Makes the bubbles rising from potent sulfur pop. They keep their white sprite rather than the
 * biome tint, so the pop is whitened to match.
 *
 * <p>Below 26.2 this is an empty mixin, so its entry in {@code make_bubbles_pop.mixins.json} needs no version guard.
 */
//? >=26.2 {
@Mixin(SulfurBubbleParticle.class)
public abstract class SulfurBubblePop extends SingleQuadParticle {

	protected SulfurBubblePop(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
	}

	// Inject pop particle
	@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/SulfurBubbleParticle;remove()V", shift = At.Shift.AFTER))
	protected void makeBubblesPop$injectPopParticle(CallbackInfo info) {
		BubbleUtil.whiten(BubbleUtil.popBubble(this.level, this.x, this.y, this.z, this.xd, this.yd, this.zd, this.quadSize));
	}

}
//?} <26.2 {
/*@Mixin(BubblePopParticle.class)
public abstract class SulfurBubblePop {
}
*///?}

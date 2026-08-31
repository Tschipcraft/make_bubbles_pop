package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >=1.21.9 {
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//?} <1.21.9 {
/*import net.minecraft.client.particle.TextureSheetParticle;
*///?}

@Mixin(BubbleColumnUpParticle.class)
//? <1.21.9 {
/*public abstract class BubbleColumnPop extends TextureSheetParticle {

	protected BubbleColumnPop(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
	}
*///?} >=1.21.9 {
public abstract class BubbleColumnPop extends SingleQuadParticle {

	protected BubbleColumnPop(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
	}
//?}

	// Tint bubble based on water color
	//? <1.21.9 {
	/*@Inject(method = "<init>", at = @At(value = "TAIL"))
	void makeBubblesPop$init(ClientLevel level, double x, double y, double z, double xa, double ya, double za, CallbackInfo ci) {
	*///?} >=1.21.9 {
	@Inject(method = "<init>", at = @At(value = "TAIL"))
	void makeBubblesPop$init(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite, CallbackInfo ci) {
	//?}
		BubbleUtil.tintBubble(this.level, this.x, this.y, this.z, this);
	}

	// Inject pop particle
	@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;remove()V", shift = At.Shift.AFTER))
	protected void makeBubblesPop$injectPopParticle(CallbackInfo info) {
		BubbleUtil.popBubble(this.level, this.x, this.y, this.z, this.xd, this.yd, this.zd, this.quadSize);
	}

	// Catch age removal
	// This is needed since the tick() method of the super class handles removing the particle when it reaches its lifetime
	@Inject(method = "tick()V", at = @At(value = "HEAD"))
	protected void makeBubblesPop$injectPopParticleToSuper(CallbackInfo info) {
		if ((this.age + 1) >= this.lifetime) {
			this.remove();
			BubbleUtil.popBubble(this.level, this.x, this.y, this.z, this.xd, this.yd, this.zd, this.quadSize);
		}
	}

}

package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.CurrentDownParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CurrentDownParticle.class)
public abstract class CurrentDownPop extends SpriteBillboardParticle {

    protected CurrentDownPop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    // Inject pop particle
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/CurrentDownParticle;markDead()V", shift = At.Shift.AFTER))
    protected void makeBubblesPop$injectPopParticle(CallbackInfo info) {
        //TODO: Global bubble pop
        this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                MakeBubblesPop.getConfigInitialVelocity(this.velocityX),
                MakeBubblesPop.getConfigInitialVelocity(this.velocityY),
                MakeBubblesPop.getConfigInitialVelocity(this.velocityZ)
        );
    }


    // Decrease spiral radius
    @ModifyConstant(method = "tick()V", constant = @Constant(doubleValue = 0.07), expect = 2)
    private double makeBubblesPop$injectDecreasedSpiralRadius(double value) {
        return 0.06D;
    }

    // Adjust initial particle starting position to make up for the decreased spiral radius
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static double makeBubblesPop$injectXOffset(double d) {
        return d - 0.025D;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    private static double makeBubblesPop$injectZOffset(double f) {
        return f + 0.025D;
    }

    // Make hitting the magma block/floor look nicer
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/CurrentDownParticle;move(DDD)V"))
    private void makeBubblesPop$injectFloorCollision(CallbackInfo ci) {
        if (!this.world.isWater(BlockPos.ofFloored(this.x, this.y - this.scale * 1.5D, this.z))) {
            this.velocityY = 0.01D;
            this.age = this.maxAge;
        }
    }

}

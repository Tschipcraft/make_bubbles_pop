package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.WaterBubbleParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * <pre>
 * This mixin injects into the WaterBubbleParticle class to completely overhaul bubble behavior.
 * Notable changes for devs:
 *  - The tick() method is completely overwritten (no super calls)
 *  - Age now counts upwards like any other particle instead of maxAge downwards (wtf mojang)
 * </pre>
 */
@Mixin(WaterBubbleParticle.class)
public abstract class BubblePop extends SpriteBillboardParticle {

    protected BubblePop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Unique
    private float accelerationAngle;
    @Unique
    private byte accelerationTicker = 0;
    @Unique
    private byte routeDir = 0;

    /*
    // Original @Inject method to the tick function
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterBubbleParticle;markDead()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 0.1f, 1f, false);
    }
     */

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
        // Longer maxAge to enable bubbles to rise to the top (Could cause performance issues - you called it previous me)
        this.maxAge = (int) ((MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER : 32D) / (this.random.nextDouble() * 0.7D + 0.1D));
        this.accelerationAngle = this.random.nextFloat() * 360F;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    public void makeBubblesPop$tick(CallbackInfo ci) {
        ci.cancel();
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge || !this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.1, this.z)) || !this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z))) {
            // Outside water/maxAge reached -> pop with sound
            this.markDead();
            // TODO: Global bubble pop
            if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
                this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                        MakeBubblesPop.getConfigInitialVelocity(this.velocityX),
                        MakeBubblesPop.getConfigInitialVelocity(this.velocityY),
                        MakeBubblesPop.getConfigInitialVelocity(this.velocityZ)
                );
                this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, (MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BUBBLE_POP_VOLUME : .1f), .85f + (this.world.random.nextFloat() * .3f), false);
            }
        } else {

            // Upward motion
            // Scale dependant motion?
            // => http://seas.ucla.edu/stenstro/Bubble.pdf - see you in v1.0.0
            this.velocityY += 0.01F;
            this.move(this.velocityX, this.velocityY, this.velocityZ);

            // Detect stuck bubbles
            if (this.y == this.prevPosY) {
                this.age *= 2;
            }

            // Left and right motion

            // New direction
            if (this.accelerationTicker == 0) {
                this.accelerationAngle = this.random.nextFloat() * 360F;
            }

            this.accelerationTicker++;
            if (this.accelerationTicker % 5 == 0) {
                this.accelerationTicker = 0;
            }

            // Apply
            this.velocityX += (((double) this.accelerationTicker / 10D) * Math.cos(this.accelerationAngle) * 0.04D);
            this.velocityZ += (((double) this.accelerationTicker / 10D) * Math.sin(this.accelerationAngle) * 0.04D);

            this.velocityX *= 0.7500000238418579D;
            this.velocityY *= 0.8500000238418579D;
            this.velocityZ *= 0.7500000238418579D;


            // PHYSICS

            if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED) {

                /*
            PlayerEntity playerEntity = this.world.getClosestPlayer(this.x, this.y, this.z, 2.0, false);
            if (playerEntity != null) {
                // Bounce off player
                this.velocityX += (playerEntity.getVelocity().x - this.velocityX) * 0.2;
                this.velocityY += (playerEntity.getVelocity().y - this.velocityY) * 0.2;
                this.velocityZ += (playerEntity.getVelocity().z - this.velocityZ) * 0.2;
            }
            */

                // Search way around blocks
                if (!this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.8D, this.z)) && !this.world.isAir(BlockPos.ofFloored(this.x, this.y + 0.8D, this.z))) {
                    // Direct way upwards blocked -> search up different way to water surface

                    boolean escapePosX = this.world.isWater(BlockPos.ofFloored(this.x + 1D, this.y + 0.8D, this.z)) && this.world.isWater(BlockPos.ofFloored(this.x + 1D, this.y, this.z));
                    boolean escapeNegX = this.world.isWater(BlockPos.ofFloored(this.x - 1D, this.y + 0.8D, this.z)) && this.world.isWater(BlockPos.ofFloored(this.x - 1D, this.y, this.z));
                    boolean escapePosZ = this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.8D, this.z + 1D)) && this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z + 1D));
                    boolean escapeNegZ = this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.8D, this.z - 1D)) && this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z - 1D));

                /*
                Ebic screenshots
                 || world.getBlockState(BlockPos.ofFloored(this.x, this.y + 0.8, this.z)).getBlock() instanceof LightBlock

                escapePosX = !(this.world.getBlockState(BlockPos.ofFloored(this.x + 1, this.y + 0.8, this.z)).getBlock() instanceof LightBlock);
                escapeNegX = !(this.world.getBlockState(BlockPos.ofFloored(this.x - 1, this.y + 0.8, this.z)).getBlock() instanceof LightBlock);

                escapePosZ = false;
                escapeNegZ = false;
                 */

                    if (!(!escapePosX && !escapeNegX && !escapePosZ && !escapeNegZ)) {
                        for (int i = 0; i <= 5; i++) {
                            if (escapePosX && this.routeDir == 1) {
                                this.velocityX += 0.03D;
                                break;
                            } else if (escapeNegX && this.routeDir == 2) {
                                this.velocityX -= 0.03D;
                                break;
                            } else if (escapePosZ && this.routeDir == 3) {
                                this.velocityZ += 0.03D;
                                break;
                            } else if (escapeNegZ && this.routeDir == 4) {
                                this.velocityZ -= 0.03D;
                                break;
                            } else {
                                // Choose escape route direction
                                this.routeDir = (byte) (this.random.nextDouble() * 4D + 1D);
                            }
                        }
                    } else {
                        // No escape route
                    }
                } else {
                    // Reset escape route direction
                    this.routeDir = 0;
                }
            }
        }
    }

}

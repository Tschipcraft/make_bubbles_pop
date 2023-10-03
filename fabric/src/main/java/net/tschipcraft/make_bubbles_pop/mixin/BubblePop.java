package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.WaterBubbleParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterBubbleParticle.class)
public abstract class BubblePop extends SpriteBillboardParticle {

    protected BubblePop(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Unique
    private float accelerationAngle = (float)(Math.random() * 360F);
    @Unique
    private float accelerationTicker = 0F;
    @Unique
    private int routeDir = 0;

    /*
    // Original @Inject method to the tick function
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterBubbleParticle;markDead()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 0.1f, 1f, false);
    }
     */

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void init(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
        // Longer maxAge to enable bubbles to rise to the top (Could cause performance issues)
        this.maxAge = (int)(32.0 / (Math.random() * 0.7 + 0.1));
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        // Upward motion
        // Scale dependant motion?
        // => http://seas.ucla.edu/stenstro/Bubble.pdf
        //this.velocityY += .02f - (this.scale / 10f);
        this.velocityY += .01f;
        this.move(this.velocityX, this.velocityY, this.velocityZ);

        // Right and left motion
        this.velocityX += ((this.accelerationTicker / 10) * Math.cos(this.accelerationAngle) * 0.04);
        this.velocityZ += ((this.accelerationTicker / 10) * Math.sin(this.accelerationAngle) * 0.04);

        this.velocityX *= 0.7500000238418579;
        this.velocityY *= 0.8500000238418579;
        this.velocityZ *= 0.7500000238418579;

        // Accelerate
        if (this.accelerationTicker >= 5.0F) {
            this.accelerationTicker = 0.0F;
        }

        // New direction
        if (this.accelerationTicker == 0.0F) {
            this.accelerationAngle = (float)(Math.random() * 360);
        }

        this.accelerationTicker++;


        if (!this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z))) {
            // Outside water -> pop with sound
            this.markDead();
            if (MakeBubblesPop.POP_PARTICLE_ENABLED) {
                this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
            }
            this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 0.1f, 1f, false);
        } else if (this.maxAge-- <= 0 || !this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.1, this.z)) && this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z))) {
            // maxAge reached/Can't reach top -> pop with low-pitch sound
            this.markDead();
            if (MakeBubblesPop.POP_PARTICLE_ENABLED) {
                this.world.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
            }
            this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, 0.1f, 0f, false);
        } else {

            if (!this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.8, this.z))) {
                // Direct way upwards blocked -> search up different way to water surface

                boolean escapePosX = this.world.isWater(BlockPos.ofFloored(this.x + 1, this.y + 0.8, this.z)) && this.world.isWater(BlockPos.ofFloored(this.x + 1, this.y, this.z));
                boolean escapeNegX = this.world.isWater(BlockPos.ofFloored(this.x - 1, this.y + 0.8, this.z)) && this.world.isWater(BlockPos.ofFloored(this.x - 1, this.y, this.z));
                boolean escapePosZ = this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.8, this.z + 1)) && this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z + 1));
                boolean escapeNegZ = this.world.isWater(BlockPos.ofFloored(this.x, this.y + 0.8, this.z - 1)) && this.world.isWater(BlockPos.ofFloored(this.x, this.y, this.z - 1));

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
                            this.velocityX += 0.03;
                            break;
                        } else if (escapeNegX && this.routeDir == 2) {
                            this.velocityX -= 0.03;
                            break;
                        } else if (escapePosZ && this.routeDir == 3) {
                            this.velocityZ += 0.03;
                            break;
                        } else if (escapeNegZ && this.routeDir == 4) {
                            this.velocityZ -= 0.03;
                            break;
                        } else {
                            // Choose escape route direction
                            this.routeDir = (int)(Math.random() * 4) + 1;
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

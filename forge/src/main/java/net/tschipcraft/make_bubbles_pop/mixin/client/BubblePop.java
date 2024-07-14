package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.BubbleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

/**
 * <pre>
 * This mixin injects into the WaterBubbleParticle class to completely overhaul bubble behavior.
 * Notable changes for devs:
 *  - Age now counts upwards like any other particle instead of maxAge downwards (wtf mojang)
 * </pre>
 */
@Mixin(BubbleParticle.class)
public abstract class BubblePop extends TextureSheetParticle {
    
    protected BubblePop(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f);
    }

    @Unique
    private float accelerationAngle;
    @Unique
    private byte accelerationTicker = 0;
    @Unique
    private byte routeDir = 0;

    /*
    // Original @Inject method to the tick function
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterBubbleParticle;remove()V", shift = At.Shift.AFTER))
    protected void makeBubblesPop$injectPopParticle(CallbackInfo info) {
        this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, 0.1f, 1f, false);
    }
     */

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientLevel clientlevel, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
        // Longer lifetime to enable bubbles to fully rise to the top (Could cause performance issues - you called it previous me)
        this.lifetime = (int) ((MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER : 32.0D) / (this.random.nextDouble() * 0.7D + 0.1D));
        this.accelerationAngle = this.random.nextFloat() * 360F;

        // Tint bubble based on water color
        BubbleUtil.tintBubble(this.level, this.x, this.y, this.z, this);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    public void makeBubblesPop$tick(CallbackInfo ci) {
        ci.cancel();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime || !this.level.isWaterAt(BlockPos.containing(this.x, this.y + 0.1, this.z)) || !this.level.isWaterAt(BlockPos.containing(this.x, this.y, this.z))) {
            // Outside water/lifetime reached -> pop with sound
            this.remove();
            BubbleUtil.popBubble(level, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        } else {

            // Upward motion
            // Scale dependant motion?
            // => http://seas.ucla.edu/stenstro/Bubble.pdf - see you in v1.0.0
            this.yd += 0.01D;
            this.move(this.xd, this.yd, this.zd);

            // Detect stuck bubbles
            if (this.y == this.yo) {
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
            this.xd += ((this.accelerationTicker / 10D) * Math.cos(this.accelerationAngle) * 0.04D);
            this.zd += ((this.accelerationTicker / 10D) * Math.sin(this.accelerationAngle) * 0.04D);

            this.xd *= 0.7500000238418579D;
            this.yd *= 0.8500000238418579D;
            this.zd *= 0.7500000238418579D;


            // PHYSICS

            if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED) {

                // Entity interaction
                List<Entity> list = level.getEntities((Entity) null, this.getBoundingBox().inflate(0.5), new Predicate<>() {
                    @Override
                    public boolean test(Entity entity) {
                        return entity.isAlive() && entity instanceof LivingEntity && !entity.noPhysics;
                    }
                });

                for (Entity entityIn : list) {

                    float xDiff = (float) (this.x - entityIn.getX());
                    float zDiff = (float) (this.z - entityIn.getZ());
                    float absDiff = (float) Mth.absMax(xDiff, zDiff);

                    if (absDiff >= 0.0099F) {
                        absDiff = (float) Math.sqrt(absDiff);
                        xDiff /= absDiff;
                        zDiff /= absDiff;

                        float invertedDir = 1.0f / absDiff;

                        if (invertedDir > 1.0F)
                            invertedDir = 1.0F;

                        this.xd += xDiff * invertedDir / 20D;
                        this.zd += zDiff * invertedDir / 20D;

                        // Add entity velocity
                        // The y velocity 0.419875 is excluded, since some players falsely return this value if they have been flying in creative mode whilst loaded
                        this.xd += (entityIn.getDeltaMovement().x - this.xd) * 0.2D;
                        if (entityIn.getDeltaMovement().y != 0.419875D) this.yd += (entityIn.getDeltaMovement().y - this.yd) * 0.2D;
                        this.zd += (entityIn.getDeltaMovement().z - this.zd) * 0.2D;
                    }
                }

                // Search way around blocks
                if (!this.level.isWaterAt(BlockPos.containing(this.x, this.y + 0.8D, this.z)) && !this.level.isEmptyBlock(BlockPos.containing(this.x, this.y + 0.8D, this.z))) {
                    // Direct way upwards blocked -> search up different way to water surface

                    boolean escapePosX = this.level.isWaterAt(BlockPos.containing(this.x + 1D, this.y + 0.8D, this.z)) && this.level.isWaterAt(BlockPos.containing(this.x + 1D, this.y, this.z));
                    boolean escapeNegX = this.level.isWaterAt(BlockPos.containing(this.x - 1D, this.y + 0.8D, this.z)) && this.level.isWaterAt(BlockPos.containing(this.x - 1D, this.y, this.z));
                    boolean escapePosZ = this.level.isWaterAt(BlockPos.containing(this.x, this.y + 0.8D, this.z + 1)) && this.level.isWaterAt(BlockPos.containing(this.x, this.y, this.z + 1D));
                    boolean escapeNegZ = this.level.isWaterAt(BlockPos.containing(this.x, this.y + 0.8D, this.z - 1)) && this.level.isWaterAt(BlockPos.containing(this.x, this.y, this.z - 1D));

                    if (!(!escapePosX && !escapeNegX && !escapePosZ && !escapeNegZ)) {
                        for (int i = 0; i <= 5; i++) {
                            if (escapePosX && this.routeDir == 1) {
                                this.xd += 0.03D;
                                break;
                            } else if (escapeNegX && this.routeDir == 2) {
                                this.xd -= 0.03D;
                                break;
                            } else if (escapePosZ && this.routeDir == 3) {
                                this.zd += 0.03D;
                                break;
                            } else if (escapeNegZ && this.routeDir == 4) {
                                this.zd -= 0.03D;
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

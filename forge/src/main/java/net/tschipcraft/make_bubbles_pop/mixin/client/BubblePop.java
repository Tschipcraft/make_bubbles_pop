package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
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
 *  - The tick() method is completely overwritten (no super calls)
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
    protected void injectPopParticle(CallbackInfo info) {
        this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, 0.1f, 1f, false);
    }
     */

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void makeBubblesPop$init(ClientLevel clientlevel, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
        // Longer lifetime to enable bubbles to rise to the top (Could cause performance issues - you called it previous me)
        this.lifetime = (int) ((MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER : 32.0D) / (this.random.nextDouble() * 0.7D + 0.1D));
        this.accelerationAngle = this.random.nextFloat() * 360F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime || !this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.1, this.z)).is(FluidTags.WATER) || !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            // Outside water/lifetime reached -> pop with sound
            this.remove();
            // TODO: Global bubble pop
            if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
                this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z,
                        !MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.xd : 0,
                        !MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.yd : 0,
                        !MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? this.zd : 0
                );
                this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, (MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BUBBLE_POP_VOLUME : .1f), .85f + (this.level.random.nextFloat() * .3f), false);
            }
        } else {

            // Upward motion
            this.yd += 0.01F;
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
            this.xd += (((double) this.accelerationTicker / 10D) * Math.cos(this.accelerationAngle) * 0.04D);
            this.zd += (((double) this.accelerationTicker / 10D) * Math.sin(this.accelerationAngle) * 0.04D);

            this.xd *= 0.7500000238418579D;
            this.yd *= 0.8500000238418579D;
            this.zd *= 0.7500000238418579D;


            // PHYSICS

            if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED) {

                // Search way around blocks
                if (!this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.8D, this.z)).is(FluidTags.WATER)) {
                    // Direct way upwards blocked -> search up different way to water surface

                    boolean escapePosX = this.level.getFluidState(BlockPos.containing(this.x + 1D, this.y + 0.8D, this.z)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x + 1D, this.y, this.z)).is(FluidTags.WATER);
                    boolean escapeNegX = this.level.getFluidState(BlockPos.containing(this.x - 1D, this.y + 0.8D, this.z)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x - 1D, this.y, this.z)).is(FluidTags.WATER);
                    boolean escapePosZ = this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.8D, this.z + 1)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z + 1D)).is(FluidTags.WATER);
                    boolean escapeNegZ = this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.8D, this.z - 1)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z - 1D)).is(FluidTags.WATER);

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

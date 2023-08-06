package net.tschipcraft.make_bubbles_pop.mixin;

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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BubbleParticle.class)
public abstract class BubblePop extends TextureSheetParticle {
    
    protected BubblePop(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f);
    }

    private float accelerationAngle = (float)(Math.random() * 360);
    private float accelerationTicker = 0;
    private int routeDir = 0;

    /*
    // Original @Inject method to the tick function
    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/WaterBubbleParticle;remove()V", shift = At.Shift.AFTER))
    protected void injectPopParticle(CallbackInfo info) {
        this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, 0.1f, 1f, false);
    }
     */

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void init(ClientLevel clientlevel, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
        // Longer lifetime to enable bubbles to rise to the top (Could cause performance issues)
        this.lifetime = (int)(32.0 / (Math.random() * 0.7 + 0.1));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // Upwards motion
        this.yd += 0.01;
        this.move(this.xd, this.yd, this.zd);

        // Right and left motion
        this.xd += ((double)((this.accelerationTicker/10) * Mth.cos(this.accelerationAngle))*0.04);
        this.zd += ((double)((this.accelerationTicker/10) * Mth.sin(this.accelerationAngle))*0.04);

        this.xd *= 0.7500000238418579;
        this.yd *= 0.8500000238418579;
        this.zd *= 0.7500000238418579;

        // Accelerate
        if (this.accelerationTicker >= 5.0F) {
            this.accelerationTicker = 0.0F;
        }

        // New direction
        if (this.accelerationTicker == 0.0F) {
            this.accelerationAngle = (float)(Math.random() * 360);
        }

        this.accelerationTicker++;


        if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            // Outside water -> pop with sound
            this.remove();
            this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, 0.1f, 1f, false);
        } else if (this.lifetime-- <= 0 || !this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.1, this.z)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            // lifetime reached/Can't reach top -> pop with low-pitch sound
            this.remove();
            this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, 0.1f, 0f, false);
        } else {

            if (!this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.8, this.z)).is(FluidTags.WATER)) {
                // Direct way upwards blocked -> search up different way to water surface

                boolean escapePosX = this.level.getFluidState(BlockPos.containing(this.x + 1, this.y + 0.8, this.z)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x + 1, this.y, this.z)).is(FluidTags.WATER);
                boolean escapeNegX = this.level.getFluidState(BlockPos.containing(this.x - 1, this.y + 0.8, this.z)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x - 1, this.y, this.z)).is(FluidTags.WATER);
                boolean escapePosZ = this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.8, this.z + 1)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z + 1)).is(FluidTags.WATER);
                boolean escapeNegZ = this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.8, this.z - 1)).is(FluidTags.WATER) && this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z - 1)).is(FluidTags.WATER);

                if (!(!escapePosX && !escapeNegX && !escapePosZ && !escapeNegZ)) {
                    for (int i = 0; i <= 5; i++) {
                        if (escapePosX && this.routeDir == 1) {
                            this.xd += 0.03;
                            break;
                        } else if (escapeNegX && this.routeDir == 2) {
                            this.xd -= 0.03;
                            break;
                        } else if (escapePosZ && this.routeDir == 3) {
                            this.zd += 0.03;
                            break;
                        } else if (escapeNegZ && this.routeDir == 4) {
                            this.zd -= 0.03;
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

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
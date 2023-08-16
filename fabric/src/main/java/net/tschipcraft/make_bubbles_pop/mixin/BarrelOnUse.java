package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * This mixin injects into the BarrelBlock class to add bubbles to opening barrels underwater.
 */
@Mixin(BarrelBlock.class)
public abstract class BarrelOnUse {

    @Inject(method = "onUse", at = @At("HEAD"))
    public void injectBubbles(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        Random random = new Random();
        boolean bl = world != null;
        if (bl && world.isClient) {
            // Get direction of barrel block and test if its underwater
            Direction facing = state.contains(BarrelBlock.FACING) ? state.get(BarrelBlock.FACING) : Direction.NORTH;
            if (world.isWater(pos.offset(facing)) && !state.get(BarrelBlock.OPEN)) {
                // A barrel block has been opened underwater by the current player
                // Sadly I haven't found a way to play particles and sound when other players open the barrel

                if (facing != Direction.DOWN) {
                    for (int i = 0; i < 6 + random.nextInt(12); i++) {
                        float xOffset = 0f;
                        float yOffset = 0f;
                        float zOffset = 0f;
                        float xOffsetRand = 0f;
                        float yOffsetRand = 0f;
                        float zOffsetRand = 0f;

                        float xVelocityRand = 0f;
                        float zVelocityRand = 0f;

                        if (facing == Direction.NORTH) {
                            xOffset = .5f;
                            yOffset = .5f;
                            zOffset = 0f;
                            xOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            yOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            zVelocityRand = -random.nextFloat();
                        } else if (facing == Direction.SOUTH) {
                            xOffset = .5f;
                            yOffset = .5f;
                            zOffset = 1f;
                            xOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            yOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            zVelocityRand = random.nextFloat();
                        } else if (facing == Direction.EAST) {
                            xOffset = 1f;
                            yOffset = .5f;
                            zOffset = .5f;
                            yOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            zOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            xVelocityRand = random.nextFloat();
                        } else if (facing == Direction.WEST) {
                            xOffset = 0f;
                            yOffset = .5f;
                            zOffset = .5f;
                            yOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            zOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            xVelocityRand = -random.nextFloat();
                        } else if (facing == Direction.UP) {
                            xOffset = .5f;
                            yOffset = 1.5f;
                            zOffset = .5f;
                            xOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                            yOffsetRand = -(random.nextFloat() / 2f);
                            zOffsetRand = (random.nextFloat() - random.nextFloat()) * .3f;
                        }

                        world.addParticle(ParticleTypes.BUBBLE, pos.getX() + xOffset + xOffsetRand, pos.getY() + yOffset + yOffsetRand, pos.getZ() + zOffset + zOffsetRand, xVelocityRand, .05f + random.nextFloat() * .05f, zVelocityRand);
                    }
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.3f, 1.4f, false);
                }
            }
        }
    }
}
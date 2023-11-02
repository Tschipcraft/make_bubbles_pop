package net.tschipcraft.make_bubbles_pop.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;

@OnlyIn(Dist.CLIENT)
public class BarrelBubbler {

    public static void spawnBubbles(Level world, BlockPos pos, Direction facing) {
        boolean bl = world != null;
        if (bl && world.isClientSide && MakeBubblesPop.BARREL_BUBBLES_ENABLED) {
            if (facing != Direction.DOWN) {
                for (int i = 0; i < 6 + world.random.nextInt(12); i++) {
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
                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zVelocityRand = -world.random.nextFloat();
                    } else if (facing == Direction.SOUTH) {
                        xOffset = .5f;
                        yOffset = .5f;
                        zOffset = 1f;
                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zVelocityRand = world.random.nextFloat();
                    } else if (facing == Direction.EAST) {
                        xOffset = 1f;
                        yOffset = .5f;
                        zOffset = .5f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        xVelocityRand = world.random.nextFloat();
                    } else if (facing == Direction.WEST) {
                        xOffset = 0f;
                        yOffset = .5f;
                        zOffset = .5f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        xVelocityRand = -world.random.nextFloat();
                    } else if (facing == Direction.UP) {
                        xOffset = .5f;
                        yOffset = 1.5f;
                        zOffset = .5f;
                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        yOffsetRand = -(world.random.nextFloat() / 2f);
                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                    }

                    world.addParticle(ParticleTypes.BUBBLE, pos.getX() + xOffset + xOffsetRand, pos.getY() + yOffset + yOffsetRand, pos.getZ() + zOffset + zOffsetRand, xVelocityRand, .05f + world.random.nextFloat() * .05f, zVelocityRand);
                }
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.AMBIENT, 0.3f, 1.4f, false);
            }
        }
    }

}

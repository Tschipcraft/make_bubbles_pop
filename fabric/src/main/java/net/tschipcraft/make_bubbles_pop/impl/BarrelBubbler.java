package net.tschipcraft.make_bubbles_pop.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

@Environment(EnvType.CLIENT)
public class BarrelBubbler {

    public static void spawnBubbles(World world, BlockPos pos, Direction facing) {
        if (world != null && world.isClient && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)) {
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
                        zOffset = -.01f;
                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zVelocityRand = -world.random.nextFloat();
                    } else if (facing == Direction.SOUTH) {
                        xOffset = .5f;
                        yOffset = .5f;
                        zOffset = 1.01f;
                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zVelocityRand = world.random.nextFloat();
                    } else if (facing == Direction.EAST) {
                        xOffset = 1.01f;
                        yOffset = .5f;
                        zOffset = .5f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        xVelocityRand = world.random.nextFloat();
                    } else if (facing == Direction.WEST) {
                        xOffset = -.01f;
                        yOffset = .5f;
                        zOffset = .5f;
                        yOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        xVelocityRand = -world.random.nextFloat();
                    } else if (facing == Direction.UP) {
                        xOffset = .5f;
                        yOffset = 1.01f;
                        zOffset = .5f;
                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                        yOffsetRand = (world.random.nextFloat() * .25f);
                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                    }

                    world.addParticle(ParticleTypes.BUBBLE, pos.getX() + xOffset + xOffsetRand, pos.getY() + yOffset + yOffsetRand, pos.getZ() + zOffset + zOffsetRand, xVelocityRand, .05f + world.random.nextFloat() * .05f, zVelocityRand);
                }
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.3f, 1.4f, false);
            }
        }
    }

}

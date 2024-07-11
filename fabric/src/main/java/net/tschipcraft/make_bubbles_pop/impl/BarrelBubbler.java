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

import java.util.Random;

@Environment(EnvType.CLIENT)
public class BarrelBubbler {

    private BarrelBubbler() {
        throw new IllegalStateException("Utility class");
    }

    public static void spawnBubbles(World world, BlockPos pos, Direction facing, Random random) {
        if (world != null && world.isClient && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)) {
            if (facing != Direction.DOWN) {
                for (int i = 0; i < 6 + random.nextInt(12); i++) {
                    float xOffset = 0F;
                    float yOffset = 0F;
                    float zOffset = 0F;
                    float xOffsetRand = 0F;
                    float yOffsetRand = 0F;
                    float zOffsetRand = 0F;

                    float xVelocityRand = 0F;
                    float zVelocityRand = 0F;

                    if (facing == Direction.NORTH) {
                        xOffset = 0.5F;
                        yOffset = 0.5F;
                        zOffset = -0.01F;
                        xOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        yOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        zVelocityRand = -random.nextFloat();
                    } else if (facing == Direction.SOUTH) {
                        xOffset = 0.5F;
                        yOffset = 0.5F;
                        zOffset = 1.01F;
                        xOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        yOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        zVelocityRand = random.nextFloat();
                    } else if (facing == Direction.EAST) {
                        xOffset = 1.01F;
                        yOffset = 0.5F;
                        zOffset = 0.5F;
                        yOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        zOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        xVelocityRand = random.nextFloat();
                    } else if (facing == Direction.WEST) {
                        xOffset = -.01F;
                        yOffset = 0.5F;
                        zOffset = 0.5F;
                        yOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        zOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        xVelocityRand = -random.nextFloat();
                    } else if (facing == Direction.UP) {
                        xOffset = 0.5F;
                        yOffset = 1.01F;
                        zOffset = 0.5F;
                        xOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                        yOffsetRand = (random.nextFloat() * 0.25F);
                        zOffsetRand = (random.nextFloat() - random.nextFloat()) * 0.3F;
                    }

                    world.addParticle(ParticleTypes.BUBBLE, pos.getX() + xOffset + xOffsetRand, pos.getY() + yOffset + yOffsetRand, pos.getZ() + zOffset + zOffsetRand, xVelocityRand, 0.05F + random.nextFloat() * 0.05F, zVelocityRand);
                }
                // Play sound
                if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED) {
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.3F + (random.nextFloat() * 0.1F), 1.3F + (random.nextFloat() * 0.3F), false);
                }
            }
        }
    }

}

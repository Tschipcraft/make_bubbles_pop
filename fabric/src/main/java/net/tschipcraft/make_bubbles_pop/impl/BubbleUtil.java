package net.tschipcraft.make_bubbles_pop.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

@Environment(EnvType.CLIENT)
public class BubbleUtil {

    private BubbleUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Add a bubble pop particle and sound at the specified location in the world.
     */
    public static void popBubble(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
            world.addParticle(ParticleTypes.BUBBLE_POP, x, y, z,
                    MakeBubblesPop.getConfigInitialVelocity(velocityX),
                    MakeBubblesPop.getConfigInitialVelocity(velocityY),
                    MakeBubblesPop.getConfigInitialVelocity(velocityZ)
            );
            world.playSound(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, (MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BUBBLE_POP_VOLUME : 0.1F) - (world.random.nextFloat() * 0.1F), 0.85F + (world.random.nextFloat() * 0.3F), false);
        }
    }

    /**
     * Tint the calling particle based on the water color.
     */
    public static void tintBubble(World world, double x, double y, double z, SpriteBillboardParticle particle) {
        if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BIOME_COLORS_ENABLED) {
            // Tint bubbles based on the water color
            int waterColor = BiomeColors.getWaterColor(world, BlockPos.ofFloored(x, y, z));

            float intensity = (MakeBubblesPop.MIDNIGHTLIB_INSTALLED ? MakeBubblesPopConfig.BIOME_COLOR_INTENSITY : 0.65F);
            float whiteMix = 1F - intensity;
            float cr = whiteMix + (intensity) * ((waterColor >> 16 & 0xFF) / 255F);
            float cg = whiteMix + (intensity) * ((waterColor >> 8 & 0xFF) / 255F);
            float cb = whiteMix + (intensity) * ((waterColor & 0xFF) / 255F);
            particle.setColor(cr, cg, cb);
        }
    }

}

package net.tschipcraft.make_bubbles_pop.impl;

import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

@OnlyIn(Dist.CLIENT)
public class BubbleUtil {

    private BubbleUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Add a bubble pop particle and sound at the specified location in the world.
     */
    public static void popBubble(Level world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
            world.addParticle(ParticleTypes.BUBBLE_POP, x, y, z,
                    MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? velocityX : 0D,
                    MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? velocityY : 0D,
                    MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY ? velocityZ : 0D
            );
            world.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.AMBIENT, (MakeBubblesPopConfig.BUBBLE_POP_VOLUME) - (world.random.nextFloat() * 0.1F), 0.85F + (world.random.nextFloat() * 0.3F), false);
        }
    }

    /**
     * Tint the calling bubble particle based on the water color.
     */
    public static void tintBubble(Level world, double x, double y, double z, TextureSheetParticle particle) {
        if (MakeBubblesPopConfig.BIOME_COLORS_ENABLED) {
            // Tint bubbles based on the water color
            int waterColor = BiomeColors.getAverageWaterColor(world, BlockPos.containing(x, y, z));

            float intensity = (MakeBubblesPopConfig.BIOME_COLOR_INTENSITY);
            float whiteMix = 1F - intensity;
            float cr = whiteMix + (intensity) * ((waterColor >> 16 & 0xFF) / 255F);
            float cg = whiteMix + (intensity) * ((waterColor >> 8 & 0xFF) / 255F);
            float cb = whiteMix + (intensity) * ((waterColor & 0xFF) / 255F);
            particle.setColor(cr, cg, cb);
        }
    }

}

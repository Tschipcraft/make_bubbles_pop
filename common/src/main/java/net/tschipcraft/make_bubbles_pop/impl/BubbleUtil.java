package net.tschipcraft.make_bubbles_pop.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.mixin.client.BillboardParticleAccessor;

@Environment(EnvType.CLIENT)
public class BubbleUtil {

    private BubbleUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Add a bubble pop particle and sound at the specified location in the world.
     */
    public static void popBubble(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scale) {
        if (MakeBubblesPopConfig.POP_PARTICLE_ENABLED) {
            Particle bubble = MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.BUBBLE_POP, x, y, z,
                    MakeBubblesPopConfig.getInitialVelocity(velocityX),
                    MakeBubblesPopConfig.getInitialVelocity(velocityY),
                    MakeBubblesPopConfig.getInitialVelocity(velocityZ)
            );
            if (bubble != null) {
                ((BillboardParticleAccessor) bubble).setScale(scale * 2F);
            }
            world.playSound(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.AMBIENT, MakeBubblesPopConfig.BUBBLE_POP_VOLUME - (world.random.nextFloat() * 0.1F), 0.85F + (world.random.nextFloat() * 0.3F), false);
        }
    }

    /**
     * Tint the calling particle based on the water color.
     */
    public static void tintBubble(World world, double x, double y, double z, SpriteBillboardParticle particle) {
        if (MakeBubblesPopConfig.BIOME_COLORS_ENABLED) {
            // Tint bubbles based on the water color
            int waterColor = BiomeColors.getWaterColor(world, BlockPos.ofFloored(x, y, z));

            float intensity = MakeBubblesPopConfig.BIOME_COLOR_INTENSITY;
            float whiteMix = 1F - intensity;
            float cr = whiteMix + (intensity) * ((waterColor >> 16 & 0xFF) / 255F);
            float cg = whiteMix + (intensity) * ((waterColor >> 8 & 0xFF) / 255F);
            float cb = whiteMix + (intensity) * ((waterColor & 0xFF) / 255F);
            particle.setColor(cr, cg, cb);
        }
    }

}

package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This mixin injects into the ChestBlockEntity class to add bubbles to opening chests underwater.
 */
@Mixin(ChestBlockEntity.class)
public abstract class ChestBubble {

    @Unique
    private static final List<BlockPos> OPENED_CHESTS = new ArrayList<>();

    @Inject(method = "lidAnimateTick", at = @At("TAIL"))
    private static void makeBubblesPop$clientTick(Level world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
        if (world != null && world.isClientSide && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED) && world.isWaterAt(pos)) {
            ChestType chestType = state.getOptionalValue(BlockStateProperties.CHEST_TYPE).orElse(ChestType.SINGLE);
            Direction facing = state.getOptionalValue(ChestBlock.FACING).orElse(Direction.NORTH);
            Block block = state.getBlock();

            boolean doubleChest = chestType != ChestType.SINGLE;

            if (block instanceof AbstractChestBlock) {
                // Get open factor
                float openFactor = blockEntity.getOpenNess(1);

                if (openFactor > 0) {
                    if(!OPENED_CHESTS.contains(pos)) {
                        // Chest just opened underwater
                        OPENED_CHESTS.add(pos);
                        if (doubleChest) {
                            if (chestType == ChestType.LEFT) {
                                // If the block is a double chest, only the left side plays particles and sound
                                for (int i = 0; i < 15 + world.random.nextInt(20); i++) {
                                    float xOffset = 0F;
                                    float zOffset = 0F;
                                    float xOffsetRand = 0F;
                                    float zOffsetRand = 0F;

                                    if (facing == Direction.NORTH) {
                                        xOffset = 1F;
                                        zOffset = 0.5F;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.8F;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.3F;
                                    } else if (facing == Direction.SOUTH) {
                                        xOffset = 0F;
                                        zOffset = 0.5F;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.8F;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.3F;
                                    } else if (facing == Direction.EAST) {
                                        xOffset = 0.5F;
                                        zOffset = 1F;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.3F;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.8F;
                                    } else if (facing == Direction.WEST) {
                                        xOffset = 0.5F;
                                        zOffset = 0F;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.3F;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * 0.8F;
                                    }

                                    world.addParticle(ParticleTypes.BUBBLE, pos.getX() + xOffset + xOffsetRand, pos.getY() + 0.7F - (world.random.nextFloat() / 2F), pos.getZ() + zOffset + zOffsetRand, 0F, 0.05F + world.random.nextFloat() * 0.05F, 0F);
                                }
                            }
                        } else {
                            // Single chest
                            for (int i = 0; i < 7 + world.random.nextInt(10); i++) {
                                world.addParticle(ParticleTypes.BUBBLE, pos.getX() + 0.5F + (world.random.nextFloat() - world.random.nextFloat()) * 0.3F, pos.getY() + 0.7F - (world.random.nextFloat() / 2F), pos.getZ() + 0.5F + (world.random.nextFloat() - world.random.nextFloat()) * 0.3F, 0F, 0.05F + world.random.nextFloat() * 0.05F, 0F);
                            }
                        }
                        // Play sound
                        if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED) {
                            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.AMBIENT, 0.3F + (world.random.nextFloat() * 0.1F), 1.3F + (world.random.nextFloat() * 0.3F), false);
                        }
                    }
                } else {
                    // Chest closed
                    OPENED_CHESTS.remove(pos);
                }
            }
        }
    }

}

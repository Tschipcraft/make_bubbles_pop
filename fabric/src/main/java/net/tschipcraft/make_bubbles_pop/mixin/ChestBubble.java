package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
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
    private static final List<BlockPos> openedChests = new ArrayList<>();

    @Inject(method = "clientTick", at = @At("TAIL"))
    private static void clientTick(World world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
        boolean bl = world != null;
        if (bl && world.isClient && MakeBubblesPop.CHEST_BUBBLES_ENABLED && world.isWater(pos)) {
            BlockState blockState = blockEntity.getCachedState();
            ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
            Direction facing = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING) : Direction.NORTH;
            Block block = blockState.getBlock();

            boolean doubleChest = chestType != ChestType.SINGLE;

            if (block instanceof AbstractChestBlock) {
                // Get open factor
                AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock) block;

                DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource;
                propertySource = abstractChestBlock.getBlockEntitySource(blockState, world, pos, true);

                float openFactor = propertySource.apply(ChestBlock.getAnimationProgressRetriever(blockEntity)).get(1.0f);

                if (openFactor > 0) {
                    if(!openedChests.contains(pos)) {
                        // Chest just opened underwater
                        openedChests.add(pos);
                        if (doubleChest) {
                            if (chestType == ChestType.LEFT) {
                                // If the block is a double chest, only the left side plays particles and sound
                                for (int i = 0; i < 15 + world.random.nextInt(20); i++) {
                                    float xOffset = 0f;
                                    float zOffset = 0f;
                                    float xOffsetRand = 0f;
                                    float zOffsetRand = 0f;

                                    if (facing == Direction.NORTH) {
                                        xOffset = 1f;
                                        zOffset = .5f;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                                    } else if (facing == Direction.SOUTH) {
                                        xOffset = 0f;
                                        zOffset = .5f;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                                    } else if (facing == Direction.EAST) {
                                        xOffset = .5f;
                                        zOffset = 1f;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
                                    } else if (facing == Direction.WEST) {
                                        xOffset = .5f;
                                        zOffset = 0f;
                                        xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
                                        zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
                                    }

                                    world.addParticle(ParticleTypes.BUBBLE, pos.getX() + xOffset + xOffsetRand, pos.getY() + .7F - (world.random.nextFloat() / 2.0), pos.getZ() + zOffset + zOffsetRand, 0F, .05f + world.random.nextFloat() * .05f, 0F);
                                }
                                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.5f, 1.4f, false);
                            }
                        } else {
                            // Single chest
                            for (int i = 0; i < 7 + world.random.nextInt(10); i++) {
                                world.addParticle(ParticleTypes.BUBBLE, pos.getX() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, pos.getY() + .7F - (world.random.nextFloat() / 2.0), pos.getZ() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, 0F, .05f + world.random.nextFloat() * .05f, 0F);
                            }
                            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.3f, 1.4f, false);
                        }
                    }
                } else {
                    // Chest closed
                    openedChests.remove(pos);
                }
            }
        }
    }
}

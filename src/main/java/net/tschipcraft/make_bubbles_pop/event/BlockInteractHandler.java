package net.tschipcraft.make_bubbles_pop.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInteractHandler implements UseBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos blockPos = hitResult.getBlockPos();

        if (hitResult.getType() == HitResult.Type.BLOCK && !player.isSpectator() && !player.isSneaking()) {

            if (world.getBlockState(blockPos).getBlock() instanceof ChestBlock) {
                // A chest block was right-clicked
                if (!ChestBlock.isChestBlocked(world, blockPos) && world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    // The chest block is waterlogged and not blocked -> show bubbles + play sound
                    for (int i = (int)(Math.random() * 5) + 5; i >= 0; i--) {
                        world.addParticle(ParticleTypes.BUBBLE, blockPos.getX() + 0.5 + ((0.5 - Math.random())/2.0), blockPos.getY() + 0.7 - (Math.random()/2.0), blockPos.getZ() + 0.5 + ((0.5 - Math.random())/2.0), 0f, 0.1f, 0f);
                        world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.1f, 1.4f, false);
                    }

                }
            }

            /*
            if (world.getBlockState(blockPos).getBlock() instanceof BarrelBlock) {
                //TODO figure out how to retrieve FACING value (BarrelBlock.FACING)
                if (world.getFluidState(blockPos.up()).isIn(FluidTags.WATER)) {
                    for (int i = (int)(Math.random() * 5) + 5; i >= 0; i--) {
                        world.addParticle(ParticleTypes.BUBBLE, blockPos.getX() + 1.0 + ((0.5 - Math.random())/2.0), blockPos.getY() + 0.7 - (Math.random()/2.0), blockPos.getZ() + 0.5 + ((0.5 - Math.random())/2.0), 0f, 0.1f, 0f);
                        world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.AMBIENT, 0.1f, 1.4f, false);
                    }

                }
            }
             */
        }

        return ActionResult.PASS;
    }
}

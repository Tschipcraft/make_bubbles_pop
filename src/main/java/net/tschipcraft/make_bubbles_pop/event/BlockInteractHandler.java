package net.tschipcraft.make_bubbles_pop.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.tag.FluidTags;
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

        if (hitResult.getType() == HitResult.Type.BLOCK && !player.isSneaking() && !player.isSpectator()) {

            if (world.getBlockState(blockPos).getBlock() instanceof ChestBlock) {
                if (!ChestBlock.isChestBlocked(world, blockPos) && world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    for (int i = (int)(Math.random() * 5) + 5; i >= 0; i--) {
                        world.addParticle(ParticleTypes.BUBBLE, blockPos.getX() + Math.random(), blockPos.getY() + 0.7 - (Math.random()/3.0), blockPos.getZ() + Math.random(), 0f, 0.1f, 0f);
                    }

                }
            }
        }

        return ActionResult.PASS;
    }
}

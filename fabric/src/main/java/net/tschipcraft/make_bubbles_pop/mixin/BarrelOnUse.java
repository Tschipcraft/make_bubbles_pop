package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBubbler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin injects into the BarrelBlock class to add bubbles to opening barrels underwater.
 */
@Mixin(BarrelBlock.class)
public abstract class BarrelOnUse {

    @Inject(method = "onUse", at = @At("HEAD"))
    public void injectBubbles(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        boolean bl = world != null;
        if (bl && world.isClient && MakeBubblesPop.BARREL_BUBBLES_ENABLED && !MakeBubblesPop.BARREL_BUBBLES_CREATED_FROM_SERVER) {
            // Get direction of barrel block and test if its underwater
            Direction facing = state.contains(BarrelBlock.FACING) ? state.get(BarrelBlock.FACING) : Direction.NORTH;
            if (world.isWater(pos.offset(facing)) && !state.get(BarrelBlock.OPEN)) {
                // A barrel block has been opened underwater by the current player
                BarrelBubbler.spawnBubbles(world, pos, facing);
            }
        }
    }
}

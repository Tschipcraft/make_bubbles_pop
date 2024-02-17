package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBubbler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * This mixin injects into the BarrelBlock class to add bubbles to opening barrels underwater.
 */
@Mixin(BarrelBlock.class)
public abstract class BarrelOnUse extends BlockWithEntity {

    @Unique
    private static final List<BlockPos> openedBarrels = new ArrayList<>();

    protected BarrelOnUse(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUse", at = @At("HEAD"))
    public void injectBubbles(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        /*
        if (world != null && world.isClient && MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED && !MakeBubblesPop.BARREL_BUBBLES_CREATED_FROM_SERVER) {
            // Get direction of barrel block and test if its underwater
            Direction facing = state.contains(BarrelBlock.FACING) ? state.get(BarrelBlock.FACING) : Direction.NORTH;
            if (world.isWater(pos.offset(facing)) && !state.get(BarrelBlock.OPEN)) {
                // A barrel block has been opened underwater by the current player
                BarrelBubbler.spawnBubbles(world, pos, facing);
            }
        }
         */
    }

    // Experimental - register BarrelBlock to tick on the client instead of using packets
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, BlockEntityType.BARREL, this::clientTick) : null;
    }

    @Unique
    public void clientTick(World world, BlockPos pos, BlockState state, BarrelBlockEntity blockEntity) {
        if (world != null && world.isClient && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)) {
            // Get direction of barrel block and test if its underwater
            Direction facing = state.contains(BarrelBlock.FACING) ? state.get(BarrelBlock.FACING) : Direction.NORTH;
            if (world.isWater(pos.offset(facing)) && state.get(BarrelBlock.OPEN)) {
                if(!openedBarrels.contains(pos)) {
                    // A barrel block has been opened underwater
                    openedBarrels.add(pos);
                    BarrelBubbler.spawnBubbles(world, pos, facing);
                }
            } else {
                // Barrel block closed
                openedBarrels.remove(pos);
            }
        }
    }

}

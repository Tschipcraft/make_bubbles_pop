package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBubbler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This mixin injects into the BarrelBlock class to add bubbles to opening barrels underwater.
 */
@Mixin(BarrelBlock.class)
public abstract class BarrelOnUse {

    @Unique
    private static final List<BlockPos> openedBarrels = new ArrayList<>();

    @Inject(method = "use", at = @At("HEAD"))
    public void injectBubbles(BlockState state, Level world, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit, CallbackInfoReturnable<InteractionResult> cir) {
        /*
        boolean bl = world != null;
        if (bl && world.isClientSide && MakeBubblesPop.BARREL_BUBBLES_ENABLED && !MakeBubblesPop.BARREL_BUBBLES_CREATED_FROM_SERVER) {
            // Get direction of barrel block and test if its underwater
            Direction facing = state.getValues().containsKey(BarrelBlock.FACING) ? state.getValue(BarrelBlock.FACING) : Direction.NORTH;
            if (world.getFluidState(pos.relative(facing)).is(FluidTags.WATER) && !state.getValue(BarrelBlock.OPEN)) {
                // A barrel block has been opened underwater by the current player
                BarrelBubbler.spawnBubbles(world, pos, facing);
            }
        }
         */
    }

    // Experimental - register BarrelBlock to tick on the client
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityType.BARREL ? this::clientTick : null;
    }

    @Unique
    public <T extends BlockEntity> void clientTick(Level world, BlockPos pos, BlockState state, T blockEntity) {
        if (world != null && world.isClientSide && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)) {
            // Get direction of barrel block and test if its underwater
            Direction facing = state.getValues().containsKey(BarrelBlock.FACING) ? state.getValue(BarrelBlock.FACING) : Direction.NORTH;
            if (world.getFluidState(pos.relative(facing)).is(FluidTags.WATER) && state.getValue(BarrelBlock.OPEN)) {
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

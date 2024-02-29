package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBlockEntityInterface;
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
public abstract class BarrelBubble extends BaseEntityBlock {

    @Unique
    private static final List<BlockPos> openedBarrels = new ArrayList<>();

    protected BarrelBubble(Properties pProperties) {
        super(pProperties);
    }

    // Experimental - register BarrelBlock to tick on the client
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityType.BARREL ? this::makeBubblesPop$clientTick : null;
    }

    @Unique
    public <T extends BlockEntity> void makeBubblesPop$clientTick(Level world, BlockPos pos, BlockState state, T blockEntity) {
        if (world != null && world.isClientSide && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)) {
            // Get direction and openness of barrel block
            Direction facing = state.getOptionalValue(BarrelBlock.FACING).orElse(Direction.NORTH);
            boolean open = state.getOptionalValue(BarrelBlock.OPEN).orElse(false);

            if (((BarrelBlockEntityInterface) blockEntity).makeBubblesPop$wasLoaded()) {
                if (world.getFluidState(pos.relative(facing)).is(FluidTags.WATER) && open) {
                    if (!openedBarrels.contains(pos)) {
                        // A barrel block has been opened underwater
                        openedBarrels.add(pos);
                        BarrelBubbler.spawnBubbles(world, pos, facing, world.random);
                    }
                } else {
                    // Barrel block closed
                    openedBarrels.remove(pos);
                }
            } else {
                if (world.getFluidState(pos.relative(facing)).is(FluidTags.WATER) && open) {
                    if (!openedBarrels.contains(pos)) {
                        // Mark barrel as open to prevent it from creating bubbles upon loading if already open
                        openedBarrels.add(pos);
                    }
                }
                ((BarrelBlockEntityInterface) blockEntity).makeBubblesPop$setLoaded(true);
            }
        }
    }

    // Prevent memory leaks
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        openedBarrels.clear();
    }

}

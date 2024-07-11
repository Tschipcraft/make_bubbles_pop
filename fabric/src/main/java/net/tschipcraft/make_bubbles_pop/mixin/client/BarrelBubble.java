package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBlockEntityInterface;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBubbler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This mixin injects into the BarrelBlock class to add bubbles to opening barrels underwater.
 */
@Mixin(BarrelBlock.class)
public abstract class BarrelBubble extends BlockWithEntity {

    @Unique
    private static final List<BlockPos> OPENED_BARRELS = new ArrayList<>();

    protected BarrelBubble(Settings settings) {
        super(settings);
    }

    // Register BarrelBlock to tick on the client instead of using packets
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, BlockEntityType.BARREL, this::makeBubblesPop$clientTick) : null;
    }

    @Unique
    public void makeBubblesPop$clientTick(World world, BlockPos pos, BlockState state, BarrelBlockEntity blockEntity) {
        if (world != null && world.isClient && (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED)) {
            // Get direction and openness of barrel block
            Direction facing = state.getOrEmpty(BarrelBlock.FACING).orElse(Direction.NORTH);
            boolean open = state.getOrEmpty(BarrelBlock.OPEN).orElse(false);

            if (((BarrelBlockEntityInterface) blockEntity).makeBubblesPop$wasLoaded()) {
                if (world.isWater(pos.offset(facing)) && open) {
                    if (!OPENED_BARRELS.contains(pos)) {
                        // A barrel block has been opened underwater
                        OPENED_BARRELS.add(pos);
                        BarrelBubbler.spawnBubbles(world, pos, facing, world.random);
                    }
                } else {
                    // Barrel block closed
                    OPENED_BARRELS.remove(pos);
                }
            } else {
                if (world.isWater(pos.offset(facing)) && open) {
                    if (!OPENED_BARRELS.contains(pos)) {
                        // Mark barrel as open to prevent it from creating bubbles upon loading if already open
                        OPENED_BARRELS.add(pos);
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
        OPENED_BARRELS.clear();
    }

}

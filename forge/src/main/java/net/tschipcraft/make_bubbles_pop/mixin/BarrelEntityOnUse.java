package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin injects into the BarrelBlockEntity class to add bubbles to Barrels opening underwater.
 */
@Mixin(BarrelBlockEntity.class)
public abstract class BarrelEntityOnUse {

    @Inject(method = "updateBlockState", at = @At("HEAD"))
    public void injectBubbles(BlockState state, boolean open, CallbackInfo info) {
        BarrelBlockEntity barrelBlockEntity = BarrelBlockEntity.class.cast(this);
        Level world = barrelBlockEntity.getLevel();
        if (world != null && !world.isClientSide) {
            BlockPos pos = barrelBlockEntity.getBlockPos();
            // Get direction of barrel block and test if its underwater
            Direction facing = state.getValues().containsKey(BarrelBlock.FACING) ? state.getValue(BarrelBlock.FACING) : Direction.NORTH;
            if (world.getFluidState(pos.relative(facing)).is(FluidTags.WATER) && !state.getValue(BarrelBlock.OPEN)) {
                // A barrel block has been opened underwater. Send a packet to all players tracking the barrel block
                //TODO: Figure this out for Forge -> handled in BarrelOnUse now
                /*
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeInt(pos.getX());
                byteBuf.writeInt(pos.getY());
                byteBuf.writeInt(pos.getZ());
                byteBuf.writeString(facing.getName());
                for (ServerPlayerEntity player : PlayerLookup.tracking(barrelBlockEntity)) {
                    ServerPlayNetworking.send(player, MakeBubblesPop.BARREL_BUBBLE_PACKET, byteBuf);
                }
                 */
            }
        }
    }

}

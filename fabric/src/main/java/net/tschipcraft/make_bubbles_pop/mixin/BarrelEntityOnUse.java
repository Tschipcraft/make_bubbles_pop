package net.tschipcraft.make_bubbles_pop.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin injects into the BarrelBlockEntity class to add bubbles to Barrels opening underwater.
 */
@Mixin(BarrelBlockEntity.class)
public abstract class BarrelEntityOnUse extends LootableContainerBlockEntity {

    protected BarrelEntityOnUse(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "setOpen", at = @At("HEAD"))
    public void injectBubbles(BlockState state, boolean open, CallbackInfo info) {
        BarrelBlockEntity barrelBlockEntity = BarrelBlockEntity.class.cast(this);
        World world = barrelBlockEntity.getWorld();
        if (world != null && !world.isClient) {
            BlockPos pos = barrelBlockEntity.getPos();
            // Get direction of barrel block and test if its underwater
            Direction facing = state.contains(BarrelBlock.FACING) ? state.get(BarrelBlock.FACING) : Direction.NORTH;
            if (world.isWater(pos.offset(facing)) && !state.get(BarrelBlock.OPEN)) {
                // A barrel block has been opened underwater. Send a packet to all players tracking the barrel block
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeInt(pos.getX());
                byteBuf.writeInt(pos.getY());
                byteBuf.writeInt(pos.getZ());
                byteBuf.writeString(facing.getName());
                for (ServerPlayerEntity player : PlayerLookup.tracking(barrelBlockEntity)) {
                    ServerPlayNetworking.send(player, MakeBubblesPop.BARREL_BUBBLE_PACKET, byteBuf);
                }
            }
        }
    }

    @Unique
    public void clientTick(World world, BlockPos pos, BlockState state, BarrelBlockEntity blockEntity) {
        // Can't call clientTick here since I can't reference it in the BarrelBlock mixin.
    }

}

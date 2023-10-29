package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBubbler;

public class MakeBubblesPopClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MakeBubblesPop.LOGGER.info("Make Bubbles Pop by Tschipcraft initialized! (Client entrypoint)");
		receiveBarrelBubblePacket();
	}

	private static void receiveBarrelBubblePacket() {
		ClientPlayNetworking.registerGlobalReceiver(MakeBubblesPop.BARREL_BUBBLE_PACKET, (ctx, handler, byteBuf, responseSender) -> {
			BlockPos pos = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
			Direction facing = Direction.byName(byteBuf.readString());
			ctx.execute(() -> {
				if (ctx.world == null)
					throw new IllegalStateException("Your world is null? How?");
				BarrelBubbler.spawnBubbles(ctx.world, pos, facing);
			});
		});
	}

}

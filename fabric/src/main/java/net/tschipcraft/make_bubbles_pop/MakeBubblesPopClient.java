package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ClientModInitializer;

public class MakeBubblesPopClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MakeBubblesPop.LOGGER.info("Make Bubbles Pop by Tschipcraft initialized! (Client entrypoint)");
	}

}

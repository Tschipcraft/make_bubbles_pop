package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.tschipcraft.make_bubbles_pop.event.BlockInteractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPop implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("make_bubbles_pop");

	@Override
	public void onInitialize() {

		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
		// Register chest bubbles event
		UseBlockCallback.EVENT.register(new BlockInteractHandler());
	}
}

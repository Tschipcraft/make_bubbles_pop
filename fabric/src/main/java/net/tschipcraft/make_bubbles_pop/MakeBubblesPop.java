package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPop implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("make_bubbles_pop");

	public static boolean POP_PARTICLE_ENABLED = true;
	public static boolean CHEST_BUBBLES_ENABLED = true;
	public static boolean BARREL_BUBBLES_ENABLED = true;

	//TODO: Make a config for these
	public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = true;

	@Override
	public void onInitialize() {
		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
	}
}

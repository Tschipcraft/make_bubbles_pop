package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPop implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("make_bubbles_pop");

	public static final Identifier BARREL_BUBBLE_PACKET = new Identifier("make_bubbles_pop", "barrel_bubble_packet");
	public static boolean POP_PARTICLE_ENABLED = true;
	public static boolean CHEST_BUBBLES_ENABLED = true;
	public static boolean BARREL_BUBBLES_ENABLED = true;

	//TODO: Make a config for these
	public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = true;
	public static boolean BARREL_BUBBLES_CREATED_FROM_SERVER = true; // To be disabled when on a server without Make Bubbles Pop

	@Override
	public void onInitialize() {
		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
	}
}

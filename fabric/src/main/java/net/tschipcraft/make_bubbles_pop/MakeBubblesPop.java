package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPop implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("make_bubbles_pop");

	public static final Identifier BARREL_BUBBLE_PACKET = new Identifier("make_bubbles_pop", "barrel_bubble_packet");

	public static boolean POP_PARTICLE_ENABLED = true;
	public static boolean CHEST_BUBBLES_ENABLED = true;
	public static boolean BARREL_BUBBLES_ENABLED = true;
	public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = true;
	public static boolean BARREL_BUBBLES_CREATED_FROM_SERVER = false; // To be disabled when on a server without Make Bubbles Pop

	public static boolean MIDNIGHTLIB_INSTALLED = false;

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isModLoaded("midnightlib")) {
			// Use MidnightLib features
			MakeBubblesPopConfig.init(LOGGER.getName(), MakeBubblesPopConfig.class);
			MIDNIGHTLIB_INSTALLED = true;
		}
		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
	}
}

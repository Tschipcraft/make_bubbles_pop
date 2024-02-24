package net.tschipcraft.make_bubbles_pop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPop implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("make_bubbles_pop");

	public static final Identifier BARREL_BUBBLE_PACKET = new Identifier("make_bubbles_pop", "barrel_bubble_packet");

	public static final boolean MIDNIGHTLIB_INSTALLED = FabricLoader.getInstance().isModLoaded("midnightlib");

	@Override
	public void onInitialize() {
		if (MIDNIGHTLIB_INSTALLED) {
			// Use MidnightLib features
			MakeBubblesPopConfig.init(LOGGER.getName(), MakeBubblesPopConfig.class);
		}
		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
	}

	public static double getConfigInitialVelocity(double original) {
		return (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY) ? original : 0D;
	}
}

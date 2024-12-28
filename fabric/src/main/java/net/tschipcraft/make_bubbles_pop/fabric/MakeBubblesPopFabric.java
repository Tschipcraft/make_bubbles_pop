package net.tschipcraft.make_bubbles_pop.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPopFabric implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("make_bubbles_pop");

	public static final boolean MIDNIGHTLIB_INSTALLED = FabricLoader.getInstance().isModLoaded("midnightlib");

	@Override
	public void onInitialize() {
		if (MIDNIGHTLIB_INSTALLED) {
			// Use MidnightLib features
			LOGGER.info("MidnightLib detected!");
			//MakeBubblesPopConfig.init(LOGGER.getName(), MakeBubblesPopConfig.class);
		}
		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
		net.tschipcraft.make_bubbles_pop.MakeBubblesPop.init();
	}

}

package net.tschipcraft.make_bubbles_pop.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeBubblesPopFabric implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(MakeBubblesPop.MOD_ID);

	public static final boolean YACL_INSTALLED = FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");

	@Override
	public void onInitialize() {
		// Call common setup
		MakeBubblesPop.init();

		if (YACL_INSTALLED) {
			LOGGER.info("Yet Another Config Lib detected! Using YACL config system.");
			MakeBubblesPopFabricConfig.HANDLER.instance().load();
		}

		LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
	}

}

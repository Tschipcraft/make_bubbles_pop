package net.tschipcraft.make_bubbles_pop.platform.fabric;

//? fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.config.ConfigScreens;
import net.tschipcraft.make_bubbles_pop.config.ConfigStorage;

public class FabricClientEntrypoint implements ClientModInitializer {

	private static ConfigStorage config;

	public static ConfigStorage config() {
		return config;
	}

	// The config library is an optional dependency, so the screen is only offered when it is present.
	public static boolean configLibraryPresent() {
		return FabricLoader.getInstance().isModLoaded(ConfigScreens.LIBRARY_MOD_ID);
	}

	@Override
	public void onInitializeClient() {
		config = new ConfigStorage(
				FabricLoader.getInstance().getConfigDir().resolve(MakeBubblesPop.MOD_ID + ".json"));
		config.load();

		MakeBubblesPop.init();
	}

}
//?}

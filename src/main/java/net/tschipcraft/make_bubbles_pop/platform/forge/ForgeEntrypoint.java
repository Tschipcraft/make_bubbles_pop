package net.tschipcraft.make_bubbles_pop.platform.forge;

// Only line comments in this file: Stonecutter block-comments the whole body on other loaders.
//? forge {
/*import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.config.ConfigScreens;

// 1.19 renamed Forge's config-screen extension point from ConfigGuiHandler to ConfigScreenHandler.
//? >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler;
//?} <1.19 {
import net.minecraftforge.client.ConfigGuiHandler;
//?}

@Mod(MakeBubblesPop.MOD_ID)
public class ForgeEntrypoint {

	public ForgeEntrypoint() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeConfig.SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
	}

	private void onClientSetup(final FMLClientSetupEvent event) {
		// Runs after Forge has loaded the client config, so the spec is ready to be written back to.
		ForgeConfig.migrateLegacyConfig();

		if (ModList.get().isLoaded(ConfigScreens.LIBRARY_MOD_ID)) {
			//? >=1.19 {
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
					() -> new ConfigScreenHandler.ConfigScreenFactory(
							(minecraft, parent) -> ConfigScreens.create(parent, ForgeConfig.PLATFORM)));
			//?} <1.19 {
			ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
					() -> new ConfigGuiHandler.ConfigGuiFactory(
							(minecraft, parent) -> ConfigScreens.create(parent, ForgeConfig.PLATFORM)));
			//?}
		}

		MakeBubblesPop.init();
	}

}
*///?}

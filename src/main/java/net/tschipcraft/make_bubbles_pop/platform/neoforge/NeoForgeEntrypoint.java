package net.tschipcraft.make_bubbles_pop.platform.neoforge;

// Only line comments in this file: Stonecutter block-comments the whole body on other loaders.
//? neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.config.ConfigScreens;

@Mod(value = MakeBubblesPop.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeEntrypoint {

	public NeoForgeEntrypoint(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfig.SPEC);

		if (ModList.get().isLoaded(ConfigScreens.LIBRARY_MOD_ID)) {
			modContainer.registerExtensionPoint(IConfigScreenFactory.class,
					(container, parent) -> ConfigScreens.create(parent, NeoForgeConfig.PLATFORM));
		} else {
			// Fall back to the native NeoForge config screen when YACL is not installed.
			//
			// ConfigurationScreen only exists from NeoForge 21.0.110-beta; declared as
			// `deps.neoforge_range_lower` in stonecutter.properties.toml.
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}

		MakeBubblesPop.init();
	}

}
*///?}

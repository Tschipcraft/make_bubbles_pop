package net.tschipcraft.make_bubbles_pop.platform.fabric;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.tschipcraft.make_bubbles_pop.config.ConfigScreens;
import net.tschipcraft.make_bubbles_pop.impl.Texts;

// 1.21.11 moved Util into net.minecraft.util; 26 renamed Minecraft#setScreen to #setScreenAndShow.
//? <1.21.11 {
/*import net.minecraft.Util;
*///?} >=1.21.11 {
import net.minecraft.util.Util;
//?}

import java.net.URI;

// Mod Menu config integration. Wired up through the `modmenu` entrypoint in the generated
// fabric.mod.json (see build.fabric.gradle.kts).
public class ModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (!FabricClientEntrypoint.configLibraryPresent()) {
			// Config library missing - offer a link to its download page instead of a broken screen.
			return parent -> new ConfirmScreen(
					result -> {
						if (result) {
							Util.getPlatform().openUri(URI.create(ConfigScreens.LIBRARY_URL));
						}
						//? <26 {
						/*Minecraft.getInstance().setScreen(parent);
						*///?} >=26 {
						Minecraft.getInstance().setScreenAndShow(parent);
						//?}
					},
					Texts.translatable("make_bubbles_pop.config_lib.missing", ConfigScreens.LIBRARY_NAME),
					Texts.translatable("make_bubbles_pop.config_lib.missing.description", ConfigScreens.LIBRARY_NAME),
					CommonComponents.GUI_YES,
					CommonComponents.GUI_NO);
		}
		return parent -> ConfigScreens.create(parent, FabricClientEntrypoint.config());
	}

}
//?}

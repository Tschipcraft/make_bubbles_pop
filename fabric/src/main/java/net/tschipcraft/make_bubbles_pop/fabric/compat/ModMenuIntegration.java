package net.tschipcraft.make_bubbles_pop.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.tschipcraft.make_bubbles_pop.config.YACLScreen;
import net.tschipcraft.make_bubbles_pop.fabric.MakeBubblesPopFabric;
import net.tschipcraft.make_bubbles_pop.fabric.MakeBubblesPopFabricConfig;

import java.net.URI;

/**
 * Mod Menu config integration for Make Bubbles Pop. Called from modmenu entrypoint in fabric.mod.json.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!MakeBubblesPopFabric.YACL_INSTALLED) {
            // If YACL is not installed, show an error screen with a link to the modrinth page
            return parent -> new ConfirmScreen(result -> {
                if (result) {
                    Util.getOperatingSystem().open(URI.create("https://modrinth.com/mod/yacl/versions"));
                }
                MinecraftClient.getInstance().setScreen(parent);
            }, Text.translatable("make_bubbles_pop.yacl.missing"), Text.translatable("make_bubbles_pop.yacl.missing.description"), ScreenTexts.YES, ScreenTexts.NO);
        } else {
            return parent -> YACLScreen.getScreen(parent, MakeBubblesPopFabricConfig.HANDLER.instance());
        }
    }

}

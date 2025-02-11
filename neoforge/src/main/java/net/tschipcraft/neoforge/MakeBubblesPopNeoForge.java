package net.tschipcraft.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.tschipcraft.make_bubbles_pop.config.YACLScreen;
import net.tschipcraft.make_bubbles_pop.config.PlatformConfig;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;

import java.util.function.Supplier;

@Mod(value = MakeBubblesPop.MOD_ID, dist = Dist.CLIENT)
public final class MakeBubblesPopNeoForge {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final boolean YACL_INSTALLED = ModList.get().isLoaded("yet_another_config_lib_v3");

    public MakeBubblesPopNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Call common setup
        MakeBubblesPop.init();

        // Register config
        modContainer.registerConfig(ModConfig.Type.CLIENT, MakeBubblesPopNeoForgeConfig.SPEC);
        if (YACL_INSTALLED) {
            LOGGER.info("Yet Another Config Lib detected! Using YACL config screen.");
            modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                    (Supplier<IConfigScreenFactory>) () -> (client, parent) ->
                            YACLScreen.getScreen(parent, new PlatformConfig() {
                                @Override
                                public void load() {
                                    // Unnecessary, as NeoForge handles this
                                }
                                @Override
                                public void save() {
                                    MakeBubblesPopNeoForgeConfig.save();
                                }
                            })
            );
        } else {
            LOGGER.info("Yet Another Config Lib not detected! Using native NeoForge config screen.");
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
    }
}

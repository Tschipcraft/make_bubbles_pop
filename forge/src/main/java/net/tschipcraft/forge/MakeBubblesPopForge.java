package net.tschipcraft.forge;

import com.mojang.logging.LogUtils;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.config.ClothScreen;
import net.tschipcraft.make_bubbles_pop.config.LegacyConfig;
import net.tschipcraft.make_bubbles_pop.config.PlatformConfig;
import org.slf4j.Logger;

@Mod(MakeBubblesPop.MOD_ID)
public class MakeBubblesPopForge {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean CLOTHCONFIG_INSTALLED = ModList.get().isLoaded("cloth_config");


    public MakeBubblesPopForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        modEventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MakeBubblesPopForgeConfig.SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Call common setup
        MakeBubblesPop.init();

        if (CLOTHCONFIG_INSTALLED) {
            // Use Cloth Config features
            LOGGER.info("Cloth Config detected! Using Cloth Config screen.");
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                    new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ClothScreen.getScreen(parent, new PlatformConfig() {
                        @Override
                        public void load() {
                            // Unnecessary, as Forge handles this
                        }

                        @Override
                        public void save() {
                            MakeBubblesPopForgeConfig.save();
                        }
                    })));
        }

        // Check for legacy config file
        LegacyConfig.loadLegacyConfig(LOGGER, FMLPaths.CONFIGDIR.get().resolve("make_bubbles_pop.json"), new PlatformConfig() {
            @Override
            public void load() {
                // Unnecessary, as Forge handles this
            }
            @Override
            public void save() {
                MakeBubblesPopForgeConfig.save();
            }
        });

        LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
    }

}

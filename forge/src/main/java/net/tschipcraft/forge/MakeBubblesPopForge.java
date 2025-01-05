package net.tschipcraft.forge;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import org.slf4j.Logger;

@Mod(MakeBubblesPop.MOD_ID)
public class MakeBubblesPopForge {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean MIDNIGHTLIB_INSTALLED = ModList.get().isLoaded("midnightlib");


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

        // Config screen code for later use
        //ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> {
        //    return new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
        //        <config screen>
        //    });
        //});

        LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
    }

}

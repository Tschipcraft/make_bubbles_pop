package net.tschipcraft.make_bubbles_pop;

import com.mojang.logging.LogUtils;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MakeBubblesPop.MODID)
public class MakeBubblesPop {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "make_bubbles_pop";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    //TODO: Find equivalent in Forge -> client ticking for barrel block
    //public static final Identifier BARREL_BUBBLE_PACKET = new Identifier("make_bubbles_pop", "barrel_bubble_packet");
    public static boolean POP_PARTICLE_ENABLED = true;
    public static boolean CHEST_BUBBLES_ENABLED = true;
    public static boolean BARREL_BUBBLES_ENABLED = true;

    //TODO: Make a config for these
    public static boolean POPPED_BUBBLES_MAINTAIN_VELOCITY = true;
    public static boolean BARREL_BUBBLES_CREATED_FROM_SERVER = false; // To be disabled when on a server without Make Bubbles Pop


    public static boolean MIDNIGHTLIB_INSTALLED = false;


    public MakeBubblesPop() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        modEventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("midnightlib")) {
            // Use MidnightLib features
            LOGGER.info("Detected midnightlib!");
            MakeBubblesPopConfig.init(MODID, MakeBubblesPopConfig.class);
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> {
                return new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
                    return MidnightConfig.getScreen(parent, MODID);
                });
            });
            MIDNIGHTLIB_INSTALLED = true;
        }
        LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
    }

}

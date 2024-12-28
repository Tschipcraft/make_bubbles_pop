package net.tschipcraft.neoforge;

import net.neoforged.api.distmarker.Dist;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;

@Mod(value = MakeBubblesPop.MOD_ID, dist = Dist.CLIENT)
public final class MakeBubblesPopNeoForge {

    private static final Logger LOGGER = LogUtils.getLogger();

    public MakeBubblesPopNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Call common setup
        MakeBubblesPop.init();

        LOGGER.info("Make Bubbles Pop by Tschipcraft initialized!");
    }
}

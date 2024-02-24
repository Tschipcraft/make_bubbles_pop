package net.tschipcraft.make_bubbles_pop.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface BarrelBlockEntityInterface {

    boolean makeBubblesPop$wasLoaded();

    void makeBubblesPop$setLoaded(boolean wasLoaded);
}

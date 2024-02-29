package net.tschipcraft.make_bubbles_pop.impl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface BarrelBlockEntityInterface {

    boolean makeBubblesPop$wasLoaded();

    void makeBubblesPop$setLoaded(boolean wasLoaded);

}

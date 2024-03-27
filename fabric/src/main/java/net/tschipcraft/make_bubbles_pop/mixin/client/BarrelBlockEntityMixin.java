package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBlockEntityInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BarrelBlockEntity.class)
public abstract class BarrelBlockEntityMixin implements BarrelBlockEntityInterface {

    @Unique
    private boolean wasLoaded = false; // This will prevent bubbles from spawning when a chunk with an open barrel is loaded

    @Unique
    @Override
    public boolean makeBubblesPop$wasLoaded() {
        return this.wasLoaded;
    }

    @Unique
    @Override
    public void makeBubblesPop$setLoaded(boolean wasLoaded) {
        this.wasLoaded = wasLoaded;
    }

}

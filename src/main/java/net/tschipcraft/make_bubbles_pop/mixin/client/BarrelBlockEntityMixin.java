package net.tschipcraft.make_bubbles_pop.mixin.client;

import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.tschipcraft.make_bubbles_pop.impl.BarrelBlockEntityInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * This mixin injects into the BarrelBlockEntity class to add a flag that tracks whether a barrel
 * has been loaded. Used to prevent bubbles from spawning when a chunk containing an already open
 * barrel is loaded.
 */
@Mixin(BarrelBlockEntity.class)
public abstract class BarrelBlockEntityMixin implements BarrelBlockEntityInterface {

	@Unique
	private boolean wasLoaded = false;

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

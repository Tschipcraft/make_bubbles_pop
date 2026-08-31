package net.tschipcraft.make_bubbles_pop.impl;

/**
 * Duck interface implemented onto {@code BarrelBlockEntity} by
 * {@code net.tschipcraft.make_bubbles_pop.mixin.client.BarrelBlockEntityMixin}.
 */
public interface BarrelBlockEntityInterface {

	boolean makeBubblesPop$wasLoaded();

	void makeBubblesPop$setLoaded(boolean wasLoaded);

}

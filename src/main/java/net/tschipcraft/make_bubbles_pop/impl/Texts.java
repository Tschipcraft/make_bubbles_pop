package net.tschipcraft.make_bubbles_pop.impl;

import net.minecraft.network.chat.Component;

//? <1.19 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///?}

/**
 * Text construction helpers.
 *
 * <p>1.19 replaced the {@code TextComponent} / {@code TranslatableComponent} constructors with the
 * {@code Component.literal} / {@code Component.translatable} factories. Routing every call site
 * through here keeps that break in one place.
 */
public final class Texts {

	private Texts() {
		throw new IllegalStateException("Utility class");
	}

	public static Component translatable(String key, Object... args) {
		//? >=1.19 {
		return Component.translatable(key, args);
		//?} <1.19 {
		/*return new TranslatableComponent(key, args);
		*///?}
	}

	public static Component literal(String text) {
		//? >=1.19 {
		return Component.literal(text);
		//?} <1.19 {
		/*return new TextComponent(text);
		*///?}
	}

}

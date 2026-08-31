package net.tschipcraft.make_bubbles_pop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader-agnostic entry point. Every platform entrypoint goes through {@link #init()} after it
 * has initialized its own config storage.
 */
public final class MakeBubblesPop {

	public static final String MOD_ID = /*$ mod_id*/ "make_bubbles_pop";
	public static final String MOD_NAME = /*$ mod_name*/ "Make Bubbles Pop";
	public static final String MOD_VERSION = /*$ mod_version*/ "0.4.0";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private MakeBubblesPop() {
		throw new IllegalStateException("Utility class");
	}

	public static void init() {
		LOGGER.info("{} v{} by Tschipcraft initialized!", MOD_NAME, MOD_VERSION);
	}

}

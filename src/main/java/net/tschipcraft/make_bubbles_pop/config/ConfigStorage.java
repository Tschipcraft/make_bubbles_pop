package net.tschipcraft.make_bubbles_pop.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain Gson-backed config file, used by the Fabric platform on every supported Minecraft version.
 *
 * <p>Deliberately does not use YACL's {@code ConfigClassHandler}: YACL 3 has no builds below
 * 1.20.1, so tying storage to it would leave the oldest targets without a config file at all.
 * Keeping storage here means YACL is only ever a screen provider, and the file format is identical
 * across the whole 1.18.2-26.2 range.
 *
 * <p>The keys match the ones MidnightLib wrote in v0.3.x, so an existing config file is picked up
 * as-is and no migration step is needed.
 */
public final class ConfigStorage implements PlatformConfig {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Path path;

	public ConfigStorage(Path path) {
		this.path = path;
	}

	@Override
	public void load() {
		if (!Files.exists(this.path)) {
			// Nothing stored yet - write the defaults so there is a file to edit by hand.
			save();
			return;
		}

		JsonObject json;
		try (Reader reader = Files.newBufferedReader(this.path)) {
			json = GSON.fromJson(reader, JsonObject.class);
		} catch (IOException e) {
			MakeBubblesPop.LOGGER.error("Failed to read config file {}", this.path, e);
			return;
		} catch (RuntimeException e) {
			MakeBubblesPop.LOGGER.error("Failed to parse config file {} - keeping defaults", this.path, e);
			return;
		}
		if (json == null) {
			return;
		}

		MakeBubblesPopConfig.POP_PARTICLE_ENABLED = bool(json, "POP_PARTICLE_ENABLED", MakeBubblesPopConfig.POP_PARTICLE_ENABLED);
		MakeBubblesPopConfig.BUBBLE_POP_VOLUME = number(json, "BUBBLE_POP_VOLUME", MakeBubblesPopConfig.BUBBLE_POP_VOLUME).floatValue();
		MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = bool(json, "POPPED_BUBBLES_MAINTAIN_VELOCITY", MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY);
		MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = bool(json, "BUBBLE_PHYSICS_ENABLED", MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED);
		MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = number(json, "BUBBLE_LIFETIME_MULTIPLIER", MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER).doubleValue();
		MakeBubblesPopConfig.BIOME_COLORS_ENABLED = bool(json, "BIOME_COLORS_ENABLED", MakeBubblesPopConfig.BIOME_COLORS_ENABLED);
		MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = number(json, "BIOME_COLOR_INTENSITY", MakeBubblesPopConfig.BIOME_COLOR_INTENSITY).floatValue();
		MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = bool(json, "CHEST_BUBBLES_ENABLED", MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED);
		MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = bool(json, "BARREL_BUBBLES_ENABLED", MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED);
		MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = bool(json, "CONTAINER_SOUND_ENABLED", MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED);
		MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = bool(json, "EXPLOSION_BUBBLES_ENABLED", MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED);
	}

	@Override
	public void save() {
		JsonObject json = new JsonObject();
		json.addProperty("POP_PARTICLE_ENABLED", MakeBubblesPopConfig.POP_PARTICLE_ENABLED);
		json.addProperty("BUBBLE_POP_VOLUME", MakeBubblesPopConfig.BUBBLE_POP_VOLUME);
		json.addProperty("POPPED_BUBBLES_MAINTAIN_VELOCITY", MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY);
		json.addProperty("BUBBLE_PHYSICS_ENABLED", MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED);
		json.addProperty("BUBBLE_LIFETIME_MULTIPLIER", MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER);
		json.addProperty("BIOME_COLORS_ENABLED", MakeBubblesPopConfig.BIOME_COLORS_ENABLED);
		json.addProperty("BIOME_COLOR_INTENSITY", MakeBubblesPopConfig.BIOME_COLOR_INTENSITY);
		json.addProperty("CHEST_BUBBLES_ENABLED", MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED);
		json.addProperty("BARREL_BUBBLES_ENABLED", MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED);
		json.addProperty("CONTAINER_SOUND_ENABLED", MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED);
		json.addProperty("EXPLOSION_BUBBLES_ENABLED", MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED);

		try {
			Path parent = this.path.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			try (Writer writer = Files.newBufferedWriter(this.path)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException e) {
			MakeBubblesPop.LOGGER.error("Failed to write config file {}", this.path, e);
		}
	}

	private static boolean bool(JsonObject json, String key, boolean fallback) {
		JsonElement element = json.get(key);
		if (element == null || element.isJsonNull()) {
			return fallback;
		}
		try {
			return element.getAsBoolean();
		} catch (RuntimeException e) {
			MakeBubblesPop.LOGGER.warn("Ignoring malformed config entry '{}'", key);
			return fallback;
		}
	}

	private static Number number(JsonObject json, String key, Number fallback) {
		JsonElement element = json.get(key);
		if (element == null || element.isJsonNull()) {
			return fallback;
		}
		try {
			return element.getAsNumber();
		} catch (RuntimeException e) {
			MakeBubblesPop.LOGGER.warn("Ignoring malformed config entry '{}'", key);
			return fallback;
		}
	}

}

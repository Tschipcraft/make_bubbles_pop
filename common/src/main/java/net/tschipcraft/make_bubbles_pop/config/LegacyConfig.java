package net.tschipcraft.make_bubbles_pop.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class LegacyConfig {

    private LegacyConfig() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Loads the legacy configuration file and migrates its settings to the new configuration system.
     *
     * @param LOGGER           the logger to use for logging messages
     * @param legacyConfigPath the path to the legacy configuration file
     * @param config           the new platform configuration instance to migrate settings to
     */
    public static void loadLegacyConfig(Logger LOGGER, Path legacyConfigPath, PlatformConfig config) {
        File configFile = legacyConfigPath.toFile();
        if (!configFile.exists()) {
            return;
        }

        LOGGER.info("Legacy config file found. Migrating to new config system...");

        try (FileReader reader = new FileReader(configFile)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            MakeBubblesPopConfig.POP_PARTICLE_ENABLED = jsonObject.get("POP_PARTICLE_ENABLED").getAsBoolean();
            MakeBubblesPopConfig.BUBBLE_POP_VOLUME = jsonObject.get("BUBBLE_POP_VOLUME").getAsFloat();
            MakeBubblesPopConfig.POPPED_BUBBLES_MAINTAIN_VELOCITY = jsonObject.get("POPPED_BUBBLES_MAINTAIN_VELOCITY").getAsBoolean();
            MakeBubblesPopConfig.BUBBLE_PHYSICS_ENABLED = jsonObject.get("BUBBLE_PHYSICS_ENABLED").getAsBoolean();
            MakeBubblesPopConfig.BUBBLE_LIFETIME_MULTIPLIER = jsonObject.get("BUBBLE_LIFETIME_MULTIPLIER").getAsDouble();
            MakeBubblesPopConfig.BIOME_COLORS_ENABLED = jsonObject.get("BIOME_COLORS_ENABLED").getAsBoolean();
            MakeBubblesPopConfig.BIOME_COLOR_INTENSITY = jsonObject.get("BIOME_COLOR_INTENSITY").getAsFloat();
            MakeBubblesPopConfig.CHEST_BUBBLES_ENABLED = jsonObject.get("CHEST_BUBBLES_ENABLED").getAsBoolean();
            MakeBubblesPopConfig.BARREL_BUBBLES_ENABLED = jsonObject.get("BARREL_BUBBLES_ENABLED").getAsBoolean();
            MakeBubblesPopConfig.CONTAINER_SOUND_ENABLED = jsonObject.get("CONTAINER_SOUND_ENABLED").getAsBoolean();
            MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED = jsonObject.get("EXPLOSION_BUBBLES_ENABLED").getAsBoolean();

            config.save();
        } catch (IOException e) {
            LOGGER.error("Failed to open legacy config file.");
        } catch (Exception e) {
            LOGGER.error("Failed to parse legacy config file.");
            e.printStackTrace();
        }

        // Delete legacy config file
        if (!configFile.delete()) {
            LOGGER.error("Failed to delete the legacy config file. Please remove it manually.");
        }
    }

}

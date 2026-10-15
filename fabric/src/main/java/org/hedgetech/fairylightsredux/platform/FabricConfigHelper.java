package org.hedgetech.fairylightsredux.platform;

import net.fabricmc.loader.api.FabricLoader;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.platform.services.IConfigHelper;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class FabricConfigHelper implements IConfigHelper {
    private final Path path = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID + ".properties");
    private final boolean christmasJingles;
    private final boolean halloweenJingles;
    private final int jingleAmplitude;
    private String tutorialProgress;

    public FabricConfigHelper() {
        final Properties properties = new Properties();
        final boolean exists = Files.exists(this.path);

        if (exists) {
            try (final Reader reader = Files.newBufferedReader(this.path)) {
                properties.load(reader);
            } catch (final IOException e) {
                Constants.LOG.warn("Could not read {}, using default config", this.path, e);
            }
        }

        this.christmasJingles = readBoolean(properties, CHRISTMAS_JINGLES_KEY, DEFAULT_CHRISTMAS_JINGLES);
        this.halloweenJingles = readBoolean(properties, HALLOWEEN_JINGLES_KEY, DEFAULT_HALLOWEEN_JINGLES);
        this.jingleAmplitude = readAmplitude(properties);
        this.tutorialProgress = properties.getProperty(TUTORIAL_PROGRESS_KEY, DEFAULT_TUTORIAL_PROGRESS).trim();

        if (!exists) {
            this.save();
        }
    }

    @Override
    public boolean christmasJingles() {
        return this.christmasJingles;
    }

    @Override
    public boolean halloweenJingles() {
        return this.halloweenJingles;
    }

    @Override
    public int jingleAmplitude() {
        return this.jingleAmplitude;
    }

    @Override
    public String tutorialProgress() {
        return this.tutorialProgress;
    }

    @Override
    public void setTutorialProgress(final String progress) {
        this.tutorialProgress = progress;
        this.save();
    }

    private void save() {
        try {
            Files.createDirectories(this.path.getParent());
            Files.write(this.path, List.of(
                    "# If true, redstone-triggered jingles play Christmas songs from December 24 to 26.",
                    CHRISTMAS_JINGLES_KEY + "=" + this.christmasJingles,
                    "# If true, redstone-triggered jingles play Halloween songs from October 30 to November 1.",
                    HALLOWEEN_JINGLES_KEY + "=" + this.halloweenJingles,
                    "# The distance that jingles can be heard, in blocks, at least 1.",
                    JINGLE_AMPLITUDE_KEY + "=" + this.jingleAmplitude,
                    "# The hanging lights tutorial progress. Once any light enters the inventory a toast appears",
                    "# prompting to craft hanging lights. A finished tutorial is 'complete' and an unstarted one is 'none'.",
                    TUTORIAL_PROGRESS_KEY + "=" + this.tutorialProgress
            ));
        } catch (final IOException e) {
            Constants.LOG.warn("Could not write config to {}", this.path, e);
        }
    }

    private static boolean readBoolean(final Properties properties, final String key, final boolean defaultValue) {
        final String value = properties.getProperty(key, String.valueOf(defaultValue)).trim();

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        }

        Constants.LOG.warn("Invalid boolean value for '{}' for {} in the config, using {}", value, key, defaultValue);
        return defaultValue;
    }

    private static int readInteger(final Properties properties, final String key, final int defaultValue) {
        final String value = properties.getProperty(key, String.valueOf(defaultValue)).trim();

        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException ignored) {}

        Constants.LOG.warn("Invalid value '{}' for {} in the config, using {}", value, key, defaultValue);
        return defaultValue;
    }

    private static int readAmplitude(final Properties properties) {
        final int amplitude = readInteger(properties, JINGLE_AMPLITUDE_KEY, DEFAULT_JINGLE_AMPLITUDE);
        return Math.max(1, amplitude);
    }
}

package org.hedgetech.fairylightsredux.platform;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.hedgetech.fairylightsredux.platform.services.IConfigHelper;

public class NeoForgeConfigHelper implements IConfigHelper {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec CLIENT_SPEC;

    private static final ModConfigSpec.BooleanValue CHRISTMAS_JINGLES;
    private static final ModConfigSpec.BooleanValue HALLOWEEN_JINGLES;
    private static final ModConfigSpec.IntValue JINGLE_AMPLITUDE;
    private static final ModConfigSpec.ConfigValue<String> TUTORIAL_PROGRESS;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("general");

        CHRISTMAS_JINGLES = builder.comment("If true, redstone-triggered jingles play Christmas songs from December 24 to 26.")
                .define(CHRISTMAS_JINGLES_KEY, DEFAULT_CHRISTMAS_JINGLES);

        HALLOWEEN_JINGLES = builder.comment("If true, redstone-triggered jingles play Halloween songs on October 31.")
                .define(HALLOWEEN_JINGLES_KEY, DEFAULT_HALLOWEEN_JINGLES);

        JINGLE_AMPLITUDE = builder.comment("The distance that jingles can be heard, in blocks, at least 1.")
                .defineInRange(JINGLE_AMPLITUDE_KEY, DEFAULT_JINGLE_AMPLITUDE, 1, Integer.MAX_VALUE);

        builder.pop();
        SPEC = builder.build();

        final ModConfigSpec.Builder client = new ModConfigSpec.Builder();
        client.push("tutorial");

        TUTORIAL_PROGRESS = client.comment("The hanging lights tutorial progress. Once any light enters the inventory a toast appears",
                    " prompting to craft hanging lights. A finished tutorial is 'complete' and an unstarted one is 'none'.")
                .define(TUTORIAL_PROGRESS_KEY, DEFAULT_TUTORIAL_PROGRESS);

        client.pop();
        CLIENT_SPEC = client.build();
    }

    @Override
    public boolean christmasJingles() {
        return CHRISTMAS_JINGLES.get();
    }

    @Override
    public boolean halloweenJingles() {
        return HALLOWEEN_JINGLES.get();
    }

    @Override
    public int jingleAmplitude() {
        return JINGLE_AMPLITUDE.get();
    }

    @Override
    public String tutorialProgress() {
        return TUTORIAL_PROGRESS.get();
    }

    @Override
    public void setTutorialProgress(final String progress) {
        TUTORIAL_PROGRESS.set(progress);
        TUTORIAL_PROGRESS.save();
    }
}

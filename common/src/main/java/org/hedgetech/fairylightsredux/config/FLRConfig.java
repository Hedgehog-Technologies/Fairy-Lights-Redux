package org.hedgetech.fairylightsredux.config;

public final class FLRConfig {
    public static final boolean DEFAULT_JINGLE_ENABLED = true;
    public static final int DEFAULT_JINGLE_VOLUME = 40;

    private static boolean jingleEnabled = DEFAULT_JINGLE_ENABLED;
    private static int jingleAmplitude = DEFAULT_JINGLE_VOLUME;

    public static boolean isJingleEnabled() {
        return jingleEnabled;
    }

    public static int getJingleAmplitude() {
        return jingleAmplitude;
    }

    public static void setJingleEnabled(boolean value) {
        jingleEnabled = value;
    }

    public static void setJingleAmplitude(int value) {
        jingleAmplitude = value;
    }

    private FLRConfig() {}
}

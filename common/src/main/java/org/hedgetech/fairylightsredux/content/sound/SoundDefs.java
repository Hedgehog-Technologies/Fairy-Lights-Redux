package org.hedgetech.fairylightsredux.content.sound;

import java.util.List;

public class SoundDefs {
    public static final String CORD_STRETCH = "cord.stretch";
    public static final String CORD_CONNECT = "cord.connect";
    public static final String CORD_DISCONNECT = "cord.disconnect";
    public static final String CORD_SNAP = "cord.snap";
    public static final String JINGLE_BELL = "jingle_bell";
    public static final String FEATURE_COLOR_CHANGE = "feature.color_change";
    public static final String FEATURE_LIGHT_TURNON = "feature.light_turnon";
    public static final String FEATURE_LIGHT_TURNOFF = "feature.light_turnoff";

    public static final List<SoundDef> SOUNDS = List.of(
            new SoundDef(CORD_STRETCH),
            new SoundDef(CORD_CONNECT),
            new SoundDef(CORD_DISCONNECT),
            new SoundDef(CORD_SNAP),
            new SoundDef(JINGLE_BELL),
            new SoundDef(FEATURE_COLOR_CHANGE),
            new SoundDef(FEATURE_LIGHT_TURNON),
            new SoundDef(FEATURE_LIGHT_TURNOFF)
    );

    private SoundDefs() {}
}

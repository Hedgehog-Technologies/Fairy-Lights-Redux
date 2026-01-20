package org.hedgetech.fairylightsredux.server.feature.light;

public interface BrightnessLightBehavior extends LightBehavior {
    float getBrightness(final float delta);
}

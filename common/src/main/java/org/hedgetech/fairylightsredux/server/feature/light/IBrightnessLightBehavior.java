package org.hedgetech.fairylightsredux.server.feature.light;

public interface IBrightnessLightBehavior extends ILightBehavior {
    float getBrightness(final float delta);
}

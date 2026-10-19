package org.hedgetech.fairylightsredux.server.feature.light;

public interface IColorLightBehavior extends ILightBehavior {
    float getRed(final float delta);

    float getGreen(final float delta);

    float getBlue(final float delta);
}

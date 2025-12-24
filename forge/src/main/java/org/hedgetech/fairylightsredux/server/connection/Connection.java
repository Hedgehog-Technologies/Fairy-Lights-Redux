package org.hedgetech.fairylightsredux.server.connection;

import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.util.CubicBezier;

public abstract class Connection {
    public static final int MAX_LENGTH = 32;
    public static final double PULL_RANGE = 5;
    public static final FeatureType CORD_FEATURE = FeatureType.register("cord");

    private static final CubicBezier SLACK_CURVE = new CubicBezier(0.495F, 0.505F, 0.495F, 0.505F);
    private static final float MAX_SLACK = 3;

     
}

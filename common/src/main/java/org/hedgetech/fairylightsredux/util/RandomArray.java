package org.hedgetech.fairylightsredux.util;

import java.util.Random;

public final class RandomArray {
    private final float[] values;

    public RandomArray(final long seed, final int length) {
        this.values = new float[length];
        final Random rng = new Random(seed);

        for (int i = 0; i < length; i++) {
            this.values[i] = rng.nextFloat() * 2.0F - 1.0F;
        }
    }

    public float get(final int index) {
        return this.values[Math.floorMod(index, this.values.length)];
    }
}

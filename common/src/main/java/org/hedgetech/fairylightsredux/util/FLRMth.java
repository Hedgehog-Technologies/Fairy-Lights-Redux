package org.hedgetech.fairylightsredux.util;

public final class FLRMth {
    public static final float PI = (float) Math.PI;
    public static final float TAU = (float) (2 * Math.PI);

    public static float mod(final float a, final float b) {
        return (a % b + b) % b;
    }

    public static float transform(final float x, final float domainMin, final float domainMax, final float rangeMin, final float rangeMax) {
        if (x <= domainMin) {
            return rangeMin;
        }

        if (x >= domainMax) {
            return rangeMax;
        }

        return (rangeMax - rangeMin) * (x - domainMin) / (domainMax - domainMin) + rangeMin;
    }

    public static int hash(int x) {
        x = (x >> 16 ^ x) * 0x45D9F3B;
        x = (x >> 16 ^ x) * 0x45D9F3B;
        x = x >> 16 ^ x;
        return x;
    }

    public static float lerpAngle(final float a, final float b, final float t) {
        return a + t * angleDifference(a, b);
    }

    public static float angleDifference(final float a, final float b) {
        return mod(b - a + FLRMth.PI, FLRMth.TAU) - FLRMth.PI;
    }

    private FLRMth() {}
}

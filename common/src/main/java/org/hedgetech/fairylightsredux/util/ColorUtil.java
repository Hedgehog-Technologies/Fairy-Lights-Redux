package org.hedgetech.fairylightsredux.util;

public final class ColorUtil {
    public static float[] intToFloatArray(final int color) {
        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        return new float[] { r, g, b };
    }

    public static int floatArrayToInt(final float[] arr) {
        if (arr == null || arr.length < 3) throw new IllegalArgumentException("Expected length >= 3");
        int r = Math.round(clamp(arr[0]) * 255) << 16;
        int g = Math.round(clamp(arr[1]) * 255) << 8;
        int b = Math.round(clamp(arr[2]) * 255);
        return r | g | b;
    }

    private static float clamp(final float v) {
        if (v < 0F) return 0F;
        return Math.min(v, 1F);
    }
}

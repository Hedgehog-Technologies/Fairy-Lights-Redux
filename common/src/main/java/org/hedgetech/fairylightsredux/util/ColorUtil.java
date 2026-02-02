package org.hedgetech.fairylightsredux.util;

public final class ColorUtil {
    public static float[] intToFloatArray(final int color) {
        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        return new float[] { r, g, b };
    }

    public static int argb(float r, float g, float b, float a) {
        return packColor(r, g, b, a);
    }

    public static int argb(float a) {
        return packColor(1.0F, 1.0F, 1.0F, a);
    }

    public static int packColor(final float r, final float g, final float b, final float a) {
        int ri = Math.round(r * 255.0F) & 0xFF;
        int gi = Math.round(g * 255.0F) & 0xFF;
        int bi = Math.round(b * 255.0F) & 0xFF;
        int ai = Math.round(a * 255.0F) & 0xFF;
        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }

    public static float[] unpackColor(final int color) {
        float a = ((color >> 24) & 0xFF) / 255F;
        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        return new float[] { r, g, b, a };
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

    private ColorUtil() {}
}

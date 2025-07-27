package org.mapsforge.map.gtk.graphics;

public class DisplayMetrics {
    private final float dpi;
    private static DisplayMetrics INSTANCE = null;

    public DisplayMetrics() {
        dpi = 120f;
    }

    public static DisplayMetrics instance() {
        if (INSTANCE == null) {
            INSTANCE = new DisplayMetrics();
        }
        return INSTANCE;
    }

    public float pointsToPixel(float points) {
        return points * dpi / 72f;

    }

    public float pixelToPoint(float pixel) {
        return pixel * 72f / dpi;
    }

    public float getDensity() {
        return dpi / 160f;
    }
}

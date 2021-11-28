package org.mapsforge.map.gtk.graphics;

import ch.bailu.gtk.gdk.Screen;

public class DisplayMetrics {
    private final float dpi;
    private static DisplayMetrics INSTANCE = null;

    public DisplayMetrics() {
        float dpi = (float) Screen.getDefault().getResolution();
        if (dpi <= 0f) {
            dpi = 96f;
        }
        this.dpi = dpi;
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

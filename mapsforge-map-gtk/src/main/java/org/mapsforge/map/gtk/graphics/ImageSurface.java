package org.mapsforge.map.gtk.graphics;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Surface;

public abstract class ImageSurface {
    private final int width;
    private final int height;
    private Surface surface;
    private Context context = null;


    public ImageSurface(Surface surface, int width, int height) {
        this.surface = surface;
        this.width = width;
        this.height = height;
    }

    public Surface getSurface() {
        mustExist();
        return surface;
    }


    public void destroy() {
        if (surface != null) {
            surface.destroy();
            surface = null;
        }

        if (context != null) {
            context.destroy();
            context = null;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDestroyed() {
        return surface == null;
    }

    public Context getContext() {
        mustExist();
        if (context == null) {
            context = getSurface().createContext();
        }
        return context;
    }

    public void mustExist() {
        if (surface == null) {
            throw new NullPointerException();
        }
    }


}

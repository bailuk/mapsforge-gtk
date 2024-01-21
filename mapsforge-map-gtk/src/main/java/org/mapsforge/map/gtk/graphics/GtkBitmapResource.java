package org.mapsforge.map.gtk.graphics;

import org.mapsforge.map.gtk.graphics.gc.BoundResourceHandler;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Surface;

public class GtkBitmapResource implements BoundResourceHandler {
    private final Surface surface;
    private final Context context;

    public GtkBitmapResource(Surface surface, Context context) {
        this.surface = new Surface(surface.cast());
        this.context = new Context(context.cast());
    }

    @Override
    public void freeResource() {
        context.destroy();
        surface.destroy();
    }
}

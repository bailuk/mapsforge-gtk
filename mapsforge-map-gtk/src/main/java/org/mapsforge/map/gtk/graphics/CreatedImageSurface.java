package org.mapsforge.map.gtk.graphics;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Format;

public class CreatedImageSurface extends ImageSurface {
    public CreatedImageSurface(int width, int height) {
        super(Cairo.imageSurfaceCreate(Format.ARGB32, width, height), width, height);
    }
}

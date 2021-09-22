package org.mapsforge.map.gtk.graphics;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.wrapper.Bytes;

public class LoadedImageSurface extends ImageSurface {
    private Bytes bytes;

    public LoadedImageSurface(Bytes bytes, int width, int height) {
        super(Cairo.imageSurfaceCreateForData(bytes, Format.RGB24, width, height, width *4), width, height);
        this.bytes = bytes;
    }


    @Override
    public void destroy() {
        super.destroy();
        if (bytes != null) {
            bytes.destroy();
            bytes = null;
        }
    }

    @Override
    public boolean isDestroyed() {
        return bytes == null;
    }
}

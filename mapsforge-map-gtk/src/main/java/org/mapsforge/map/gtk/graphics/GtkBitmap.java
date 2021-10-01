package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.ResourceBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.bridge.Image;
import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.Window;
import ch.bailu.gtk.gdkpixbuf.Colorspace;
import ch.bailu.gtk.gdkpixbuf.Gdkpixbuf;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;


public  class GtkBitmap implements Bitmap, ResourceBitmap {

    private final static InstanceCount INSTANCE_COUNT = new InstanceCount();

    private final Surface surface;
    private final Context context;
    final int width, height;

    private int refCount = 0;

    public GtkBitmap(InputStream inputStream, int hash, float scaleFactor, int width, int height, int percent) throws IOException {
        this(load(inputStream, 20, 20));
    }

    public GtkBitmap(InputStream inputStream) throws IOException {
        this(load(inputStream));
    }


    public GtkBitmap(int width, int height, boolean isTransparent) {
        this.width = width;
        this.height = height;
        surface = Cairo.imageSurfaceCreate(Format.ARGB32, width, height);
        context = surface.createContext();
        INSTANCE_COUNT.increment();
    }


    private GtkBitmap(Pixbuf pixbuf) {
        width = pixbuf.getWidth();
        height = pixbuf.getHeight();
        surface = Cairo.imageSurfaceCreate(Format.ARGB32, width, height);
        context = surface.createContext();
        Gdk.cairoSetSourcePixbuf(context,pixbuf,0,0);
        context.paint();
        pixbuf.unref();
    }

    private static Pixbuf load(InputStream stream, int width, int height) throws IOException {
        try {
            return Image.load(stream, width, height);
        } catch (AllocationError allocationError) {
            throw new IOException(allocationError.getMessage());
        }
    }

    private static Pixbuf load(InputStream stream) throws IOException {
        try {
            return Image.load(stream);
        } catch (AllocationError allocationError) {
            throw new IOException(allocationError.getMessage());
        }
    }


    @Override
    public synchronized  void compress(OutputStream outputStream) throws IOException {
        mustHaveRefCount();
        System.out.println("GtkBitmap::compress() => no buffer");
    }

    @Override
    public synchronized  void decrementRefCount() {
        mustHaveRefCount();
        refCount--;
        if (refCount < 0) {
            //System.out.println("GtkBitmap::destroy()");
            surface.destroy();
            context.destroy();
            INSTANCE_COUNT.decrement();
        }
    }

    private void mustHaveRefCount() {
        if (refCount < 0) {
            throw new IndexOutOfBoundsException("refCount: " + refCount);
        }
    }

    @Override
    public synchronized  int getHeight() {
        return height;
    }

    @Override
    public synchronized  int getWidth() {
        return width;
    }

    @Override
    public synchronized  void incrementRefCount() {
        mustHaveRefCount();
        refCount++;
    }

    @Override
    public synchronized  boolean isDestroyed() {
        return refCount < 0;
    }

    @Override
    public void scaleTo(int width, int height) {
        mustHaveRefCount();

        if (width != this.getWidth() || height != this.getHeight())
            System.out.println("GtkBitmap::scaleTo()");
    }

    @Override
    public void setBackgroundColor(int color) {
        mustHaveRefCount();
        new GtkGraphicContext(getContext(), getWidth(), getHeight()).fillColor(color);

    }

    public Context getContext() {
        mustHaveRefCount();
        return context;
    }

    public Surface getSurface() {
        mustHaveRefCount();
        return surface;
    }
}

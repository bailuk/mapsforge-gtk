package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.ResourceBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.exception.AllocationError;


public class GtkBitmap implements Bitmap, ResourceBitmap {
    private final ImageSurface imageSurface;
    private int refCount = 1;
    private static int staticRefCount = 0;
    private static int raportCount = 0;

    public GtkBitmap(InputStream inputStream, int hash, float scaleFactor, int width, int height, int percent) throws IOException {
        this(ImageConverter.svgToImageSurface(inputStream, Integer.toString(hash), scaleFactor, width, height, percent));
    }
/*
    public GtkBitmap(InputStream inputStream, float scaleFactor, int width, int height, int percent) throws IOException, AllocationError {
        this(inputStream);
        float[] newSize = GraphicUtils.imageSize(getWidth(), getHeight(), scaleFactor, width, height, percent);
        scaleTo((int) newSize[0], (int) newSize[1]);
    }
*/

    public GtkBitmap(InputStream inputStream) throws AllocationError, IOException {
        this(ImageConverter.bitmapToImageSurface(inputStream));


    }
    public GtkBitmap(ImageSurface imageSurface) throws IOException {
        this.imageSurface = imageSurface;
        staticRefCount++;
    }


    public GtkBitmap(int width, int height, boolean isTransparent) {
        this.imageSurface = new CreatedImageSurface(width, height);
        staticRefCount++;
    }


    @Override
    public void compress(OutputStream outputStream) throws IOException {
        System.out.println("GtkBitmap::compress()");
    }

    @Override
    public void decrementRefCount() {
        mustHaveRefCount();
        refCount--;

        if (refCount == 0) {
            imageSurface.destroy();
        }

        staticRefCount--;
        //System.out.println("GtkBitmap::decrementRefCount()");
        //pixbuf.unref();
    }

    private void mustHaveRefCount() {
        if (refCount <= 0) {
            throw new IndexOutOfBoundsException("refCount: " + refCount);
        }
    }

    @Override
    public int getHeight() {
        //System.out.println("GtkBitmap::getHeight()");
        return imageSurface.getHeight();
    }

    @Override
    public int getWidth() {
        //System.out.println("GtkBitmap::getWidth()");
        return imageSurface.getWidth();
    }

    @Override
    public void incrementRefCount() {
        imageSurface.mustExist();
        mustHaveRefCount();
        refCount++;
        staticRefCount++;

        if (raportCount < 0) {
            System.out.println("GtkBitmap::incrementRefCount(): " + staticRefCount);
            raportCount = 20;
        }
        raportCount--;
    }

    @Override
    public boolean isDestroyed() {
        //System.out.println("GtkBitmap::isDestroyed()");
        return imageSurface.isDestroyed();
    }

    @Override
    public void scaleTo(int width, int height) {
        System.out.println("GtkBitmap::scaleTo()");
    }

    @Override
    public void setBackgroundColor(int color) {
        GtkGraphicContext context = new GtkGraphicContext(imageSurface.getContext());
        context.fillColor(color);
        //System.out.println("GtkBitmap::setBackgroundColor()");
    }

    public Context getContext() {
        return imageSurface.getContext();
    }

    public Surface getSurface() {
        return imageSurface.getSurface();
    }
}

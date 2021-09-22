package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.ResourceBitmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Surface;


public class GtkBitmap implements Bitmap, ResourceBitmap {

    private final static InstanceCount INSTANCE_COUNT = new InstanceCount();

    private final ImageSurface imageSurface;
    private BufferedImage bufferedImage;

    private int refCount = 1;


    public GtkBitmap(InputStream inputStream, int hash, float scaleFactor, int width, int height, int percent) throws IOException {
        bufferedImage = ImageConverter.streamToBufferedImage(inputStream, Integer.toString(hash), scaleFactor, width, height, percent);
        imageSurface = ImageConverter.toImageSurface(bufferedImage);
        INSTANCE_COUNT.increment();
    }

    public GtkBitmap(InputStream inputStream) throws IOException {
        bufferedImage = ImageConverter.streamToBufferedImage(inputStream);
        imageSurface = ImageConverter.toImageSurface(bufferedImage);
        INSTANCE_COUNT.increment();
    }

    public GtkBitmap(ImageSurface imageSurface) throws IOException {
        this.imageSurface = imageSurface;
        INSTANCE_COUNT.increment();
    }

    public GtkBitmap(int width, int height, boolean isTransparent) {
        this.imageSurface = new CreatedImageSurface(width, height);
        INSTANCE_COUNT.increment();
    }


    @Override
    public void compress(OutputStream outputStream) throws IOException {
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage, "png", outputStream);
            bufferedImage = null;
        } else {
            System.out.println("GtkBitmap::compress() => no buffer");
        }
    }

    @Override
    public void decrementRefCount() {
        mustHaveRefCount();
        refCount--;
        System.out.println(refCount);

        if (refCount == 0) {
            INSTANCE_COUNT.decrement();
        }
    }

    private void mustHaveRefCount() {
        if (refCount <= 0) {
            throw new IndexOutOfBoundsException("refCount: " + refCount);
        }
    }

    @Override
    public int getHeight() {
        return imageSurface.getHeight();
    }

    @Override
    public int getWidth() {
        return imageSurface.getWidth();
    }

    @Override
    public void incrementRefCount() {
        imageSurface.mustExist();
        mustHaveRefCount();
        refCount++;
        System.out.println(refCount);
    }

    @Override
    public boolean isDestroyed() {
        return imageSurface.isDestroyed();
    }

    @Override
    public void scaleTo(int width, int height) {
        if (width != this.getWidth() || height != this.getHeight())
            System.out.println("GtkBitmap::scaleTo()");
    }

    @Override
    public void setBackgroundColor(int color) {
        GtkGraphicContext context = new GtkGraphicContext(imageSurface.getContext());
        context.fillColor(color);
    }

    public Context getContext() {
        return imageSurface.getContext();
    }

    public Surface getSurface() {
        return imageSurface.getSurface();
    }
}

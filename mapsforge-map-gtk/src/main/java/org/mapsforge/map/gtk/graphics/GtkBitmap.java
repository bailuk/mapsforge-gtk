/*
 * Copyright 2021 Lukas Bai
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.map.gtk.util.InstanceCount;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.gtk.bridge.Image;
import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.gdk.Gdk;
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
        return Image.load(stream, width, height);
    }

    private static Pixbuf load(InputStream stream) throws IOException {
        return Image.load(stream);
    }


    @Override
    public synchronized  void compress(OutputStream outputStream) throws IOException {
        mustHaveRefCount();
        Pixbuf pixbuf = Gdk.pixbufGetFromSurface(surface, 0, 0, width, height);
        Image.save(outputStream, pixbuf, "png");
        pixbuf.unref();
    }

    @Override
    public synchronized  void decrementRefCount() {
        mustHaveRefCount();
        refCount--;
        if (refCount < 0) {
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

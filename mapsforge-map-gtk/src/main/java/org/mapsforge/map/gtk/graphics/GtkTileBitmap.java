package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.TileBitmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.exception.AllocationError;

public class GtkTileBitmap extends GtkBitmap implements TileBitmap {
    private long expiration = 0;
    private long timestamp = System.currentTimeMillis();


    public GtkTileBitmap(InputStream inputStream) throws IOException, AllocationError {
        super(inputStream);
    }

    public GtkTileBitmap(int tileSize) {
        super(tileSize, tileSize, false);
    }



    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isExpired() {
        if (expiration == 0)
            return false;
        return (expiration <= System.currentTimeMillis());
    }

    @Override
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public GtkTileBitmap(int tileSize, boolean hasAlpha) {
        super(tileSize, tileSize, hasAlpha);
    }

}

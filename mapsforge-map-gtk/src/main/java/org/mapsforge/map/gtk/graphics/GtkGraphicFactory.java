package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.HillshadingBitmap;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.gtk.util.color.ARGB;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdk.RGBA;

public class GtkGraphicFactory implements GraphicFactory {
    public static final GraphicFactory INSTANCE = new GtkGraphicFactory();
    public static final boolean DRAW_DEBUG = true;

    @Override
    public Bitmap createBitmap(int width, int height) {
        return new GtkBitmap(width, height, false);
    }

    @Override
    public Bitmap createBitmap(int width, int height, boolean isTransparent) {
        return new GtkBitmap(width, height, isTransparent);
    }

    @Override
    public Canvas createCanvas() {
        return new GtkCanvas();
    }

    @Override
    public int createColor(Color color) {
        switch (color) {
            case RED:
                return 0xFFFF0000;
            case GREEN:
                return 0xFF00FF00;
            case BLUE:
                return 0xFF0000FF;
            case BLACK:
                return 0xFF000000;
            case WHITE:
                return 0xFFFFFFFF;
            case TRANSPARENT:
                return 0;
        }
        return 0;
    }

    @Override
    public int createColor(int alpha, int red, int green, int blue) {
        return new ARGB(alpha, red, green, blue).toInt();
    }


    @Override
    public Matrix createMatrix() {
        return new AwtMatrix();
    }

    @Override
    public HillshadingBitmap createMonoBitmap(int width, int height, byte[] buffer, int padding, BoundingBox area) {
        System.out.println("GtkGraphicFactory::createMonoBitmap");
        return null;
    }

    @Override
    public Paint createPaint() {
        return new GtkPaint();
    }

    @Override
    public Paint createPaint(Paint paint) {
        return new GtkPaint(paint);
    }

    @Override
    public Path createPath() {
        return new GtkPath();
    }

    @Override
    public PointTextContainer createPointTextContainer(Point xy, Display display, int priority, String text, Paint paintFront, Paint paintBack, SymbolContainer symbolContainer, Position position, int maxTextWidth) {
        return new GtkPointTextContainer(xy, display, priority, text, paintFront, paintBack, symbolContainer, position, maxTextWidth);
    }

    @Override
    public ResourceBitmap createResourceBitmap(InputStream inputStream, float scaleFactor, int width, int height, int percent, int hash) throws IOException {
        return new GtkBitmap(inputStream, hash,  scaleFactor, width, height, percent);
    }

    @Override
    public TileBitmap createTileBitmap(InputStream inputStream, int tileSize, boolean isTransparent) throws IOException {
        try {
            return new GtkTileBitmap(inputStream);
        } catch (AllocationError allocationError) {

            return null;
        }
    }

    @Override
    public TileBitmap createTileBitmap(int tileSize, boolean isTransparent) {
        return new GtkTileBitmap(tileSize, isTransparent);
    }

    @Override
    public InputStream platformSpecificSources(String relativePathPrefix, String src) throws IOException {
        System.out.println("GtkGraphicFactory::platformSpecificSources");
        return null;
    }

    @Override
    public ResourceBitmap renderSvg(InputStream inputStream, float scaleFactor, int width, int height, int percent, int hash) throws IOException {
        return new GtkBitmap(inputStream, hash, scaleFactor, width, height, percent);
    }
}

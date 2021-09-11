package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
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

import java.io.IOException;
import java.io.InputStream;

public class GtkGraphicFactory implements GraphicFactory {
    public static final GraphicFactory INSTANCE = new GtkGraphicFactory();

    @Override
    public Bitmap createBitmap(int width, int height) {
        return null;
    }

    @Override
    public Bitmap createBitmap(int width, int height, boolean isTransparent) {
        return null;
    }

    @Override
    public Canvas createCanvas() {
        return null;
    }

    @Override
    public int createColor(Color color) {
        return 0;
    }

    @Override
    public int createColor(int alpha, int red, int green, int blue) {
        return 0;
    }

    @Override
    public Matrix createMatrix() {
        return null;
    }

    @Override
    public HillshadingBitmap createMonoBitmap(int width, int height, byte[] buffer, int padding, BoundingBox area) {
        return null;
    }

    @Override
    public Paint createPaint() {
        return null;
    }

    @Override
    public Paint createPaint(Paint paint) {
        return null;
    }

    @Override
    public Path createPath() {
        return null;
    }

    @Override
    public PointTextContainer createPointTextContainer(Point xy, Display display, int priority, String text, Paint paintFront, Paint paintBack, SymbolContainer symbolContainer, Position position, int maxTextWidth) {
        return null;
    }

    @Override
    public ResourceBitmap createResourceBitmap(InputStream inputStream, float scaleFactor, int width, int height, int percent, int hash) throws IOException {
        return null;
    }

    @Override
    public TileBitmap createTileBitmap(InputStream inputStream, int tileSize, boolean isTransparent) throws IOException {
        return null;
    }

    @Override
    public TileBitmap createTileBitmap(int tileSize, boolean isTransparent) {
        return null;
    }

    @Override
    public InputStream platformSpecificSources(String relativePathPrefix, String src) throws IOException {
        return null;
    }

    @Override
    public ResourceBitmap renderSvg(InputStream inputStream, float scaleFactor, int width, int height, int percent, int hash) throws IOException {
        return null;
    }
}

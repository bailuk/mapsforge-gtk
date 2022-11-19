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
import org.mapsforge.map.gtk.util.color.ARGB;
import org.mapsforge.map.model.DisplayModel;

import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.type.exception.AllocationError;

public class GtkGraphicFactory implements GraphicFactory {
    public static final GraphicFactory INSTANCE = new GtkGraphicFactory();

    /**
     * Draw debug structures (red squares and lines)
     */
    public static boolean DRAW_DEBUG = false;

    private static final int DEFAULT_SVG_SIZE = 20;

    public GtkGraphicFactory() {
        DisplayModel.setDeviceScaleFactor(DisplayMetrics.instance().getDensity());
    }

    public static final Paint DEBUG_PAINT = new GtkPaint() {
        {
            setColor(Color.RED);
            setStrokeWidth(2);
        }
    };


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
            return new GtkTileBitmap(inputStream, tileSize);
        } catch (AllocationError allocationError) {
            return null;
        }
    }

    @Override
    public TileBitmap createTileBitmap(int tileSize, boolean isTransparent) {
        return new GtkTileBitmap(tileSize, isTransparent);
    }

    @Override
    public InputStream platformSpecificSources(String relativePathPrefix, String src) {
        System.out.println("GtkGraphicFactory::platformSpecificSources");
        return null;
    }

    @Override
    public ResourceBitmap renderSvg(InputStream inputStream, float scaleFactor, int width, int height, int percent, int hash) throws IOException {
        return new GtkBitmap(inputStream, hash, scaleFactor, width == 0 ? DEFAULT_SVG_SIZE : width, height == 0 ? DEFAULT_SVG_SIZE : height, percent);
    }
}

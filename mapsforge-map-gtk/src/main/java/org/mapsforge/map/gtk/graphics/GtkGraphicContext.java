package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.map.gtk.util.color.ARGB;
import org.mapsforge.map.gtk.util.color.Conv255;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.RGBA;

public class GtkGraphicContext implements GraphicContext {

    private final Context context;

    public GtkGraphicContext(Context context) {
        this.context = context;

    }
    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top) {
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;

        context.setSourceSurface(gtkBitmap.getSurface(), left, top);
        context.paint();
        //System.out.println("GraphicContext::drawBitmap(1)");

    }

    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top, float alpha, Filter filter) {
        drawBitmap(bitmap, left, top);
        //System.out.println("GraphicContext::drawBitmap(2)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix) {
        drawBitmap(bitmap, 0,0);
       // System.out.println("GraphicContext::drawBitmap(3)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix, float alpha, Filter filter) {
        drawBitmap(bitmap, 0,0);
        System.out.println("GraphicContext::drawBitmap(4)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom) {
        drawBitmap(bitmap, srcLeft,srcTop);
        System.out.println("GraphicContext::drawBitmap(5)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom, float alpha, Filter filter) {
        drawBitmap(bitmap, srcLeft,srcTop);
        System.out.println("GraphicContext::drawBitmap(6)");
    }

    @Override
    public void drawCircle(int x, int y, int radius, Paint paint) {
        System.out.println("GraphicContext::drawCircle()");
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Paint paint) {
        System.out.println("GraphicContext::drawLine()");
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        System.out.println("GraphicContext::drawPath()");
    }

    @Override
    public void drawPathText(String text, Path path, Paint paint) {
        System.out.println("GraphicContext::drawPathText()");
    }

    @Override
    public void drawText(String text, int x, int y, Paint paint) {
        System.out.println("GraphicContext::drawText()");
    }

    @Override
    public void drawTextRotated(String text, int x1, int y1, int x2, int y2, Paint paint) {
        System.out.println("GraphicContext::drawTextRotated()");
    }

    @Override
    public void fillColor(Color color) {
        System.out.println("GraphicContext::fillColor(enum)");
        switch (color) {
            case RED:
                fillColor(1f, 0f,0f, 1f);
                break;
            case BLUE:
                fillColor(0f, 0f,1f, 1f);
                break;
            case BLACK:
                fillColor(1f, 1f,1f, 1f);
                break;
            case GREEN:
                fillColor(0f, 1f,0f, 1f);
                break;
            case WHITE:
                fillColor(0f, 0f,0f, 1f);
                break;
            case TRANSPARENT:
                fillColor(0f, 0f,0f, 0f);
                break;
        }
    }

    @Override
    public void fillColor(int color) {
        //System.out.println("GraphicContext::fillColor(int)");
        ARGB argb = new ARGB(color);
        fillColor(
                Conv255.toDouble(argb.red()),
                Conv255.toDouble(argb.green()),
                Conv255.toDouble(argb.blue()),
                Conv255.toDouble(argb.alpha()));
    }

    public void fillColor(double r, double g, double b, double a) {
        context.setSourceRgba(r,g,b,a);
        context.paint();
    }

    @Override
    public boolean isAntiAlias() {
        System.out.println("GraphicContext::isAntiAlias()");
        return false;
    }

    @Override
    public boolean isFilterBitmap() {
        System.out.println("GraphicContext::isFilterBitmap()");
        return false;
    }

    @Override
    public void resetClip() {
        System.out.println("GraphicContext::resetClip()");
    }

    @Override
    public void setAntiAlias(boolean aa) {
        System.out.println("GraphicContext::setAntiAlias()");
    }

    @Override
    public void setClip(int left, int top, int width, int height) {
        System.out.println("GraphicContext::setClip()");
    }

    @Override
    public void setClip(int left, int top, int width, int height, boolean intersect) {
        System.out.println("GraphicContext::setClip()");
    }

    @Override
    public void setClipDifference(int left, int top, int width, int height) {
        System.out.println("GraphicContext::setClipDifference()");
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        System.out.println("GraphicContext::setFilterBitmap()");
    }

    @Override
    public void shadeBitmap(Bitmap bitmap, Rectangle shadeRect, Rectangle tileRect, float magnitude) {
        System.out.println("GraphicContext::shadeBitmap()");
    }
}

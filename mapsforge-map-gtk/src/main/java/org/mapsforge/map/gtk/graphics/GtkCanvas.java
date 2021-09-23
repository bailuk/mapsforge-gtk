package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.Rectangle;

public class GtkCanvas implements Canvas {
    private Dimension dimension = new Dimension(0,0);
    private GtkGraphicContext graphicContext;



    @Override
    public void destroy() {
        System.out.println("GtkCanvas::destroy()");
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public int getHeight() {
        return dimension.height;
    }

    @Override
    public int getWidth() {
        return dimension.width;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        //System.out.println("GtkCanvas::setBitmap()");
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;
        graphicContext = new GtkGraphicContext(gtkBitmap.getContext(), getWidth(), getHeight());
        dimension = new Dimension(bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top) {
        graphicContext.drawBitmap(bitmap, left, top);
        //System.out.println("GtkCanvas::drawBitmap()");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top, float alpha, Filter filter) {
        if (alpha != 1f || filter != Filter.NONE) {
            System.out.println("GtkCanvas::drawBitmap(left, top, "+ alpha + ", " + filter + ")");
        }
        graphicContext.drawBitmap(bitmap, left, top);
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix) {
        graphicContext.drawBitmap(bitmap, matrix);
        //System.out.println("GtkCanvas::drawBitmap(matrix)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix, float alpha, Filter filter) {
        if (alpha != 1f || filter != Filter.NONE) {
            System.out.println("GtkCanvas::drawBitmap(matrix, "+ alpha + ", " + filter + ")");
        }
        drawBitmap(bitmap, matrix);

    }

    @Override
    public void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom) {
        drawBitmap(bitmap, srcLeft, srcTop);
        System.out.println("GtkCanvas::drawBitmap(rect)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom, float alpha, Filter filter) {
        drawBitmap(bitmap, srcLeft, srcTop);
        System.out.println("GtkCanvas::drawBitmap(rect)");
    }

    @Override
    public void drawCircle(int x, int y, int radius, Paint paint) {
        System.out.println("GtkCanvas::drawCircle()");
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Paint paint) {
        graphicContext.drawLine(x1,y1, x2, y2, paint);
        //System.out.println("GtkCanvas::drawLine()");
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        graphicContext.drawPath(path, paint);
        //System.out.println("GtkCanvas::drawPath()");
    }

    @Override
    public void drawPathText(String text, Path path, Paint paint) {
        graphicContext.drawPath(path, paint);
        //System.out.println("GtkCanvas::drawPathText()");
    }

    @Override
    public void drawText(String text, int x, int y, Paint paint) {
        graphicContext.drawText(text, x, y, paint);
        //System.out.println("GtkCanvas::drawText()");
    }

    @Override
    public void drawTextRotated(String text, int x1, int y1, int x2, int y2, Paint paint) {
        System.out.println("GtkCanvas::drawTextRotated()");
    }

    @Override
    public void fillColor(Color color) {
        graphicContext.fillColor(color);
        //System.out.println("GtkCanvas::fillColor()");
    }

    @Override
    public void fillColor(int color) {
        graphicContext.fillColor(color);
        //System.out.println("GtkCanvas::fillColor()");
    }

    @Override
    public boolean isAntiAlias() {
        System.out.println("GtkCanvas::isAntiAlias()");
        return false;
    }

    @Override
    public boolean isFilterBitmap() {
        System.out.println("GtkCanvas::isFilterBitmap()");
        return false;
    }

    @Override
    public void resetClip() {
        graphicContext.resetClip();
        //System.out.println("GtkCanvas::resetClip()");
    }

    @Override
    public void setAntiAlias(boolean aa) {
        System.out.println("GtkCanvas::setAntiAlias()");
    }

    @Override
    public void setClip(int left, int top, int width, int height) {
        graphicContext.setClip(left, top,width,height);
        //System.out.println("GtkCanvas::setclip()");
    }

    @Override
    public void setClip(int left, int top, int width, int height, boolean intersect) {
        System.out.println("GtkCanvas::setClip()");
    }

    @Override
    public void setClipDifference(int left, int top, int width, int height) {
        graphicContext.setClipDifference(left,top,width, height);
        //System.out.println("GtkCanvas::setClipDifference()");
    }

    @Override
    public void setFilterBitmap(boolean filter) {

        System.out.println("GtkCanvas::setFilterBitmap()");
    }

    @Override
    public void shadeBitmap(Bitmap bitmap, Rectangle shadeRect, Rectangle tileRect, float magnitude) {
        System.out.println("GtkCanvas::shadeBitmap()");
    }
}

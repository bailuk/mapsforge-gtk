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
    public void destroy() {}

    @Override
    public synchronized Dimension getDimension() {
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
    public synchronized void setBitmap(Bitmap bitmap) {
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;
        graphicContext = new GtkGraphicContext(gtkBitmap.getContext(), getWidth(), getHeight());
        dimension = new Dimension(bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public synchronized void drawBitmap(Bitmap bitmap, int left, int top) {
        graphicContext.drawBitmap(bitmap, left, top);
    }

    @Override
    public synchronized void drawBitmap(Bitmap bitmap, int left, int top, float alpha, Filter filter) {
        if (alpha != 1f || filter != Filter.NONE) {
            System.out.println("GtkCanvas::drawBitmap(left, top, "+ alpha + ", " + filter + ")");
        }
        graphicContext.drawBitmap(bitmap, left, top);
    }

    @Override
    public synchronized void drawBitmap(Bitmap bitmap, Matrix matrix) {
        graphicContext.drawBitmap(bitmap, matrix);
    }

    @Override
    public synchronized void drawBitmap(Bitmap bitmap, Matrix matrix, float alpha, Filter filter) {
        if (alpha != 1f || filter != Filter.NONE) {
            System.out.println("GtkCanvas::drawBitmap(matrix, "+ alpha + ", " + filter + ")");
        }
        drawBitmap(bitmap, matrix);
    }

    @Override
    public synchronized void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom) {
        drawBitmap(bitmap, srcLeft, srcTop);
        System.out.println("GtkCanvas::drawBitmap(rect)");
    }

    @Override
    public synchronized void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom, float alpha, Filter filter) {
        drawBitmap(bitmap, srcLeft, srcTop);
        System.out.println("GtkCanvas::drawBitmap(rect)");
    }

    @Override
    public synchronized void drawCircle(int x, int y, int radius, Paint paint) {
        graphicContext.drawCircle(x,y,radius,paint);
    }

    @Override
    public synchronized void drawLine(int x1, int y1, int x2, int y2, Paint paint) {
        graphicContext.drawLine(x1,y1, x2, y2, paint);
    }

    @Override
    public synchronized void drawPath(Path path, Paint paint) {
        graphicContext.drawPath(path, paint);
    }

    @Override
    public synchronized void drawPathText(String text, Path path, Paint paint) {
        graphicContext.drawPathText(text, path, paint);
    }

    @Override
    public synchronized void drawText(String text, int x, int y, Paint paint) {
        graphicContext.drawText(text, x, y, paint);
    }

    @Override
    public synchronized void drawTextRotated(String text, int x1, int y1, int x2, int y2, Paint paint) {
        System.out.println("GtkCanvas::drawTextRotated()");
    }

    @Override
    public synchronized void fillColor(Color color) {
        graphicContext.fillColor(color);
    }

    @Override
    public synchronized void fillColor(int color) {
        graphicContext.fillColor(color);
    }

    @Override
    public synchronized boolean isAntiAlias() {
        System.out.println("GtkCanvas::isAntiAlias()");
        return false;
    }

    @Override
    public boolean isFilterBitmap() {
        System.out.println("GtkCanvas::isFilterBitmap()");
        return false;
    }

    @Override
    public synchronized void resetClip() {
        graphicContext.resetClip();
    }

    @Override
    public synchronized void setAntiAlias(boolean aa) {
        System.out.println("GtkCanvas::setAntiAlias()");
    }

    @Override
    public synchronized void setClip(int left, int top, int width, int height) {
        graphicContext.setClip(left, top,width,height);
    }

    @Override
    public void setClip(int left, int top, int width, int height, boolean intersect) {
        System.out.println("GtkCanvas::setClip(intersect)");
    }

    @Override
    public synchronized void setClipDifference(int left, int top, int width, int height) {
        graphicContext.setClipDifference(left,top,width, height);
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        System.out.println("GtkCanvas::setFilterBitmap()");
    }

    @Override
    public void shadeBitmap(Bitmap bitmap, Rectangle shadeRect, Rectangle tileRect, float magnitude) {
        System.out.println("GtkCanvas::shadeBitmap()");
    }


    public void drawDebugRect(Rectangle rect) {
        graphicContext.drawDebugRect(rect);
    }

    public void drawTextIntoBoundary(String text, Rectangle boundary, GtkPaint paintFront) {
        graphicContext.drawTextIntoBoundary(text, boundary, paintFront);
    }
}

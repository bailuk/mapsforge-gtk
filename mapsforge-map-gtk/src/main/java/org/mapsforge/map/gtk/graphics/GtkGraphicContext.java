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
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.map.gtk.util.color.ARGB;
import org.mapsforge.map.gtk.util.color.Conv255;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Extend;
import ch.bailu.gtk.cairo.Operator;
import ch.bailu.gtk.cairo.Pattern;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.pango.FontDescription;
import ch.bailu.gtk.pango.Layout;
import ch.bailu.gtk.pango.Pango;
import ch.bailu.gtk.pangocairo.Pangocairo;
import ch.bailu.gtk.type.Dbl;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Wrapper;


public class GtkGraphicContext implements GraphicContext, Canvas {

    private final Context context;
    private final Dimension dimension;

    public GtkGraphicContext(Context context, Dimension dimension) {
        this.context = context;
        this.dimension = dimension;
    }

    public GtkGraphicContext(Context context, int width, int height) {
        this.context = context;
        dimension = new Dimension(width, height);
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top) {
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;
        context.save();
        context.setSourceSurface(gtkBitmap.getSurface(), left, top);
        context.paint();
        context.restore();

    }

    @Override
    public void drawBitmap(Bitmap bitmap, int left, int top, float alpha, Filter filter) {
        if (filter != Filter.NONE) {
            System.out.println("GraphicContext::drawBitmap("+alpha+ ", " + filter.toString() + ")");
        }

        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;
        context.save();
        context.setSourceSurface(gtkBitmap.getSurface(), left, top);
        context.paintWithAlpha(alpha);
        context.restore();
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix) {
        AwtMatrix awtMatrix = (AwtMatrix) matrix;
        ch.bailu.gtk.cairo.Matrix cairoMatrix = awtMatrix.toCairoMatrix();
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;

        context.save();
        context.transform(cairoMatrix);
        context.setSourceSurface(gtkBitmap.getSurface(),0,0);
        context.paint();
        context.identityMatrix();
        context.restore();

        cairoMatrix.destroy();
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix, float alpha, Filter filter) {
        if (filter != Filter.NONE) {
            System.out.println("GraphicContext::drawBitmap(matrix, "+alpha+ ", " + filter.toString() + ")");
        }
        AwtMatrix awtMatrix = (AwtMatrix) matrix;
        ch.bailu.gtk.cairo.Matrix cairoMatrix = awtMatrix.toCairoMatrix();
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;

        context.save();
        context.transform(cairoMatrix);
        context.setSourceSurface(gtkBitmap.getSurface(),0,0);
        context.paintWithAlpha(alpha);
        context.identityMatrix();
        context.restore();

        cairoMatrix.destroy();
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom) {
        drawBitmap(bitmap, srcLeft,srcTop);
        System.out.println("GraphicContext::drawBitmap(5)");
    }

    @Override
    public void drawBitmap(Bitmap bitmap, int srcLeft, int srcTop, int srcRight, int srcBottom, int dstLeft, int dstTop, int dstRight, int dstBottom, float alpha, Filter filter) {
        drawBitmap(bitmap, srcLeft,srcTop, alpha, filter);
        System.out.println("GraphicContext::drawBitmap(6)");
    }

    @Override
    public void drawCircle(int x, int y, int radius, Paint paint) {
        if (!paint.isTransparent()) {
            GtkPaint gtkPaint = (GtkPaint) paint;

            context.save();
            var res = setLineAndColor(gtkPaint);
            context.arc(x , y , radius, 0.0, 2 * Math.PI);
            fillOrStroke(gtkPaint.getStyle());
            context.restore();
            destroyResources(res);
        }
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Paint paint) {
        if (!paint.isTransparent()) {
            GtkPaint gtkPaint = (GtkPaint) paint;

            context.save();
            var res = setLineAndColor(gtkPaint);
            context.moveTo(x1, y1);
            context.lineTo(x2, y2);
            context.stroke();
            context.restore();
            destroyResources(res);
        }
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        if (!paint.isTransparent() && !path.isEmpty()) {
            GtkPaint gtkPaint = (GtkPaint) paint;
            GtkPath gtkPath = (GtkPath) path;
            if (gtkPaint.getBitmapShader() == null) {
                context.save();
                var res = setLineAndColor(gtkPaint);
                context.setFillRule(gtkPath.getFillRule());
                gtkPath.exec(context);
                fillOrStroke(gtkPaint.getStyle());
                context.restore();
                destroyResources(res);

            } else {
                context.save();
                gtkPath.exec(context);
                Surface surface = gtkPaint.getBitmapShader().getSurface();
                Pattern pattern = Pattern.createForSurfacePattern(surface);
                pattern.setExtend(Extend.REPEAT);
                context.setSource(pattern);
                context.fill();
                context.restore();
                pattern.destroy();
                drawDebugPath(gtkPath);
            }
        }
    }


    private Dbl setLineAndColor(GtkPaint paint) {
        float[] dashes = paint.getDashPathEffect();
        Dbl res = null;

        setColor(paint.getColor());
        context.setLineWidth(paint.getStrokeWidth());
        context.setLineCap(paint.getStrokeCap());
        context.setLineJoin(paint.getStrokeJoin());
        if (dashes != null && dashes.length > 0) {
            res = new Dbl(dashes);
            context.setDash(res, dashes.length, 0d);
        }
        return res;
    }

    private static void destroyResources(Wrapper res) {
        if (res != null) res.destroy();
    }

    private void fillOrStroke(Style style) {
        if (style == Style.FILL) {
            context.fill();
        } else if (style == Style.STROKE){
            context.stroke();
        }
    }

    @Override
    public void drawPathText(String text, Path path, Paint paint) {
        GtkPath gtkPath = (GtkPath) path;

        drawDebugPath(gtkPath);
        drawTextRotated(text,
                (int)gtkPath.getXStart(),
                (int)gtkPath.getYStart(),
                (int)gtkPath.getXEnd(),
                (int)gtkPath.getYEnd(),
                paint);
    }

    @Override
    public void drawText(String text, int x, int y, Paint paint) {
        GtkPaint gtkPaint = (GtkPaint) paint;
        drawTextRotated(text,x, (y), (int)(gtkPaint.getFontSize()/ 100* -130),gtkPaint, 0);
    }

    private void drawTextRotated(String text, int x, int y, int yshift, GtkPaint paint, double angle) {
        final Str strText = new Str(text);
        final Str strFont = new Str(paint.getFontDescription());
        final FontDescription fontDescription = Pango.fontDescriptionFromString(strFont);
        final Layout layout = Pangocairo.createLayout(context);

        layout.setText(strText, strText.getSize()-1);
        layout.setFontDescription(fontDescription);
        layout.setAlignment(paint.getTextAlignment());

        context.save();
        context.setLineWidth(paint.getStrokeWidth());

        if (angle != 0) {
            context.translate(x, y);
            context.rotate(angle);
            context.translate(-x, -y);
        }
        context.moveTo(x,y+yshift);
        Pangocairo.layoutPath(context,layout);
        setColor(paint.getColor());
        context.fillPreserve();
        context.stroke();
        context.restore();

        layout.unref();
        fontDescription.free();
        strText.destroy();
        strFont.destroy();
    }

    public void drawTextIntoBoundary(String text, Rectangle boundary, GtkPaint paint) {

        drawTextRotated(text, (int)boundary.left, (int) boundary.top, 0, paint, 0);
        drawDebugRect(boundary);
    }
    @Override
    public void drawTextRotated(String text, int x1, int y1, int x2, int y2, Paint paint) {

        drawDebugLine(x1, y1, x2, y2);
        drawTextRotated(text, x1,y1, paint.getTextHeight("W")/-2, (GtkPaint) paint, getAngle(x1,y1, x2, y2));
    }

    private double getAngle(int x1, int y1, int x2, int y2) {
        return Math.atan2(y2 - y1, x2 - x1) + 2 * Math.PI;
    }

    @Override
    public void fillColor(Color color) {
        fillColor(GtkGraphicFactory.INSTANCE.createColor(color));
    }

    @Override
    public void fillColor(int color) {
        context.save();
        setColor(color);
        context.setOperator(getSourceFromAlpha(color));
        context.paint();
        context.restore();
    }


    private int getSourceFromAlpha(int color) {
        if (ARGB.alpha(color) == 0) {
            return Operator.CLEAR;
        } else {
            return Operator.SOURCE;
        }
    }

    private void setColor(int color) {
        ARGB argb = new ARGB(color);
        context.setSourceRgba(
                Conv255.toDouble(argb.red()),
                Conv255.toDouble(argb.green()),
                Conv255.toDouble(argb.blue()),
                Conv255.toDouble(argb.alpha()));
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
        context.resetClip();
    }

    @Override
    public void setAntiAlias(boolean aa) {
        System.out.println("GraphicContext::setAntiAlias()");
    }

    @Override
    public void setClip(int left, int top, int width, int height) {
        context.resetClip();
        context.newPath();
        context.rectangle(left,top,width,height);
        context.clip();
    }

    @Override
    public void setClip(int left, int top, int width, int height, boolean intersect) {
        System.out.println("GraphicContext::setClip()");
    }

    @Override
    public void setClipDifference(int left, int top, int width, int height) {
        context.resetClip();
        context.newPath();

        final int tw=dimension.width;
        context.rectangle(0, 0, tw, top);
        context.clip();

        final int lx=0;
        context.rectangle(lx, top, left, height);
        context.clip();

        final int rw=dimension.width- left -width;
        final int rx=dimension.width-rw;
        context.rectangle(rx, top, left, height);
        context.clip();

        final int bh=dimension.height-height- top;
        final int bx=0;
        final int by=dimension.height-bh;
        context.rectangle(bx, by, tw, bh);
        context.clip();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        System.out.println("GraphicContext::setFilterBitmap()");
    }

    @Override
    public void shadeBitmap(Bitmap bitmap, Rectangle shadeRect, Rectangle tileRect, float magnitude) {
        System.out.println("GraphicContext::shadeBitmap()");
    }

    private void drawDebugLine(int x1, int y1, int x2, int y2) {
        if (GtkGraphicFactory.DRAW_DEBUG) {
            drawCircle(x1,y1, 5, GtkGraphicFactory.DEBUG_PAINT);
            drawLine(x1,y1,x2,y2, GtkGraphicFactory.DEBUG_PAINT);
        }
    }

    private void drawDebugPath(GtkPath path) {
        if (GtkGraphicFactory.DRAW_DEBUG) {
            drawCircle((int)path.getXStart(),(int)path.getYStart(), 5, GtkGraphicFactory.DEBUG_PAINT);
            drawPath(path, GtkGraphicFactory.DEBUG_PAINT);
        }
    }


    public void drawDebugRect(Rectangle rect) {
        if (GtkGraphicFactory.DRAW_DEBUG) {
            final Path path = GtkGraphicFactory.INSTANCE.createPath();
            path.moveTo((float) rect.left, (float) rect.top);
            path.lineTo((float) rect.right, (float) rect.top);
            path.lineTo((float) rect.right, (float) rect.bottom);
            path.lineTo((float) rect.left, (float) rect.bottom);
            path.lineTo((float) rect.left, (float) rect.top);
            drawPath(path, GtkGraphicFactory.DEBUG_PAINT);
        }
    }

    /**
     * Does nothing. Exists to satisfy Canvas interface
     */
    @Override
    public void destroy() {}

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

    /**
     * Does nothing. Exists to satisfy Canvas interface
     */
    @Override
    public void setBitmap(Bitmap bitmap) {}
}

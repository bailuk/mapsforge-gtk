package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Bitmap;
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
import ch.bailu.gtk.cairo.Operator;
import ch.bailu.gtk.pango.FontDescription;
import ch.bailu.gtk.pango.Layout;
import ch.bailu.gtk.pango.Pango;
import ch.bailu.gtk.pangocairo.Pangocairo;
import ch.bailu.gtk.wrapper.Str;

public class GtkGraphicContext implements GraphicContext {

    private final Context context;
    private final int width, height;

    public GtkGraphicContext(Context context, Dimension dimension) {
        this(context, dimension.width, dimension.height);
    }

    public GtkGraphicContext(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;

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
        if (alpha != 1f || filter != Filter.NONE) {
            System.out.println("GraphicContext::drawBitmap("+alpha+ ", " + filter.toString() + ")");
        }
        drawBitmap(bitmap, left, top);

    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix) {
        //System.out.println(bitmap.getWidth() + " x " +bitmap.getHeight());

        context.save();
        GtkMatrix gtkMatrix = (GtkMatrix) matrix;
        GtkBitmap gtkBitmap = (GtkBitmap) bitmap;
        context.setMatrix(gtkMatrix.matrix);
        context.setSourceSurface(gtkBitmap.getSurface(),0,0);
        context.paint();
        context.identityMatrix();
        context.restore();
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix, float alpha, Filter filter) {
        if (alpha != 1f || filter != Filter.NONE) {
            System.out.println("GraphicContext::drawBitmap(matrix, "+alpha+ ", " + filter.toString() + ")");
        }
        drawBitmap(bitmap, matrix);
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
        GtkPaint gtkPaint = (GtkPaint) paint;

        if (!paint.isTransparent()) {
            context.save();
            setColor(paint.getColor());
            context.setLineWidth(paint.getStrokeWidth());
            context.arc(x , y , radius, 0.0, 2 * Math.PI);
            fillOrStroke(gtkPaint.style);
            context.restore();
        }
        //System.out.println("GraphicContext::drawCircle()");
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Paint paint) {
        if (!paint.isTransparent()) {
            context.save();
            setColor(paint.getColor());
            context.setLineWidth(paint.getStrokeWidth());

            context.moveTo(x1, y1);
            context.lineTo(x2, y2);
            context.stroke();
            context.restore();
        }
        //System.out.println("GraphicContext::drawLine()");
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        GtkPaint gtkPaint = (GtkPaint) paint;

        if (!gtkPaint.isTransparent() && !path.isEmpty()) {
            GtkPath p = (GtkPath) path;
            context.save();

            setColor(paint.getColor());
            context.setLineWidth(paint.getStrokeWidth());
            p.exec(context);
            fillOrStroke(gtkPaint.style);
            context.restore();
        }
    }

    private void fillOrStroke(Style style) {
        if (style == Style.FILL) {
            context.fill();
        } else {
            context.stroke();
        }
    }

    @Override
    public void drawPathText(String text, Path path, Paint paint) {
        this.drawPath(path, paint);
        System.out.println("GraphicContext::drawPathText()");
    }

    @Override
    public void drawText(String text, int x, int y, Paint paint) {
        Str txt = new Str(text);
        Str dsc = new Str("sans 10");

        Layout layout = Pangocairo.createLayout(context);
        layout.setText(txt, text.length());
        FontDescription desc = Pango.fontDescriptionFromString(dsc);
        layout.setFontDescription(desc);
        desc.free();

        context.setLineWidth(paint.getStrokeWidth());
        context.moveTo(x,y-12);
        Pangocairo.layoutPath(context,layout);
        layout.unref();

        setColor(paint.getColor());

        context.fillPreserve();
        context.stroke();

        //txt.destroy();
        //dsc.destroy();
    }

    @Override
    public void drawTextRotated(String text, int x1, int y1, int x2, int y2, Paint paint) {
        System.out.println("GraphicContext::drawTextRotated()");
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

        //System.out.println("GraphicContext::setClip()");
    }

    @Override
    public void setClip(int left, int top, int width, int height, boolean intersect) {
        System.out.println("GraphicContext::setClip()");
    }

    @Override
    public void setClipDifference(int left, int top, int width, int height) {
        context.resetClip();
        context.newPath();

        final int th=top;
        final int tw=this.width;
        context.rectangle(0, 0, tw, th);
        context.clip();

        final int lh=height;
        final int lw=left;
        final int lx=0;
        final int ly=th;
        context.rectangle(lx, ly, lw, lh);
        context.clip();

        final int rh=lh;
        final int rw=this.width-lw-width;
        final int rx=this.width-rw;
        final int ry=th;
        context.rectangle(rx, ry, lw, rh);
        context.clip();

        final int bh=this.height-height-th;
        final int bw=tw;
        final int bx=0;
        final int by=this.height-bh;
        context.rectangle(bx, by, bw, bh);


        context.clip();
        //System.out.println("GraphicContext::setClipDifference()");
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

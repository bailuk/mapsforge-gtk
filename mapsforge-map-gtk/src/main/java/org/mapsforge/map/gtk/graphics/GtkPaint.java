package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Align;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.Join;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.gtk.util.color.ARGB;

import ch.bailu.gtk.cairo.CairoConstants;
import ch.bailu.gtk.cairo.LineCap;
import ch.bailu.gtk.cairo.LineJoin;
import ch.bailu.gtk.pango.Alignment;

public class GtkPaint implements Paint {
    private int color;
    private float strokeWidth;
    private GtkBitmap bitmapShader;
    private Point bitmapShaderShift = new Point(0,0);

    private FontFamily fontFamily;
    private FontStyle fontStyle;
    private float fontSize = 10;
    private Align textAlign = Align.LEFT;

    private Style style;
    private float[] dashes;
    private int strokeCap;
    private int strokeJoin;

    public GtkPaint(Paint paint) {
        GtkPaint other = (GtkPaint) paint;
        this.color = other.color;
        this.strokeWidth = other.strokeWidth;
        this.bitmapShader = other.bitmapShader;
        this.bitmapShaderShift = other.bitmapShaderShift;
        this.fontStyle = other.fontStyle;
        this.fontFamily = other.fontFamily;
        this.fontSize = other.fontSize;
        this.textAlign = other.textAlign;
        this.style = other.style;
        this.dashes = other.dashes;
        this.strokeCap = other.strokeCap;
        this.strokeJoin = other.strokeJoin;
    }

    public GtkPaint() {
        this.style = Style.STROKE;
        this.color = GtkGraphicFactory.INSTANCE.createColor(Color.BLACK);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public float getStrokeWidth() {
        return strokeWidth;
    }

    @Override
    public int getTextHeight(String text) {
        return (int) (fontSize+5f);
    }

    @Override
    public int getTextWidth(String text) {
        return (int) ((fontSize * text.length()));
    }

    @Override
    public boolean isTransparent() {
        return bitmapShader == null && ARGB.alpha(color) == 0;
    }

    @Override
    public void setBitmapShader(Bitmap bitmap) {
        if (bitmapShader != null) {
            bitmapShader.decrementRefCount();
            bitmapShader = null;
        }
        if (bitmap != null) {
            bitmapShader = (GtkBitmap) bitmap;
            bitmapShader.incrementRefCount();
        }
    }

    public GtkBitmap getBitmapShader() {
        return bitmapShader;
    }

    @Override
    public void setBitmapShaderShift(Point origin) {
        //System.out.println("GtkPaint::setBitmapShaderShift");
        bitmapShaderShift = origin;
    }

    public Point getBitmapShaderShift() {
        return bitmapShaderShift;
    }
    @Override
    public void setColor(Color color) {
        this.color = GtkGraphicFactory.INSTANCE.createColor(color);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void setDashPathEffect(float[] strokeDasharray) {
        //System.out.println("GtkPaint::setDashPathEffect");
        dashes = strokeDasharray;
    }

    public float[] getDashPathEffect() {
        return dashes;
    }

    @Override
    public void setStrokeCap(Cap cap) {
        switch (cap) {
            case ROUND:
                strokeCap = LineCap.ROUND;
                break;
            case SQUARE:
                strokeCap = LineCap.SQUARE;
                break;
            case BUTT:
            default:
                strokeCap = LineCap.BUTT;
                break;
        }
    }

    public int getStrokeCap() {
        return strokeCap;
    }

    @Override
    public void setStrokeJoin(Join join) {
        switch (join) {
            case BEVEL:
                strokeJoin = LineJoin.BEVEL;
                break;
            case MITER:
                strokeJoin = LineJoin.MITER;
                break;
            case ROUND:
            default:
                strokeJoin = LineJoin.ROUND;
                break;
        }
    }


    public int getStrokeJoin() {
        return strokeJoin;
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public void setTextAlign(Align align) {
        textAlign = align;
    }

    @Override
    public void setTextSize(float textSize) {
        this.fontSize = textSize;
    }

    @Override
    public void setTypeface(FontFamily fontFamily, FontStyle fontStyle) {
        this.fontFamily = fontFamily;
        this.fontStyle = fontStyle;
    }

    public String getFontDescription() {
        StringBuilder result = new StringBuilder();
        switch (fontFamily) {
            case DEFAULT:
            case SANS_SERIF:
                result.append("sans");
                break;
            case MONOSPACE:
                result.append("mono");
                break;
            case SERIF:
                result.append("serif");
                break;
        }

        switch (fontStyle) {
            case BOLD:
                result.append(" bold");
                break;
            case BOLD_ITALIC:
                result.append(" bold italic");
                break;
            case ITALIC:
                result.append(" italic");
                break;
            case NORMAL:
                result.append(" normal");
                break;
        }

        result.append(" ");
        result.append(fontSize);
        return result.toString();
    }

    public float getFontSize() {
        return fontSize;
    }

    public int getTextAlignment() {
        int result = Alignment.LEFT;
        switch (textAlign) {
            case CENTER:
                result = Alignment.CENTER;
                break;
            case LEFT:
                result = Alignment.LEFT;
                break;
            case RIGHT:
                result = Alignment.RIGHT;
                break;
        }
        return result;
    }

    public Style getStyle() {
        return style;
    }
}

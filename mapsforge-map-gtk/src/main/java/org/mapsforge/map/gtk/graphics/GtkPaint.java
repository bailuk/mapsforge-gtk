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

public class GtkPaint implements Paint {
    private int color;
    private float strokeWidth;
    private boolean transparent=false;
    private GtkBitmap bitmapShader;
    private Point bitmapShaderShift = null;

    private FontFamily fontFamily;
    private FontStyle fontStyle;
    private float textSize;

    private Align textAlign;
    private Style style;
    private float[] dashPathEffect;
    private Cap strokeCap;
    private Join strokeJoin;

    public GtkPaint(Paint paint) {
        GtkPaint other = (GtkPaint) paint;
        this.color = other.color;
        this.strokeWidth = other.strokeWidth;
        this.bitmapShader = other.bitmapShader;
        this.bitmapShaderShift = other.bitmapShaderShift;
        this.fontStyle = other.fontStyle;
        this.fontFamily = other.fontFamily;
        this.textSize = other.textSize;
        this.textAlign = other.textAlign;
        this.style = other.style;
        this.dashPathEffect = other.dashPathEffect;
        this.strokeCap = other.strokeCap;
        this.strokeJoin = other.strokeJoin;
    }

    public GtkPaint() {

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
        return 12;
    }

    @Override
    public int getTextWidth(String text) {
        return 20;
    }

    @Override
    public boolean isTransparent() {
        return transparent;
    }

    @Override
    public void setBitmapShader(Bitmap bitmap) {
        bitmapShader = (GtkBitmap) bitmap;
    }

    @Override
    public void setBitmapShaderShift(Point origin) {
        bitmapShaderShift = origin;
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
        dashPathEffect = strokeDasharray;
    }

    @Override
    public void setStrokeCap(Cap cap) {
        strokeCap = cap;
    }

    @Override
    public void setStrokeJoin(Join join) {
        strokeJoin = join;
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
        this.textSize = textSize;
    }

    @Override
    public void setTypeface(FontFamily fontFamily, FontStyle fontStyle) {
        this.fontFamily = fontFamily;
        this.fontStyle = fontStyle;
    }
}

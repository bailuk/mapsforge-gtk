package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Matrix;
/*
public class GtkMatrix implements Matrix {
    final ch.bailu.gtk.cairo.Matrix matrix = new ch.bailu.gtk.cairo.Matrix();

    public GtkMatrix() {
        reset();
    }

    @Override
    public void reset() {
        matrix.initIdentity();
    }

    @Override
    public void rotate(float theta) {
        matrix.rotate(theta);
    }

    @Override
    public void rotate(float theta, float pivotX, float pivotY) {
        matrix.translate(pivotX, pivotY);
        matrix.rotate(theta);
        matrix.translate(-pivotX, -pivotY);
    }

    @Override
    public void scale(float scaleX, float scaleY) {
        matrix.scale(scaleX, scaleY);
    }

    @Override
    public void scale(float scaleX, float scaleY, float pivotX, float pivotY) {
        matrix.translate(pivotX, pivotY);
        matrix.scale(scaleX, scaleY);
        matrix.translate(-pivotX, -pivotY);
    }

    @Override
    public void translate(float translateX, float translateY) {
        matrix.translate(translateX, translateY);
    }
}
*/
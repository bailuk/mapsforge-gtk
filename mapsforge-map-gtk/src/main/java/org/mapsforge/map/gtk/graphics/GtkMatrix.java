package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Matrix;

import java.awt.geom.AffineTransform;

public class GtkMatrix implements Matrix {
    final AffineTransform affineTransform = new AffineTransform();

    @Override
    public void reset() {
        this.affineTransform.setToIdentity();
    }

    @Override
    public void rotate(float theta) {
        this.affineTransform.rotate(theta);
    }

    @Override
    public void rotate(float theta, float pivotX, float pivotY) {
        this.affineTransform.rotate(theta, pivotX, pivotY);
    }

    @Override
    public void scale(float scaleX, float scaleY) {
        this.affineTransform.scale(scaleX, scaleY);
    }

    @Override
    public void scale(float scaleX, float scaleY, float pivotX, float pivotY) {
        this.affineTransform.translate(pivotX, pivotY);
        this.affineTransform.scale(scaleX, scaleY);
        this.affineTransform.translate(-pivotX, -pivotY);
    }

    @Override
    public void translate(float translateX, float translateY) {
        this.affineTransform.translate(translateX, translateY);
    }
}

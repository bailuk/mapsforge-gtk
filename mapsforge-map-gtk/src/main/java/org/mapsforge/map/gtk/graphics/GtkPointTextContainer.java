/*
 * Copyright 2014 Ludwig M Brinckmann
 * Copyright 2014-2016 devemux86
 * Copyright 2021-2025 Lukas Bai
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

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.core.model.Rotation;
import org.mapsforge.map.gtk.util.MultiLineText;

public class GtkPointTextContainer extends PointTextContainer {
    private final MultiLineText lines;

    private final Rectangle boundary;
    /**
     * Create a new point container, that holds the x-y coordinates of a point, a text variable, two paint objects, and
     * a reference on a symbolContainer, if the text is connected with a POI.
     */
    protected GtkPointTextContainer(Point point, double horizontalOffset, double verticalOffset,
                                 Display display, int priority, String text, Paint paintFront, Paint paintBack,
                                 SymbolContainer symbolContainer, Position position, int maxTextWidth) {
        super(point, horizontalOffset, verticalOffset, display, priority, text, paintFront, paintBack, symbolContainer, position, maxTextWidth);

        final int charWidth = paintBack.getTextWidth("W");
        final int charHeight = paintBack.getTextHeight("Y");

        this.lines = new MultiLineText(text, maxTextWidth / charWidth);
        this.boundary = alignBoundary(lines.getBoundary(charWidth, charHeight));
    }


    @Override
    public void draw(Canvas canvas, Point origin, Matrix matrix, Rotation rotation) {
        if (canDraw()) {
            doDraw(canvas, origin);
        }
    }

    private boolean canDraw() {
        return havePaint(paintBack) || havePaint(paintFront);
    }
    private boolean havePaint(Paint paint) {
        return paint != null && !paint.isTransparent();
    }

    private void doDraw(Canvas canvas, Point origin) {
        final GtkCanvas gtkCanvas = (GtkCanvas) canvas;
        final Point pointAdjusted = this.xy.offset(-origin.x, -origin.y);
        final Rectangle boundary = this.boundary.shift(pointAdjusted);
        drawLines(gtkCanvas, boundary);
    }


    private void drawLines(GtkCanvas gtkCanvas, Rectangle boundary) {
        if (lines.size() > 0) {
            Rectangle lineBoundary = new Rectangle(
                    boundary.left, boundary.top,
                    boundary.right, boundary.top + (boundary.getHeight() / lines.size()));
            Point shift = new Point(0, lineBoundary.getHeight());

            for (String line : lines.getLines()) {
                drawTextIntoBoundary(gtkCanvas, line, lineBoundary, (GtkPaint) paintBack);
                drawTextIntoBoundary(gtkCanvas, line, lineBoundary, (GtkPaint) paintFront);
                lineBoundary = lineBoundary.shift(shift);
            }
        }
    }


    private void drawTextIntoBoundary(GtkCanvas canvas, String text, Rectangle boundary, GtkPaint paint) {
        if (paint != null) {
            int color = paint.getColor();
            setFilterColor(paint);
            canvas.drawTextIntoBoundary(text, boundary, paint);
            paint.setColor(color);
        }
    }

    private static void setFilterColor(Paint paint) {
        if (paint != null) {
            paint.setColor(paint.getColor());
        }
    }


    private Rectangle alignBoundary(Rectangle rect) {
        return rect.shift(getAlignShift(rect.getWidth(), rect.getHeight()));
    }

    private Point getAlignShift(double w, double h) {
        switch (this.position) {
            case CENTER:
                return new Point(w / -2, h / -2);
            case BELOW:
                return new Point(w / -2, 0);
            case BELOW_LEFT:
                return new Point(w * -1, 0);
            case BELOW_RIGHT:
                return new Point(0, 0);
            case ABOVE:
                return new Point(w / -2, h * -1);
            case ABOVE_LEFT:
                return new Point(w * -1, h * -1);
            case ABOVE_RIGHT:
                return new Point(0, h * -1);
            case LEFT:
                return new Point(w * -1, h / -2);
            case RIGHT:
            default:
                return new Point(w, h / -2);
        }
    }

    @Override
    protected Rectangle getBoundary() {
        return boundary;
    }

}

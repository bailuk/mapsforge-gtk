package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.map.gtk.util.MultiLineText;

public class GtkPointTextContainer extends PointTextContainer {
    private final MultiLineText lines;

    /**
     * Create a new point container, that holds the x-y coordinates of a point, a text variable, two paint objects, and
     * a reference on a symbolContainer, if the text is connected with a POI.
     */
    protected GtkPointTextContainer(
            Point point,
            Display display,
            int priority,
            String text,
            Paint paintFront,
            Paint paintBack,
            SymbolContainer symbolContainer,
            Position position,
            int maxTextWidth) {
        super(point, display, priority, text, paintFront, paintBack, symbolContainer, position, maxTextWidth);

        final int charWidth = paintBack.getTextWidth("W");
        final int charHeight = paintBack.getTextHeight("Y");

        this.lines = new MultiLineText(text, maxTextWidth / charWidth);
        this.boundary = alignBoundary(lines.getBoundary(charWidth, charHeight));
    }


    @Override
    public void draw(Canvas canvas, Point origin, Matrix matrix, Filter filter) {
        if (canDraw()) {
            doDraw(canvas, origin, filter);
        }
    }

    private boolean canDraw() {
        return havePaint(paintBack) || havePaint(paintFront);
    }
    private boolean havePaint(Paint paint) {
        return paint != null && !paint.isTransparent();
    }

    private void doDraw(Canvas canvas, Point origin, Filter filter) {
        final GtkCanvas gtkCanvas = (GtkCanvas) canvas;
        final Point pointAdjusted = this.xy.offset(-origin.x, -origin.y);
        final Rectangle boundary = this.boundary.shift(pointAdjusted);
        drawLines(gtkCanvas, boundary, filter);
    }


    private void drawLines(GtkCanvas gtkCanvas, Rectangle boundary, Filter filter) {
        if (lines.size() > 0) {
            Rectangle lineBoundary = new Rectangle(
                    boundary.left, boundary.top,
                    boundary.right, boundary.top + (boundary.getHeight() / lines.size()));
            Point shift = new Point(0, lineBoundary.getHeight());

            for (String line : lines.getLines()) {
                drawTextIntoBoundary(gtkCanvas, line, lineBoundary, (GtkPaint) paintBack, filter);
                drawTextIntoBoundary(gtkCanvas, line, lineBoundary, (GtkPaint) paintFront, filter);
                lineBoundary = lineBoundary.shift(shift);
            }
        }
    }


    private void drawTextIntoBoundary(GtkCanvas canvas, String text, Rectangle boundary, GtkPaint paint, Filter filter) {
        if (paint != null) {
            int color = paint.getColor();
            setFilterColor(paint, filter);
            canvas.drawTextIntoBoundary(text, boundary, paint);
            paint.setColor(color);
        }
    }

    private static void setFilterColor(Paint paint, Filter filter) {
        if (paint != null) {
            paint.setColor(GraphicUtils.filterColor(paint.getColor(), filter));
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
}

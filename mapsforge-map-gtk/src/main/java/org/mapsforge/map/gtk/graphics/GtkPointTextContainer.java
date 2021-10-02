package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;

public class GtkPointTextContainer extends PointTextContainer {
    /**
     * Create a new point container, that holds the x-y coordinates of a point, a text variable, two paint objects, and
     * a reference on a symbolContainer, if the text is connected with a POI.
     *
     * @param point
     * @param display
     * @param priority
     * @param text
     * @param paintFront
     * @param paintBack
     * @param symbolContainer
     * @param position
     * @param maxTextWidth
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
        this.boundary = computeBoundary();
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
        GtkCanvas gtkCanvas = (GtkCanvas) canvas;
        final Point pointAdjusted = this.xy.offset(-origin.x, -origin.y);

        showBoundry(canvas, boundary, pointAdjusted);

        if (isMultiLine()) {
            drawMultiLine(gtkCanvas, pointAdjusted, filter);
        } else {
            drawSingleLine(gtkCanvas, pointAdjusted, filter);
        }
    }

    private void showBoundry(Canvas canvas, Rectangle boundary, Point offset) {
        final Rectangle rect = boundary.shift(offset);

        Path path = GtkGraphicFactory.INSTANCE.createPath();
        path.moveTo((float) rect.left, (float)rect.top);
        path.lineTo((float)rect.right, (float)rect.top);
        path.lineTo((float)rect.right, (float)rect.bottom);
        path.lineTo((float)rect.left, (float)rect.bottom);
        path.lineTo((float)rect.left, (float)rect.top);
        Paint paint = GtkGraphicFactory.INSTANCE.createPaint();
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2f);
        canvas.drawPath(path, paint);
    }

    private void drawMultiLine(GtkCanvas gtkCanvas, Point point, Filter filter) {
        System.out.println("GtkPaintTextContainer::drawMultiLine");
        System.out.println(text);
    }


    private void drawSingleLine(GtkCanvas canvas, Point point, Filter filter) {
        drawSingleLine(canvas,point,filter,paintBack);
        drawSingleLine(canvas,point,filter,paintFront);
    }

    private void drawSingleLine(GtkCanvas canvas, Point point, Filter filter, Paint paint) {
        if (paint != null) {
            int color = paint.getColor();
            setFilterColor(paint, filter);
            canvas.drawText(this.text, (int) (point.x + boundary.left), (int) (point.y + boundary.top + this.textHeight), paint);
            paint.setColor(color);
        }

    }
    private static void setFilterColor(Paint paint, Filter filter) {
        if (paint != null) {
            paint.setColor(GraphicUtils.filterColor(paint.getColor(),filter));
        }
    }


    private boolean isMultiLine() {
        return textWidth > maxTextWidth;
    }
    private Rectangle computeBoundary() {
        int lines = this.textWidth / maxTextWidth + 1;
        double boxWidth = this.textWidth;
        double boxHeight = this.textHeight;

        if (lines > 1) {
            // a crude approximation of the size of the text box
            boxWidth = maxTextWidth;
            boxHeight = this.textHeight * lines;
        }

        switch (this.position) {
            case CENTER:
                return new Rectangle(-boxWidth / 2f, -boxHeight / 2f, boxWidth / 2f, boxHeight / 2f);
            case BELOW:
                return new Rectangle(-boxWidth / 2f, 0, boxWidth / 2f, boxHeight);
            case BELOW_LEFT:
                return new Rectangle(-boxWidth, 0, 0, boxHeight);
            case BELOW_RIGHT:
                return new Rectangle(0, 0, boxWidth, boxHeight);
            case ABOVE:
                return new Rectangle(-boxWidth / 2f, -boxHeight, boxWidth / 2f, 0);
            case ABOVE_LEFT:
                return new Rectangle(-boxWidth, -boxHeight, 0, 0);
            case ABOVE_RIGHT:
                return new Rectangle(0, -boxHeight, boxWidth, 0);
            case LEFT:
                return new Rectangle(-boxWidth, -boxHeight / 2f, 0, boxHeight / 2f);
            case RIGHT:
            default:
                return new Rectangle(0, -boxHeight / 2f, boxWidth, boxHeight / 2f);
        }
    }
}

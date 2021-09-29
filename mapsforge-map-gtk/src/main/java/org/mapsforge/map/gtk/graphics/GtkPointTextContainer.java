package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

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
    protected GtkPointTextContainer(Point point, Display display, int priority, String text, Paint paintFront, Paint paintBack, SymbolContainer symbolContainer, Position position, int maxTextWidth) {
        super(point, display, priority, text, paintFront, paintBack, symbolContainer, position, maxTextWidth);
        this.boundary = computeBoundary();
    }

    @Override
    public void draw(Canvas canvas, Point origin, Matrix matrix, Filter filter) {
        Point point = this.xy.offset(-origin.x, -origin.y);

        GtkCanvas gtkCanvas = (GtkCanvas) canvas;
        gtkCanvas.drawText(text, (int)point.x,(int)point.y, paintBack);
        gtkCanvas.drawText(text, (int)point.x,(int)point.y, paintFront);
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
                return new Rectangle(0, -boxHeight / 2f, boxWidth, boxHeight / 2f);
            default:
                break;
        }
        return null;
    }
}

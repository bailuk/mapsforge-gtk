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
package org.mapsforge.map.gtk.view;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.IMapViewPosition;

import java.awt.Point;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gdk.EventButton;
import ch.bailu.gtk.gdk.EventMask;
import ch.bailu.gtk.gdk.EventMotion;
import ch.bailu.gtk.gdk.EventScroll;
import ch.bailu.gtk.gdk.EventType;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gdk.ModifierType;
import ch.bailu.gtk.gdk.ScrollDirection;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Widget;

public class MouseEvents implements Widget.OnScrollEvent, Widget.OnMotionNotifyEvent, Widget.OnButtonPressEvent, Widget.OnButtonReleaseEvent {

    private final MapView mapView;
    private Point lastDragPoint = null;


    public MouseEvents(MapView mapView, DrawingArea drawingArea) {
        this.mapView = mapView;

        drawingArea.onScrollEvent(this);
        drawingArea.onMotionNotifyEvent(this);
        drawingArea.onButtonPressEvent(this);
        drawingArea.onButtonReleaseEvent(this);

        drawingArea.addEvents(EventMask.BUTTON1_MOTION_MASK);
        drawingArea.addEvents(EventMask.SCROLL_MASK);
        drawingArea.addEvents(EventMask.BUTTON_PRESS_MASK);
        drawingArea.addEvents(EventMask.BUTTON_RELEASE_MASK);

    }


    @Override
    public int onMotionNotifyEvent(EventMotion eventMotion) {
            if ((eventMotion.getFieldState() & ModifierType.BUTTON1_MASK) != 0) {
                int x = (int) eventMotion.getFieldX();
                int y = (int) eventMotion.getFieldY();
                Point point = new Point(x,y);
                if (lastDragPoint != null) {
                    int moveHorizontal = point.x - lastDragPoint.x;
                    int moveVertical = point.y - lastDragPoint.y;
                    mapView.getModel().mapViewPosition.moveCenter(moveHorizontal, moveVertical);
                }
                lastDragPoint = point;
                return GTK.TRUE;
            }
            return GTK.FALSE;
    }

    @Override
    public int onScrollEvent(EventScroll eventScroll) {
        int direction = eventScroll.getFieldDirection();

        if (direction == ScrollDirection.UP) {
            zoomInAndCenter((int)eventScroll.getFieldX(), (int)eventScroll.getFieldY());
            return GTK.TRUE;

        } else if (direction == ScrollDirection.DOWN) {
            zoomOut();
            return GTK.TRUE;

        }
        return GTK.FALSE;
    }



    @Override
    public int onButtonPressEvent(EventButton eventButton) {

        if (eventButton.getFieldButton() == GdkConstants.BUTTON_PRIMARY) {
            if (eventButton.getFieldType() == EventType.DOUBLE_BUTTON_PRESS) {
                zoomInAndCenter((int)eventButton.getFieldX(), (int)eventButton.getFieldY());
                lastDragPoint = null;
                return GTK.TRUE;
            }
            lastDragPoint = new Point((int)eventButton.getFieldX(), (int)eventButton.getFieldY());
            return GTK.TRUE;
        }
        return GTK.FALSE;
    }

    @Override
    public int onButtonReleaseEvent(EventButton eventButton) {
        if (eventButton.getFieldButton() == GdkConstants.BUTTON_PRIMARY) {
            lastDragPoint = null;
            return GTK.TRUE;
        }
        return GTK.FALSE;
    }



    private void zoomInAndCenter(int x, int y) {
        IMapViewPosition mapViewPosition = this.mapView.getModel().mapViewPosition;
        if (mapViewPosition.getZoomLevel() < mapViewPosition.getZoomLevelMax()) {
            org.mapsforge.core.model.Point center = this.mapView.getModel().mapViewDimension.getDimension().getCenter();
            byte zoomLevelDiff = 1;
            double moveHorizontal = (center.x - x) / Math.pow(2, zoomLevelDiff);
            double moveVertical = (center.y - y) / Math.pow(2, zoomLevelDiff);
            LatLong pivot = this.mapView.getMapViewProjection().fromPixels(x, y);
            if (pivot != null) {
                mapViewPosition.setPivot(pivot);
                mapViewPosition.moveCenterAndZoom(moveHorizontal, moveVertical, zoomLevelDiff);
            }
        }
    }

    private void zoomOut() {
        IMapViewPosition mapViewPosition = this.mapView.getModel().mapViewPosition;

        if (mapViewPosition.getZoomLevel() > mapViewPosition.getZoomLevelMin()) {
            mapView.getModel().mapViewPosition.zoom((byte) -1);
        }
    }

}

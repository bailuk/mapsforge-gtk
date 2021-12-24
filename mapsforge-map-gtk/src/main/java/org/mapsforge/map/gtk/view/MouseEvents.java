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

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.EventControllerMotion;
import ch.bailu.gtk.gtk.EventControllerScroll;
import ch.bailu.gtk.gtk.EventControllerScrollFlags;
import ch.bailu.gtk.gtk.GestureClick;
import ch.bailu.gtk.gtk.GestureDrag;

public class MouseEvents {

    private final MapView mapView;

    private double mouseX = 0;
    private double mouseY = 0;
    private double dragX = 0;
    private double dragY = 0;

    public MouseEvents(MapView mapView, DrawingArea drawingArea) {
        this.mapView = mapView;

        var scroller = new EventControllerScroll(EventControllerScrollFlags.VERTICAL | EventControllerScrollFlags.DISCRETE);
        drawingArea.addController(scroller);
        scroller.onScroll((dx, dy) -> {
            if (dy < 0) {
                zoomInAndCenter(mouseX, mouseY);
                return GTK.TRUE;

            } else if (dy > 0) {
                zoomOut();
                return GTK.TRUE;

            }
            return GTK.FALSE;
        });


        var motion = new EventControllerMotion();
        drawingArea.addController(motion);
        motion.onMotion((x, y) -> {
            mouseX = x;
            mouseY = y;
        });

        var drag = new GestureDrag();
        drawingArea.addController(drag);
        drag.onDragBegin((start_x, start_y) -> {
            dragX = mouseX;
            dragY = mouseY;
        });

        drag.onDragUpdate((offset_x, offset_y) -> {
            double deltaX = mouseX - dragX;
            double deltaY = mouseY - dragY;

            mapView.getModel().mapViewPosition.moveCenter(deltaX, deltaY);
            dragX = mouseX;
            dragY = mouseY;
        });

        var click = new GestureClick();
        drawingArea.addController(click);
        click.onReleased((n_press, x, y) -> {
            if (n_press == 2) {
                zoomInAndCenter(x, y);
            }
        });
    }

    private void zoomInAndCenter(double x, double y) {
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

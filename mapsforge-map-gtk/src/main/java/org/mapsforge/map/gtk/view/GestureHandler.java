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
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.IMapViewPosition;

import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.EventControllerMotion;
import ch.bailu.gtk.gtk.EventControllerScroll;
import ch.bailu.gtk.gtk.EventControllerScrollFlags;
import ch.bailu.gtk.gtk.GestureClick;
import ch.bailu.gtk.gtk.GestureDrag;
import ch.bailu.gtk.gtk.GestureZoom;

public class GestureHandler {

    private final MapView mapView;

    private double mouseX = 0;
    private double mouseY = 0;
    private double dragX = 0;
    private double dragY = 0;

    private double scaleFactor = 0;

    private boolean singleClick = false;

    public GestureHandler(MapView mapView, DrawingArea drawingArea) {
        this.mapView = mapView;

        // Two finger zoom
        var gestureZoom = new GestureZoom();
        drawingArea.addController(gestureZoom);
        gestureZoom.onScaleChanged(scale -> {
            mapView.getModel().mapViewPosition.setScaleFactorAdjustment(scale);
            scaleFactor = scale;
        });

        gestureZoom.onEnd(sequence -> {
            double zoomLevelOffset = Math.log(scaleFactor) / Math.log(2);
            byte zoomLevelDiff;

            if (Math.abs(zoomLevelOffset) > 1) {
                zoomLevelDiff = (byte) Math.round(zoomLevelOffset < 0 ? Math.floor(zoomLevelOffset) : Math.ceil(zoomLevelOffset));
            } else {
                zoomLevelDiff = (byte) Math.round(zoomLevelOffset);
            }

            mapView.getModel().mapViewPosition.zoom(zoomLevelDiff);
        });


        // Mouse wheel zoom
        var scroller = new EventControllerScroll(EventControllerScrollFlags.VERTICAL | EventControllerScrollFlags.DISCRETE);
        drawingArea.addController(scroller);
        scroller.onScroll((dx, dy) -> {
            if (dy < 0) {
                zoomInAndCenter(mouseX, mouseY);
                return true;

            } else if (dy > 0) {
                zoomOut();
                return true;

            }
            return false;
        });


        // Get mouse position for scroll event (mouse wheel zoom)
        var motion = new EventControllerMotion();
        drawingArea.addController(motion);
        motion.onMotion((x, y) -> {
            mouseX = x;
            mouseY = y;
        });


        // Mouse and touch drag (move center of map)
        var drag = new GestureDrag();
        drag.setButton(0);
        drawingArea.addController(drag);
        drag.onDragBegin((start_x, start_y) -> {
            dragX = 0;
            dragY = 0;
        });

        drag.onDragUpdate((offset_x, offset_y) -> {
            double deltaX = offset_x - dragX;
            double deltaY = offset_y - dragY;

            mapView.getModel().mapViewPosition.moveCenter(deltaX, deltaY);
            dragX += deltaX;
            dragY += deltaY;
        });


        // mouse and touch "click"
        var click = new GestureClick();
        drawingArea.addController(click);
        click.onPressed((i, v, v1) -> singleClick = i==1);
        click.onStopped(() -> singleClick = false);
        click.onReleased((n_press, x, y) -> {
            if (n_press == 2) {
                zoomInAndCenter(x, y);

            } else if (n_press == 1 && singleClick) {
                Point tapXY = new Point(x, y);
                LatLong tapGeo = toLatLong(tapXY);

                for (int i = mapView.getLayerManager().getLayers().size()-1; i>=0; --i) {
                    final Layer layer = mapView.getLayerManager().getLayers().get(i);
                    layer.onTap(tapGeo, toPixel(layer.getPosition()), tapXY);
                }
            }
        });

    }

    private LatLong toLatLong(Point point) {
        if (point != null) {
            return mapView.getMapViewProjection().fromPixels(point.x, point.y);
        }
        return null;
    }

    private Point toPixel(LatLong latLong) {
        if (latLong != null) {
            return mapView.getMapViewProjection().toPixels(latLong);
        }
        return null;
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

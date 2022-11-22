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
package org.mapsforge.samples.gtk.config;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.model.Model;
import org.mapsforge.samples.gtk.SampleApp;
import org.mapsforge.samples.gtk.lib.FileDialog;

import java.io.File;

import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.ClassHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;

public class Config {

    private final MapView mapView;
    private final LayerConfig layerConfig;
    private final Window window;
    private final ActionHelper actionHelper;

    public Config(String[] args, Window window, Application app, MapView mapView) {
        this.window = window;
        this.mapView = mapView;
        this.layerConfig = new LayerConfig(args, mapView);
        if (!layerConfig.haveVectorMap()) {
            PreferencesHelper.setBoolean(Key.enableVectorMap, true);
        }

        actionHelper = new ActionHelper(app);
        actionHelper.onActivate(Key.openVectorMap, this::openVectorMap);
        actionHelper.onActivate(Key.frameVectorMap, this::frameVectorMap);
        actionHelper.onActivate(Key.dumpActionHandler, () -> ActionHandler.dump(System.out));
        actionHelper.onActivate(Key.dumpCallbackHandler, () -> CallbackHandler.dump(System.out));
        actionHelper.onActivate(Key.dumpSignalHandler, () -> SignalHandler.dump(System.out));
        actionHelper.onActivate(Key.dumpClassHandler, () -> ClassHandler.dump(System.out));

        actionHelper.onToggle(Key.scale, false, this::setScaleBar);
        actionHelper.onToggle(Key.displayCoordinates, false, this::setCoordLayer);
        actionHelper.onToggle(Key.displayGrid, false, this::setGridLayer);
        actionHelper.onToggle(Key.displayFpsCounter, false, this::setFpsLayer);
        actionHelper.onToggle(Key.enableVectorMap, false, this::initMapLayer);
        actionHelper.onToggle(Key.enableDrawDebug, false, this::setDrawDebug);
    }

    public void openVectorMap() {
        new FileDialog().title("Open vector map").onResponse((path) -> {
            if (layerConfig.setVectorMap(new File(path))) {
                PreferencesHelper.setString(Key.vectorMapPath, path);
                window.setTitle(SampleApp.APP_NAME + layerConfig.getTitleExtra());
                initMapLayer(true);
                actionHelper.setBoolean(Key.vectorMapPath, true);
            }
        }).show(window);
    }

    public void setDrawDebug(boolean on) {
        GtkGraphicFactory.DRAW_DEBUG = on;
    }

    public void setFpsLayer(boolean v) {
        layerConfig.setFpsLayer(v);
    }

    public void setGridLayer(boolean v) {
        layerConfig.setGridLayer(v);
    }


    public void setCoordLayer(boolean v) {
        layerConfig.setCoordLayer(v);
    }

    public void initMapView() {
        GtkGraphicFactory.DRAW_DEBUG = PreferencesHelper.getBoolean(Key.enableDrawDebug);

        mapView.getModel().init(PreferencesHelper.PREFERENCES);
        initMapLayer(PreferencesHelper.getBoolean(Key.enableVectorMap));
        setScaleBar(PreferencesHelper.getBoolean(Key.scale));
        setGridLayer(PreferencesHelper.getBoolean(Key.displayGrid));
        setCoordLayer(PreferencesHelper.getBoolean(Key.displayCoordinates));
        setFpsLayer(PreferencesHelper.getBoolean(Key.displayFpsCounter));
    }

    public void initMapLayer(boolean vector) {
        if (vector && layerConfig.haveVectorMap()) {
            layerConfig.setVectorMap(true);
            layerConfig.setRasterMap(false);
            frameVectorMap();
        } else {
            layerConfig.setVectorMap(false);
            layerConfig.setRasterMap(true);
        }
    }

    public void frameVectorMap() {
        setMapPosition(mapView.getModel(), layerConfig.getInitialBounding());
    }

    public void setScaleBar(boolean v) {
        layerConfig.setScaleBar(v);
    }

    public void save() {
        mapView.getModel().save(PreferencesHelper.PREFERENCES);
        PreferencesHelper.PREFERENCES.save();
    }

    private void setMapPosition(Model model, BoundingBox boundingBox) {
        if (model != null && boundingBox != null) {
            final Dimension dimension = model.mapViewDimension.getDimension();
            final LatLong center = model.mapViewPosition.getCenter();
            byte zoomLevel = model.mapViewPosition.getZoomLevel();
            int tileSize = model.displayModel.getTileSize();

            if (center != null && dimension != null && dimension.height> 0 && dimension.width > 0) {
                if (zoomLevel == 0 || !boundingBox.contains(center)) {
                    zoomLevel = LatLongUtils.zoomForBounds(dimension, boundingBox, tileSize);
                    model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
                }
            }
        }
    }
}

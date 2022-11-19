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
package org.mapsforge.samples.gtk;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.PreferencesFacade;

import java.util.prefs.Preferences;

public class Config {

    private final MapView mapView ;
    private final LayerConfig layerConfig;

    private final PreferencesFacade preferences = new JavaPreferences(Preferences.userNodeForPackage(SampleApp.class));


    public Config(String[] args, MapView mapView) {
        this.mapView = mapView;
        this.layerConfig = new LayerConfig(args, mapView);
        if (!layerConfig.haveVectorMap()) {
            rasterMapOn.set(true);
        }
        GtkGraphicFactory.DRAW_DEBUG = debugOn.get();
    }

    public void setMenus(Menus menus) {
        System.out.println("Config::setMenus()");

        menus.setChecked("scale",  scaleBarOn.get());
        menus.setChecked("coord",  coordLayerOn.get());
        menus.setChecked("grid",   gridLayerOn.get());
        menus.setChecked("fps",    fpsLayerOn.get());
        menus.setChecked("raster", rasterMapOn.get());
        menus.setChecked("debug",  debugOn.get());
    }

    private class Bool {
        private final String key;
        private final boolean def;
        public Bool(String key, boolean def) {
            this.def = def;
            this.key = key;
        }
        private boolean hasChanged(boolean val) {
            return get() != val;
        }
        public boolean get() {
            return preferences.getBoolean(key, def);
        }
        public boolean set(boolean val) {
            if (hasChanged(val)) {
                preferences.putBoolean(key, val);
                return true;
            }
            return false;
        }
    }


    private final Bool scaleBarOn = new Bool("scaleBarOn", false);
    private final Bool coordLayerOn = new Bool("coordLayerOn", false);
    private final Bool gridLayerOn = new Bool("gridLayerOn", false);
    private final Bool fpsLayerOn = new Bool("fpsLayerOn", false);
    private final Bool rasterMapOn = new Bool("rasterMapOn", true);
    private final Bool debugOn = new Bool("debugOn", true);

    public void setVectorMap() {
        if (layerConfig.haveVectorMap()) {
            if (rasterMapOn.set(false))
                initMapLayer();
        }
    }

    public void setRasterMap() {
        if (rasterMapOn.set(true)) {
            initMapLayer();
        }
    }

    public void setFpsLayer(boolean on) {
        if (fpsLayerOn.set(on)) {
            setFpsLayer();
        }
    }

    public void setDrawDebug(boolean on) {
        debugOn.set(on);
        GtkGraphicFactory.DRAW_DEBUG = on;
    }

    private void setFpsLayer() {
        layerConfig.setFpsLayer(fpsLayerOn.get());
    }

    public void setGridLayer(boolean on) {
        if (gridLayerOn.set(on)) {
            setGridLayer();
        }
    }

    private void setGridLayer() {
        layerConfig.setGridLayer(gridLayerOn.get());
    }

    public void setCoordLayer(boolean on) {
        if (coordLayerOn.set(on)) {
            setCoordLayer();
        }
    }

    private void setCoordLayer() {
        layerConfig.setCoordLayer(coordLayerOn.get());
    }

    public void setScaleBar(boolean on) {
        if (scaleBarOn.set(on)) {
            setScaleBar();
        }
    }

    public void initMapView() {
        mapView.getModel().init(preferences);
        initMapLayer();

        setScaleBar();
        setGridLayer();
        setCoordLayer();
        setFpsLayer();
    }

    private void initMapLayer() {
        if (rasterMapOn.get()) {
            layerConfig.setVectorMap(false);
            layerConfig.setRasterMap(true);
        } else if (layerConfig.haveVectorMap()) {
            layerConfig.setRasterMap(false);
            layerConfig.setVectorMap(true);
        } else {
            layerConfig.setVectorMap(false);
            layerConfig.setRasterMap(true);
        }
        setMapPosition(mapView.getModel(), layerConfig.getInitialBounding());
    }

    public void setScaleBar() {
        layerConfig.setScaleBar(scaleBarOn.get());
    }

    public void save() {
        mapView.getModel().save(preferences);
        preferences.save();
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

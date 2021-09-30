package org.mapsforge.samples.gtk;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.PreferencesFacade;

import java.util.prefs.Preferences;

import ch.bailu.gtk.GTK;

public class Config {

    private final MapView mapView ;
    private final LayerConfig layerConfig;

    private final PreferencesFacade preferences = new JavaPreferences(Preferences.userNodeForPackage(Samples.class));


    public Config(String[] args, MapView mapView) {
        this.mapView = mapView;
        this.layerConfig = new LayerConfig(args, mapView);
    }

    public void setMenus(Menus menus) {
        menus.scale.setActive(GTK.is(scaleBarOn.get()));
        menus.coords.setActive(GTK.is(coordsLayerOn.get()));
        menus.grid.setActive(GTK.is(gridLayerOn.get()));
        menus.fps.setActive(GTK.is(fpsLayerOn.get()));
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
    private final Bool coordsLayerOn = new Bool("coordsLayerOn", false);
    private final Bool gridLayerOn = new Bool("gridLayerOn", false);
    private final Bool fpsLayerOn = new Bool("fpsLayerOn", false);


    public void setFpsLayer(boolean on) {
        if (fpsLayerOn.set(on)) {
            setFpsLayer();
        }
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

    public void setCoordsLayer(boolean on) {
        if (coordsLayerOn.set(on)) {
            setCoordsLayer();
        }
    }

    private void setCoordsLayer() {
        layerConfig.setCoordsLayer(coordsLayerOn.get());
    }

    public void setScaleBar(boolean on) {
        if (scaleBarOn.set(on)) {
            setScaleBar();
        }
    }

    public void initMapView() {
        final BoundingBox boundingBox = layerConfig.initLayers();
        mapView.getModel().init(preferences);
        setMapPosition(mapView.getModel(), boundingBox);

        setScaleBar();
        setGridLayer();
        setCoordsLayer();
        setFpsLayer();
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

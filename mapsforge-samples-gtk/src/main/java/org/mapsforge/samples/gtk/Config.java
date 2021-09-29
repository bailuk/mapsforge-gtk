package org.mapsforge.samples.gtk;

import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.view.MapView;

import java.util.prefs.Preferences;

import ch.bailu.gtk.GTK;

public class Config {

    private MapView mapView = null;
    private final PreferencesFacade preferences = new JavaPreferences(Preferences.userNodeForPackage(Samples.class));

    private Samples.Menus menus;

    public void setMenus(Samples.Menus menus) {
        this.menus = menus;

        if (menus != null) {
            menus.scale.setActive(GTK.is(scaleBarOn.get()));
        }
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

    public void setScaleBar(boolean on) {
        if (scaleBarOn.set(on)) {
            setScaleBar();
        }
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        mapView.getModel().init(preferences);
        setScaleBar();
    }

    public void setScaleBar() {
        if (mapView != null) {
            mapView.getMapScaleBar().setVisible(scaleBarOn.get());
            mapView.repaint();
        }
    }

    public void save() {
        if (mapView != null) mapView.getModel().save(preferences);
        preferences.save();
    }
}

package org.mapsforge.samples.gtk.config;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.samples.gtk.SampleApp;

import java.util.Objects;
import java.util.prefs.Preferences;

public class PreferencesHelper {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ZOOM_LEVEL = "zoomLevel";

    private static final JavaPreferences PREFERENCES = new JavaPreferences(Preferences.userNodeForPackage(SampleApp.class));

    public static boolean hasChanged(Key key, boolean val) {
        return getBoolean(key) != val;
    }

    public static boolean getBoolean(Key key, boolean initial) {
        return PREFERENCES.getBoolean(key.name(), initial);
    }

    public static boolean getBoolean(Key key) {
        return PREFERENCES.getBoolean(key.name(), false);
    }

    public static boolean setBoolean(Key key, boolean val) {
        if (hasChanged(key, val)) {
            PREFERENCES.putBoolean(key.name(), val);
            return true;
        }
        return false;
    }

    public static boolean hasChanged(Key key, String val) {
        return !Objects.equals(getString(key), val);
    }

    private static boolean hasChanged(Key key, int val) {
        return getInt(key, -1) != val;
    }

    public static String getString(Key key) {
        return PREFERENCES.getString(key.name(), "");
    }

    public static boolean setString(Key key, String val) {
        if (hasChanged(key, val)) {
            PREFERENCES.putString(key.name(), val);
            return true;
        }
        return false;
    }
    public static int getInt(Key key, int val) {
        return PREFERENCES.getInt(key.name(), val);
    }

    public static boolean setInt(Key key, int val) {
        if (hasChanged(key, val)) {
            PREFERENCES.putInt(key.name(), val);
            return true;
        }
        return false;
    }

    public static MapPosition getMapViewPosition() {
        double latitude = PREFERENCES.getDouble(LATITUDE, 0);
        double longitude = PREFERENCES.getDouble(LONGITUDE, 0);
        byte zoomLevel = PREFERENCES.getByte(ZOOM_LEVEL, (byte) 0);
        return new MapPosition(new LatLong(latitude, longitude), zoomLevel);
    }

    public static void setMapViewPosition(MapViewPosition mapViewPosition) {
        PREFERENCES.putDouble(LATITUDE, mapViewPosition.getCenter().latitude);
        PREFERENCES.putDouble(LONGITUDE, mapViewPosition.getCenter().longitude);
        PREFERENCES.putByte(ZOOM_LEVEL, mapViewPosition.getZoomLevel());
    }

    public static void save() {
        PREFERENCES.save();
    }
}

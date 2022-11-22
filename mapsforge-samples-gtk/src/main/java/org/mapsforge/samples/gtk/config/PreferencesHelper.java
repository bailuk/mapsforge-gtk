package org.mapsforge.samples.gtk.config;

import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.samples.gtk.SampleApp;

import java.util.Objects;
import java.util.prefs.Preferences;

public class PreferencesHelper {

    public static final PreferencesFacade PREFERENCES = new JavaPreferences(Preferences.userNodeForPackage(SampleApp.class));

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
}

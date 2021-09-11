package org.mapsforge.map.gtk.util;

import org.mapsforge.map.model.common.PreferencesFacade;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class JavaPreferences implements PreferencesFacade {
    private final Preferences preferences;

    public JavaPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public synchronized void clear() {
        try {
            this.preferences.clear();
        } catch (BackingStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public synchronized boolean getBoolean(String key, boolean defaultValue) {
        return this.preferences.getBoolean(key, defaultValue);
    }

    @Override
    public synchronized byte getByte(String key, byte defaultValue) {
        int intValue = this.preferences.getInt(key, defaultValue);
        if (intValue < Byte.MIN_VALUE || intValue > Byte.MAX_VALUE) {
            throw new IllegalStateException("byte value out of range: " + intValue);
        }
        return (byte) intValue;
    }

    @Override
    public synchronized double getDouble(String key, double defaultValue) {
        return this.preferences.getDouble(key, defaultValue);
    }

    @Override
    public synchronized float getFloat(String key, float defaultValue) {
        return this.preferences.getFloat(key, defaultValue);
    }

    @Override
    public synchronized int getInt(String key, int defaultValue) {
        return this.preferences.getInt(key, defaultValue);
    }

    @Override
    public synchronized long getLong(String key, long defaultValue) {
        return this.preferences.getLong(key, defaultValue);
    }

    @Override
    public synchronized String getString(String key, String defaultValue) {
        return this.preferences.get(key, defaultValue);
    }

    @Override
    public synchronized void putBoolean(String key, boolean value) {
        this.preferences.putBoolean(key, value);
    }

    @Override
    public synchronized void putByte(String key, byte value) {
        this.preferences.putInt(key, value);
    }

    @Override
    public synchronized void putDouble(String key, double value) {
        this.preferences.putDouble(key, value);
    }

    @Override
    public synchronized void putFloat(String key, float value) {
        this.preferences.putFloat(key, value);
    }

    @Override
    public synchronized void putInt(String key, int value) {
        this.preferences.putInt(key, value);
    }

    @Override
    public synchronized void putLong(String key, long value) {
        this.preferences.putLong(key, value);
    }

    @Override
    public synchronized void putString(String key, String value) {
        this.preferences.put(key, value);
    }

    @Override
    public synchronized void save() {
        try {
            this.preferences.flush();
        } catch (BackingStoreException e) {
            throw new IllegalStateException(e);
        }
    }
}



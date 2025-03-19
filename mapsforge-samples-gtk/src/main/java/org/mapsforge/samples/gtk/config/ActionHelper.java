package org.mapsforge.samples.gtk.config;

import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;

public class ActionHelper {
    private final Application app;

    public ActionHelper(Application app) {
        this.app = app;
    }

    public SignalHandler onToggle(Key key, boolean initial, ActionHandler.OnToggled signal) {
        return ActionHandler.get(app, key.name(), PreferencesHelper.getBoolean(key, initial)).onToggle((value) -> {
            if (PreferencesHelper.setBoolean(key, value)){
                signal.onActivate(value);
            }
        });
    }

    public SignalHandler onSelect(Key key, ActionHandler.OnChange signal) {
        return ActionHandler.get(app, key.name(), PreferencesHelper.getInt(key, 0)).onChange((value) -> {
            if (PreferencesHelper.setInt(key, value)){
                signal.onActivate(value);
            }
        });
    }

    public SignalHandler onActivate(Key key, ActionHandler.OnActivate signal) {
        return ActionHandler.get(app, key.name()).onActivate(signal);
    }

    public void setBoolean(Key key, boolean value) {
        ActionHandler.get(app, key.name()).changeBoolean(value);
    }

    public void setEnabled(Key key, boolean value) {
        ActionHandler.get(app, key.name()).setEnabled(value);
    }
}

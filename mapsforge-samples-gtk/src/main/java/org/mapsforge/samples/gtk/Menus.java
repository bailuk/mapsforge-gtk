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

import org.mapsforge.samples.gtk.util.MenuHelper;

import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.ClassHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;

public class Menus {

    private final MenuHelper menuHelper;


    public Menus(Config config, Application app) {
        menuHelper = new MenuHelper(app);

        menuHelper.appendToggleItem("Scale bar", "scale", config::setScaleBar);
        menuHelper.appendToggleItem("Raster map", "raster", (isChecked)-> {
            if (isChecked) config.setRasterMap();
            else config.setVectorMap();
        });

        menuHelper.push();
        menuHelper.appendToggleItem("Tile coordinates", "coord", config::setCoordLayer);
        menuHelper.appendToggleItem("Show grid", "grid", config::setGridLayer);
        menuHelper.appendToggleItem("Fps counter", "fps", config::setFpsLayer);
        menuHelper.appendToggleItem("Draw debug structures", "debug", config::setDrawDebug);
        menuHelper.appendSection("Debug");

        menuHelper.push();
        menuHelper.appendItem("ActionHandler", "dump-action-handler", () -> ActionHandler.dump(System.out));
        menuHelper.appendItem("CallbackHandler", "dump-callback-handler", () -> CallbackHandler.dump(System.out));
        menuHelper.appendItem("SignalHandler", "dump-signal-handler", () -> SignalHandler.dump(System.out));
        menuHelper.appendItem("ClassHandler", "dump-class-handler", () -> ClassHandler.dump(System.out));
        menuHelper.appendSection("Dump resources");
        config.setMenus(this);
    }

    public void setChecked(String id, boolean isChecked) {
        menuHelper.setChecked(id, isChecked);
    }

    public MenuHelper getMenuHelper() {
        return menuHelper;
    }


}

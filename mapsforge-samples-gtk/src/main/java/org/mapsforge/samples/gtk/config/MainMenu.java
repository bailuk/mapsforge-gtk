/*
 * Copyright 2021-2025 Lukas Bai
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

import org.mapsforge.map.rendertheme.internal.MapsforgeThemes;
import org.mapsforge.samples.gtk.lib.MenuHelper;

import ch.bailu.gtk.gtk.MenuButton;

public class MainMenu {
    private final MenuHelper menuHelper;

    public MainMenu() {
        menuHelper = new MenuHelper();

        menuHelper.appendItem("Scale bar", Key.scale);

        menuHelper.push();
        menuHelper.appendItem("Open...", Key.openVectorMap);
        menuHelper.appendItem("Enable", Key.enableVectorMap);
        menuHelper.appendItem("Frame", Key.frameMap);
        menuHelper.appendItem("Center", Key.centerMap);

        menuHelper.push();

        for (int i = 0; i < MapsforgeThemes.values().length; i++) {
            menuHelper.appendItem(MapsforgeThemes.values()[i].name(), Key.mapTheme, i);
        }
        menuHelper.appendSubmenu("Theme");
        menuHelper.appendSection("Vector map");

        menuHelper.push();
        menuHelper.appendItem("Tile coordinates", Key.displayCoordinates);
        menuHelper.appendItem("Show grid", Key.displayGrid);
        menuHelper.appendItem("Fps counter", Key.displayFpsCounter);
        menuHelper.appendItem("Draw debug", Key.enableDrawDebug);
        menuHelper.appendItem("Show inspector...", Key.showInspector);

        menuHelper.push();
        menuHelper.appendItem("ActionHandler",Key.dumpActionHandler);
        menuHelper.appendItem("CallbackHandler", Key.dumpCallbackHandler);
        menuHelper.appendItem("SignalHandler", Key.dumpSignalHandler);
        menuHelper.appendItem("ClassHandler", Key.dumpClassHandler);
        menuHelper.appendSubmenu("Dump resources");
        menuHelper.appendSection("Debug");

        menuHelper.push();
        menuHelper.appendItem("About...", Key.about);
        menuHelper.appendSection();
    }

    public MenuButton createMenuButton() {
        return menuHelper.createMenuButton();
    }
}

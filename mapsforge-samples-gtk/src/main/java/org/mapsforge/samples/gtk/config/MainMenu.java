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

import org.mapsforge.samples.gtk.lib.MenuHelper;

import ch.bailu.gtk.gtk.MenuButton;

public class MainMenu {
    private final MenuHelper menuHelper;

    public MainMenu() {
        menuHelper = new MenuHelper();

        menuHelper.appendToggleItem("Scale bar", Key.scale);
        menuHelper.appendItem("Frame map", Key.frameMap);
        menuHelper.appendItem("Center map", Key.centerMap);

        menuHelper.push();
        menuHelper.appendItem("Open...", Key.openVectorMap);
        menuHelper.appendToggleItem("Enable", Key.enableVectorMap);
        menuHelper.appendSection("Vector map");

        menuHelper.push();
        menuHelper.appendToggleItem("Tile coordinates", Key.displayCoordinates);
        menuHelper.appendToggleItem("Show grid", Key.displayGrid);
        menuHelper.appendToggleItem("Fps counter", Key.displayFpsCounter);
        menuHelper.appendToggleItem("Draw debug", Key.enableDrawDebug);
        menuHelper.appendItem("Show inspector...", Key.showInspector);
        menuHelper.appendSection("Debug");

        menuHelper.push();
        menuHelper.appendItem("About...", Key.about);
        menuHelper.appendSection();

        menuHelper.push();
        menuHelper.appendItem("ActionHandler",Key.dumpActionHandler);
        menuHelper.appendItem("CallbackHandler", Key.dumpCallbackHandler);
        menuHelper.appendItem("SignalHandler", Key.dumpSignalHandler);
        menuHelper.appendItem("ClassHandler", Key.dumpClassHandler);
        menuHelper.appendSection("Dump resources");
    }

    public MenuButton createMenuButton() {
        return menuHelper.createMenuButton();
    }
}

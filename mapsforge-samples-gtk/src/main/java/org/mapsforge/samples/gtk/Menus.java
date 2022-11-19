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

import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.PopoverMenu;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;

public class Menus {

    private final MenuHelper menuHelper;


    public Menus(Config config, Application app) {
        menuHelper = new MenuHelper(app);

        menuHelper.addToggleItem("Scale bar", "scale", config::setScaleBar);
        menuHelper.addToggleItem("Tile coordinates", "coord", config::setCoordLayer);
        menuHelper.addToggleItem("Show grid", "grid", config::setGridLayer);
        menuHelper.addToggleItem("Fps counter", "fps", config::setFpsLayer);
        menuHelper.addToggleItem("Draw debug structures", "debug", config::setDrawDebug);
        menuHelper.addToggleItem("Raster map",
                "raster", (isChecked)-> {
            if (isChecked) config.setRasterMap();
            else config.setVectorMap();
        });

        menuHelper.addItem("Dump resources", "dump", () -> {
            ActionHandler.dump(System.out);
            CallbackHandler.dump(System.out);
            SignalHandler.dump(System.out);
        });
        config.setMenus(this);
    }

    public void setChecked(String id, boolean isChecked) {
        menuHelper.setChecked(id, isChecked);
    }

    public MenuHelper getMenuHelper() {
        return menuHelper;
    }


    public static class MenuHelper {
        private final Menu menu = new Menu();
        private final Application app;

        public MenuHelper(Application app) {
            this.app = app;
        }

        void addToggleItem(String label, String id, ActionHandler.OnToggled onToggled) {
            menu.appendItem(new MenuItem(label, "app." + id));
            ActionHandler.get(app, id, true).onToggle(onToggled);
        }

        void addItem(String label, String id, ActionHandler.OnActivate onActivate) {
            menu.appendItem(new MenuItem(label, "app." + id));
            ActionHandler.get(app, id).onActivate(onActivate);
        }

        public MenuButton createMenuButton() {
            var result = new MenuButton();
            result.setPopover(PopoverMenu.newFromModelPopoverMenu(menu));
            return result;
        }

        public void setChecked(String actionName, boolean state) {
            ActionHandler.get(app, actionName).changeBoolean(state);
        }
    }
}

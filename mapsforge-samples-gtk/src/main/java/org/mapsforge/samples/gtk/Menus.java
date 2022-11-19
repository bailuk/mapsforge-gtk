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
import ch.bailu.gtk.lib.handler.action.ActionHandler;

public class Menus {

    private final MenuHelper menuHelper;


    public Menus(Config config, Application app) {
        menuHelper = new MenuHelper(app);

        menuHelper.addCheckBoxMenu("Scale bar",
                "scale", config::setScaleBar);

        menuHelper.addCheckBoxMenu("Tile coordinates",
                "coord", config::setCoordsLayer);

        menuHelper.addCheckBoxMenu("Show grid",
                "grid", config::setGridLayer);

        menuHelper.addCheckBoxMenu("Fps counter",
                "fps", config::setFpsLayer);

        menuHelper.addCheckBoxMenu("Draw debug structures",
                "debug", config::setDrawDebug);

        menuHelper.addCheckBoxMenu("Raster map",
                "raster", (isChecked)-> {
            if (isChecked) config.setRasterMap();
            else config.setVectorMap();
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

        void addCheckBoxMenu(String label, String id, ActionHandler.OnToggled onToggled) {
            menu.appendItem(new MenuItem(label, "app." + id));
            ActionHandler.get(app, id, true).onToggle(onToggled);
        }

        public MenuButton getMenuButton() {
            var result = new MenuButton();
            result.setPopover(PopoverMenu.newFromModelPopoverMenu(menu));
            return result;
        }

        public interface OnMenuSelected {
            void run();
        }

        public interface OnMenuChecked {
            void run(boolean isChecked);
        }

        public void setChecked(String actionName, boolean state) {
            ActionHandler.get(app, actionName).changeBoolean(state);
        }

    }
}

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

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.PopoverMenu;
import ch.bailu.gtk.helper.ActionHelper;
import ch.bailu.gtk.type.Str;

public class Menus {

    private final MenuHelper menuHelper;


    public Menus(Config config, ActionMap actionMap) {
        System.out.println("Menus::Menus()");

        menuHelper = new MenuHelper(actionMap, "app");

        menuHelper.addCheckBoxMenu("Scale bar",
                "scale", (isChecked)-> config.setScaleBar(isChecked));

        menuHelper.addCheckBoxMenu("Tile coordinates",
                "coord", (isChecked)-> config.setCoordsLayer(isChecked));

        menuHelper.addCheckBoxMenu("Show grid",
                "grid", (isChecked)-> config.setGridLayer(isChecked));

        menuHelper.addCheckBoxMenu("Fps counter",
                "fps", (isChecked)-> config.setFpsLayer(isChecked));

        menuHelper.addCheckBoxMenu("Draw debug structures",
                "debug", (isChecked)-> config.setDrawDebug(isChecked));

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
        private final ActionHelper actionHelper;
        private final Menu menu = new Menu();
        private final String name;

        public MenuHelper(ActionMap actionMap, String name) {
            this.name = name;
            actionHelper = new ActionHelper(actionMap, name);
        }

        void addCheckBoxMenu(String label, String id, OnMenuChecked onMenuChecked) {
            menu.appendItem(new MenuItem(new Str(label), new Str(this.name + "." + id)));
            actionHelper.addBoolean(id, GTK.TRUE, (x) -> onMenuChecked.run(actionHelper.toggleChecked(id)));
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

        public void setChecked(String action, boolean enabled) {
            actionHelper.setChecked(action, enabled);
        }

    }
}

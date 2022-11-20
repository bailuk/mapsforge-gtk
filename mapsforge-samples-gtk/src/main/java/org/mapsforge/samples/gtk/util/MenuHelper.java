package org.mapsforge.samples.gtk.util;

import java.util.Stack;

import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.PopoverMenu;
import ch.bailu.gtk.lib.handler.action.ActionHandler;

public class MenuHelper {
    private final Application app;
    private final Stack<Menu> menu = new Stack<>();

    public MenuHelper(Application app) {
        push();
        this.app = app;
    }

    public void appendToggleItem(String label, String id, ActionHandler.OnToggled onToggled) {
        menu.peek().appendItem(new MenuItem(label, "app." + id));
        ActionHandler.get(app, id, true).onToggle(onToggled);
    }

    public void appendItem(String label, String id, ActionHandler.OnActivate onActivate) {
        menu.peek().appendItem(new MenuItem(label, "app." + id));
        ActionHandler.get(app, id).onActivate(onActivate);
    }

    public MenuButton createMenuButton() {
        var result = new MenuButton();
        result.setPopover(PopoverMenu.newFromModelPopoverMenu(menu.firstElement()));
        return result;
    }

    public void setChecked(String actionName, boolean state) {
        ActionHandler.get(app, actionName).changeBoolean(state);
    }

    public void push() {
        var menu = new Menu();
        this.menu.push(menu);
    }

    public void appendSection(String name) {
        if (menu.size() > 1) {
            var menu = this.menu.pop();
            this.menu.peek().appendSection(name, menu);
        }
    }

    public void appendSubmenu(String name) {
        if (menu.size() > 1) {
            var menu = this.menu.pop();
            this.menu.peek().appendSubmenu(name, menu);
        }
    }
}

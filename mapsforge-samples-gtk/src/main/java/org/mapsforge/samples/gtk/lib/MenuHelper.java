package org.mapsforge.samples.gtk.lib;

import org.mapsforge.samples.gtk.config.Key;

import java.util.Stack;

import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.PopoverMenu;
import ch.bailu.gtk.type.Str;

public class MenuHelper {
    private final Stack<Menu> menu = new Stack<>();

    public MenuHelper() {
        push();
    }

    public void appendToggleItem(String label, Key id) {
        menu.peek().appendItem(new MenuItem(label, "app." + id));
    }

    public void appendItem(String label, Key id) {
        menu.peek().appendItem(new MenuItem(label, "app." + id));
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

    public void appendSection() {
        if (menu.size() > 1) {
            var menu = this.menu.pop();
            this.menu.peek().appendSection(Str.NULL, menu);
        }
    }

    public void appendSubmenu(String name) {
        if (menu.size() > 1) {
            var menu = this.menu.pop();
            this.menu.peek().appendSubmenu(name, menu);
        }
    }

    public MenuButton createMenuButton() {
        var result = new MenuButton();
        result.setPopover(PopoverMenu.newFromModelPopoverMenu(menu.firstElement()));
        return result;
    }
}

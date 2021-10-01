/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2014 Christian Pesch
 * Copyright 2014 Ludwig M Brinckmann
 * Copyright 2014-2020 devemux86
 * Copyright 2017 usrusr
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

import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.gtk.view.MapView;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.IconSize;
import ch.bailu.gtk.gtk.Image;
import ch.bailu.gtk.gtk.Menu;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.wrapper.Str;
import ch.bailu.gtk.wrapper.Strs;

public final class Samples {

    private final static String APP_ID = "org.mapsforge.samples.gtk";
    private final static String APP_NAME = "Mapsforge GTK3 Sample";
    private final static String APP_ICON = "../docs/logo/Mapsforge.svg";


     /**
     * Starts the {@code Samples}.
     *
     * @param args command line args: expects the map files as multiple parameters
     *             with possible SRTM hgt folder as 1st argument.
     */
    public static void main(final String[] args) throws IOException {
        GTK.init();
        Parameters.SQUARE_FRAME_BUFFER = false;
        new Samples(args);
    }

    private Samples(String args[]) {
        final var app = new Application(new Str(APP_ID), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> onActivate(new ApplicationWindow(app), args));
        app.run(1, new Strs(new String[]{APP_NAME}));
    }



    public void onActivate(ApplicationWindow window, String[] args) {
        final var mapView = new MapView();
        final var config = new Config(args, mapView);

        window.setTitlebar(createHeader(mapView, new Menus(config).menu));

        try  {
            window.setIcon(Pixbuf.newFromFilePixbuf(new Str(APP_ICON)));
        } catch (AllocationError e) {
            System.out.println(e.getMessage());
        }

        window.setDefaultSize(1024, 768);

        window.onShow(() -> {
            config.initMapView();
        });

        window.onDestroy(() -> {
            config.save();
            System.exit(0);
        });
        window.add(mapView.getDrawingArea());
        window.showAll();
    }


    private HeaderBar createHeader(MapView mapView, Menu menu) {
        final var header = new HeaderBar();
        header.setShowCloseButton(1);
        header.setTitle(new Str(APP_NAME));

        final var menuButton = new MenuButton();
        menuButton.add(Image.newFromIconNameImage(new Str("open-menu-symbolic"), IconSize.BUTTON));
        menuButton.setPopup(menu);
        header.packEnd(menuButton);

        var box = new Box(Orientation.HORIZONTAL, 0);
        box.getStyleContext().addClass(new Str("linked"));

        final var zoomIn = new Button();
        zoomIn.add(Image.newFromIconNameImage(new Str("zoom-in-symbolic"), IconSize.BUTTON));
        zoomIn.onClicked(() -> mapView.getModel().mapViewPosition.zoomIn());
        box.add(zoomIn);

        final var zoomOut = new Button();
        zoomOut.add(Image.newFromIconNameImage(new Str("zoom-out-symbolic"), IconSize.BUTTON));
        zoomOut.onClicked(() -> mapView.getModel().mapViewPosition.zoomOut());
        box.add(zoomOut);

        header.packStart(box);
        return header;
    }

}

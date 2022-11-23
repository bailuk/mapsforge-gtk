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
import org.mapsforge.samples.gtk.config.Config;
import org.mapsforge.samples.gtk.config.MainMenu;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;


public final class SampleApp {

    private final static Str APP_ID   = new Str("org.mapsforge.samples.gtk");
    public  final static Str APP_NAME = new Str("Mapsforge GTK4 Sample");


     /**
     * Starts the {@code Samples}.
     *
     * @param args command line args: expects the map files as multiple parameters
     *             with possible SRTM hgt folder as 1st argument.
     */
    public static void main(final String[] args) {
        Parameters.SQUARE_FRAME_BUFFER = false;
        new SampleApp(args);
    }

    private SampleApp(String[] args) {
        final Application app = new Application(APP_ID, ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> onActivate(new ApplicationWindow(app), app, args));
        app.run(1, new Strs(new Str[]{APP_NAME}));
    }

    public void onActivate(ApplicationWindow window, Application app, String[] args) {
        final MapView mapView = new MapView();
        final Config config = new Config(args, window, app, mapView);

        window.setTitlebar(createHeader(mapView, new MainMenu().createMenuButton()));
        window.setDefaultSize(1024, 768);

        window.onShow(config::initMapView);
        window.onDestroy(() -> {
            config.save();
            System.exit(0);
        });

        window.setChild(mapView.getDrawingArea());
        window.show();

    }


    private HeaderBar createHeader(MapView mapView, MenuButton menuButton) {
        final HeaderBar header = new HeaderBar();
        header.setShowTitleButtons(true);

        header.packEnd(menuButton);

        Box box = new Box(Orientation.HORIZONTAL, 0);
        box.getStyleContext().addClass("linked");

        final Button zoomIn = Button.newFromIconNameButton("zoom-in-symbolic");
        zoomIn.onClicked(() -> mapView.getModel().mapViewPosition.zoomIn());
        box.append(zoomIn);

        final Button zoomOut = Button.newFromIconNameButton("zoom-out-symbolic");
        zoomOut.onClicked(() -> mapView.getModel().mapViewPosition.zoomOut());
        box.append(zoomOut);

        header.packStart(box);
        return header;
    }
}

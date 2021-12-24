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
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.PopoverMenu;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;


public final class SampleApp {

    static {
        try {
            GTK.init();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private final static Str APP_ID   = new Str("org.mapsforge.samples.gtk");
    private final static Str APP_NAME = new Str("Mapsforge GTK4 Sample");
    private final static Str APP_ICON = new Str("../docs/logo/Mapsforge.svg");


     /**
     * Starts the {@code Samples}.
     *
     * @param args command line args: expects the map files as multiple parameters
     *             with possible SRTM hgt folder as 1st argument.
     */
    public static void main(final String[] args) throws IOException {
        Parameters.SQUARE_FRAME_BUFFER = false;
        new SampleApp(args);
    }

    private SampleApp(String args[]) {
        final Application app = new Application(APP_ID, ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> onActivate(new ApplicationWindow(app), new ActionMap(app.cast()), args));
        app.run(1, new Strs(new Str[]{APP_NAME}));
    }



    public void onActivate(ApplicationWindow window, ActionMap actionMap, String[] args) {
        final MapView mapView = new MapView();
        final Config config = new Config(args, mapView);

        window.setTitle(APP_NAME);

        System.out.println("SampleApp::onActivate()");

        window.setTitlebar(createHeader(mapView, new Menus(config, actionMap).getMenuHelper()));

/*
        try  {
            window.setIcon(Pixbuf.newFromFilePixbuf(APP_ICON));
        } catch (AllocationError e) {
            System.out.println(e.getMessage());
        }
*/
        window.setDefaultSize(1024, 768);

        window.onShow(() -> {
            System.out.println("SampleApp::onShow()");
            config.initMapView();
        });

        window.onDestroy(() -> {
            System.out.println("SampleApp::onDestroy()");
            config.save();
            System.exit(0);
        });

        window.setChild(mapView.getDrawingArea());
        window.show();

    }


    private HeaderBar createHeader(MapView mapView, Menus.MenuHelper menu) {
        System.out.println("SampleApp::createHeader()");
        final HeaderBar header = new HeaderBar();
        header.setShowTitleButtons(GTK.TRUE);

        final MenuButton menuButton = menu.getMenuButton();
        header.packEnd(menuButton);

        Box box = new Box(Orientation.HORIZONTAL, 0);
        box.getStyleContext().addClass(new Str("linked"));

        final Button zoomIn = Button.newFromIconNameButton(new Str("zoom-in-symbolic"));
        zoomIn.onClicked(() -> mapView.getModel().mapViewPosition.zoomIn());
        box.append(zoomIn);

        final Button zoomOut = Button.newFromIconNameButton(new Str("zoom-out-symbolic"));
        zoomOut.onClicked(() -> mapView.getModel().mapViewPosition.zoomOut());
        box.append(zoomOut);

        header.packStart(box);
        return header;
    }

}

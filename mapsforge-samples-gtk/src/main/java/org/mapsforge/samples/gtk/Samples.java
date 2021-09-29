/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2014 Christian Pesch
 * Copyright 2014 Ludwig M Brinckmann
 * Copyright 2014-2020 devemux86
 * Copyright 2017 usrusr
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

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.model.Model;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Icon;
import ch.bailu.gtk.gio.ThemedIcon;
import ch.bailu.gtk.glib.SList;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.CheckMenuItem;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.IconSize;
import ch.bailu.gtk.gtk.Image;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Menu;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.RadioMenuItem;
import ch.bailu.gtk.gtk.SeparatorMenuItem;
import ch.bailu.gtk.gtk.VBox;
import ch.bailu.gtk.wrapper.Str;
import ch.bailu.gtk.wrapper.Strs;

public final class Samples {

    private final LayerConfig layerConfig;
    private final Config config = new Config();

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
        final Application app = new Application(new Str("org.mapsforge.samples.gtk"), ApplicationFlags.FLAGS_NONE);
        layerConfig = new LayerConfig(args);

        app.onActivate(() -> {
            onActivate(new ApplicationWindow(app));
        });
        app.run(args.length, new Strs(args));
    }



    public void onActivate(ApplicationWindow window) {
        var mapView = createMapView();
        var header = createHeader(mapView);

        try  {
            window.setIcon(Pixbuf.newFromFilePixbuf(new Str("../docs/logo/Mapsforge.svg")));
        } catch (AllocationError e) {
            System.out.println(e.getMessage());
        }
        window.setTitlebar(header);
        window.setDefaultSize(1024, 768);

        window.onShow(() -> {
            final BoundingBox boundingBox = layerConfig.initLayers(mapView);
            config.setMapView(mapView);
            setMapPosition(mapView.getModel(), boundingBox);
        });

        window.onDestroy(() -> {
            config.save();
            System.exit(0);
        });
        window.setBorderWidth(0);
        window.add(mapView.getDrawingArea());
        window.showAll();
    }


    private HeaderBar createHeader(MapView mapView) {
        var header = new HeaderBar();
        header.setShowCloseButton(1);
        header.setTitle(new Str("Mapsforge GTK Sample application"));
        header.setHasSubtitle(0);
        var button = new MenuButton();
        var icon = new ThemedIcon(new Str("open-menu-symbolic"));
        var image = Image.newFromGiconImage(new Icon(icon.getCPointer()), IconSize.BUTTON);
        icon.unref();

        var menu = createMenu(mapView);

        button.add(image);
        button.setPopup(menu);
        header.packEnd(button);

        var box = new Box(Orientation.HORIZONTAL, 0);
        box.getStyleContext().addClass(new Str("linked"));

        var button1 = new Button();
        button1.add(Image.newFromIconNameImage(new Str("zoom-in-symbolic"), IconSize.BUTTON));
        button1.onClicked(() -> mapView.getModel().mapViewPosition.zoomIn());
        box.add(button1);

        var button2 = new Button();
        button2.add(Image.newFromIconNameImage(new Str("zoom-out-symbolic"), IconSize.BUTTON));
        button2.onClicked(() -> mapView.getModel().mapViewPosition.zoomOut());
        box.add(button2);

        header.packStart(box);
        return header;
    }

    public class Menus {
        public final Menu menu;
        public final CheckMenuItem scale;

        public Menus() {
            menu = new Menu();

            var raster = new RadioMenuItem(new SList(0));
            var render = new RadioMenuItem(raster.getGroup());
            raster.setLabel(new Str("Raster map"));
            render.setLabel(new Str("Vector map"));
            menu.append(raster);
            menu.append(render);

            var separator = new SeparatorMenuItem();
            menu.append(separator);
            scale = new CheckMenuItem();
            scale.setLabel(new Str("Scale bar"));
            scale.onToggled(() -> {
                config.setScaleBar(GTK.is(scale.getActive()));
            });
            menu.append(scale);

            var coords = new CheckMenuItem();
            coords.setLabel(new Str("Tile coordinates layer"));
            coords.onToggled(() -> {
                layerConfig.setCoordsLayer(coords.getActive());
            });
            menu.append(coords);

            var grid = new CheckMenuItem();
            grid.setLabel(new Str("Tile grid layer"));
            grid.onToggled(() -> {
                layerConfig.setGridLayer(coords.getActive());
            });
            menu.append(grid);

            menu.showAll();
        }
    }
    private Menu createMenu(MapView mapView) {
        Menus menus = new Menus();

        config.setMenus(menus);
        return menus.menu;

    }


    private void setMapPosition(Model model, BoundingBox boundingBox) {
        if (model != null && boundingBox != null) {
            final Dimension dimension = model.mapViewDimension.getDimension();
            final LatLong center = model.mapViewPosition.getCenter();
            byte zoomLevel = model.mapViewPosition.getZoomLevel();
            int tileSize = model.displayModel.getTileSize();

            if (center != null && dimension != null && dimension.height> 0 && dimension.width > 0) {
                if (zoomLevel == 0 || !boundingBox.contains(center)) {
                    zoomLevel = LatLongUtils.zoomForBounds(dimension, boundingBox, tileSize);
                    model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
                }
            }
        }
    }

    private static MapView createMapView() {
        return new MapView();
    }

}

package org.mapsforge.samples.gtk;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.glib.SList;
import ch.bailu.gtk.gtk.CheckMenuItem;
import ch.bailu.gtk.gtk.Menu;
import ch.bailu.gtk.gtk.RadioMenuItem;
import ch.bailu.gtk.gtk.SeparatorMenuItem;
import ch.bailu.gtk.type.Str;

public class Menus {

    public final Menu menu;
    public final CheckMenuItem scale;
    public final CheckMenuItem coords;
    public final CheckMenuItem grid;
    public final CheckMenuItem fps;
    public final CheckMenuItem debug;
    public final RadioMenuItem raster, vector;

    public Menus(Config config) {
        menu = new Menu();

        raster = new RadioMenuItem(new SList(0));
        vector = new RadioMenuItem(raster.getGroup());
        raster.setLabel(new Str("Raster map"));
        raster.onToggled(() -> {
            if (GTK.is(raster.getActive())) config.setRasterMap();
        });

        vector.setLabel(new Str("Vector map"));
        vector.onToggled(() -> {
            if (GTK.is(vector.getActive())) config.setVectorMap();
        });

        menu.append(raster);
        menu.append(vector);

        SeparatorMenuItem separator = new SeparatorMenuItem();
        menu.append(separator);
        scale = new CheckMenuItem();
        scale.setLabel(new Str("Scale bar"));
        scale.onToggled(() -> config.setScaleBar(GTK.is(scale.getActive())));
        menu.append(scale);

        fps = new CheckMenuItem();
        fps.setLabel(new Str("Fps counter"));
        fps.onToggled(() -> config.setFpsLayer(GTK.is(fps.getActive())));
        menu.append(fps);

        coords = new CheckMenuItem();
        coords.setLabel(new Str("Tile coordinates layer"));
        coords.onToggled(() -> config.setCoordsLayer(GTK.is(coords.getActive())));
        menu.append(coords);

        grid = new CheckMenuItem();
        grid.setLabel(new Str("Tile grid layer"));
        grid.onToggled(() -> config.setGridLayer(GTK.is(grid.getActive())));
        menu.append(grid);

        debug = new CheckMenuItem();
        debug.setLabel(new Str("Draw debug structures"));
        debug.onToggled((() -> config.setDrawDebug(GTK.is(debug.getActive()))));
        menu.append(debug);

        menu.showAll();
        config.setMenus(this);
    }

}

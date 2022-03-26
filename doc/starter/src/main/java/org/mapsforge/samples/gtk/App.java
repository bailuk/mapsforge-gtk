package org.mapsforge.samples.gtk;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.gtk.util.TileCacheUtil;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class App {
    public static void main(String args[]) {
        var app = new Application(new Str("com.example.gtk.app"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            var mapView = new MapView();
            window.setChild(mapView.getDrawingArea());
            window.setTitle(new Str("Map"));
            window.setSizeRequest(500,500);

            window.onShow(() -> {
                OpenStreetMapMapnik.INSTANCE.setUserAgent("mapsforge-samples-gtk");

                var tileCache = TileCacheUtil.createTileCache(mapView.getModel());
                var tileDownloadLayer = new TileDownloadLayer(tileCache, mapView.getModel().mapViewPosition, OpenStreetMapMapnik.INSTANCE, GtkGraphicFactory.INSTANCE);

                mapView.getModel().displayModel.setFixedTileSize(256); // FIXME only works with this tile size
                mapView.getLayerManager().getLayers().add(tileDownloadLayer);
                tileDownloadLayer.start();

                mapView.setZoomLevelMin(OpenStreetMapMapnik.INSTANCE.getZoomLevelMin());
                mapView.setZoomLevelMax(OpenStreetMapMapnik.INSTANCE.getZoomLevelMax());

                mapView.setZoomLevel((byte)14);
                mapView.getModel().mapViewPosition.setCenter(new LatLong(47.35,7.9));

            });

            window.onDestroy(() -> System.exit(0));

            window.show();
        });
        app.run(args.length, new Strs(args));
    }
}

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

public class HelloMap {
    public static void main(String[] args) {
        final Application app = new Application(new Str("org.mapsforge.samples.hello-map"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            final MapView mapView = new MapView();

            initDownloadTilesMap(mapView);
            mapView.setZoomLevel((byte) 1);
            window.setTitle("Hello Map");
            window.setSizeRequest(400,400);
            window.setChild(mapView.getDrawingArea());
            window.onDestroy(()->System.exit(0));
            window.show();
        });

        app.run(args.length, new Strs(args));
    }


    private static void initDownloadTilesMap(MapView mapView) {
        var layers = mapView.getLayerManager().getLayers();
        var tileSource = OpenStreetMapMapnik.INSTANCE;
        var tileCache = TileCacheUtil.createTileCache(mapView.getModel());
        var tileDownloadLayer = new TileDownloadLayer(tileCache, mapView.getModel().mapViewPosition,
                tileSource, GtkGraphicFactory.INSTANCE);

        mapView.getModel().displayModel.setFixedTileSize(256);
        mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
        mapView.setZoomLevelMax(tileSource.getZoomLevelMax());

        layers.add(0,tileDownloadLayer);
        tileSource.setUserAgent("mapsforge-samples-gtk");
        tileDownloadLayer.start();
    }
}

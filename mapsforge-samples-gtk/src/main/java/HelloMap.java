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
    private final static Str APP_ID = new Str("org.mapsforge.samples.hello_map");
    private final static Str TITLE = new Str("Hello map");

    private final static String USER_AGENT = "mapsforge-samples-gtk";
    private final static int TILE_SIZE = 256;

    public static void main(String[] args) {
        final Application app = new Application(APP_ID, ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            final var window = new ApplicationWindow(app);
            final var mapView = new MapView();

            initDownloadTilesMap(mapView);
            mapView.setZoomLevel((byte) 1);
            window.setTitle(TITLE);
            window.setSizeRequest(400,400);
            window.setChild(mapView.getDrawingArea());
            window.onDestroy(mapView::destroy);
            window.onDestroy(()->System.exit(0)); // TODO Download thread does not terminate in mapsforge
            window.show();
        });

        app.run(args.length, new Strs(args));
    }


    private static void initDownloadTilesMap(MapView mapView) {
        final var tileSource = OpenStreetMapMapnik.INSTANCE;
        final var tileCache = TileCacheUtil.createTileCache(mapView.getModel());
        final var tileDownloadLayer = new TileDownloadLayer(tileCache, mapView.getModel().mapViewPosition,
                tileSource, GtkGraphicFactory.INSTANCE);
        final var layers = mapView.getLayerManager().getLayers();

        layers.add(0,tileDownloadLayer);

        mapView.getModel().displayModel.setFixedTileSize(TILE_SIZE);
        mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
        mapView.setZoomLevelMax(tileSource.getZoomLevelMax());

        tileSource.setUserAgent(USER_AGENT);
        tileDownloadLayer.start();
    }
}

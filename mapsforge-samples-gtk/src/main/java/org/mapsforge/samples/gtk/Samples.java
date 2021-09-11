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

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
// TODO import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.gtk.util.JavaPreferences;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.hills.DiffuseLightShadingAlgorithm;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.hills.MemoryCachingHgtReaderTileSource;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import javax.swing.*;
import javax.swing.text.View;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Widget;

public final class Samples {
    private static final GraphicFactory GRAPHIC_FACTORY = GtkGraphicFactory.INSTANCE;
    private static final boolean SHOW_DEBUG_LAYERS = false;
    private static final boolean SHOW_RASTER_MAP = false;

    private static final String MESSAGE = "Are you sure you want to exit the application?";
    private static final String TITLE = "Confirm close";

    /**
     * Starts the {@code Samples}.
     *
     * @param args command line args: expects the map files as multiple parameters
     *             with possible SRTM hgt folder as 1st argument.
     */
    public static void main(String[] args) throws IOException {
        GTK.init();

        // Square frame buffer
        Parameters.SQUARE_FRAME_BUFFER = false;

        HillsRenderConfig hillsCfg = null;
        File demFolder = getDemFolder(args);
        if (demFolder != null) {
            MemoryCachingHgtReaderTileSource tileSource = new MemoryCachingHgtReaderTileSource(demFolder, new DiffuseLightShadingAlgorithm(), GRAPHIC_FACTORY);
            tileSource.setEnableInterpolationOverlap(true);
            hillsCfg = new HillsRenderConfig(tileSource);
            hillsCfg.indexOnThread();
            args = Arrays.copyOfRange(args, 1, args.length);
        }

        List<File> mapFiles = SHOW_RASTER_MAP ? null : getMapFiles(args);
        final MapView mapView = createMapView();
        //final BoundingBox boundingBox = addLayers(mapView, mapFiles, hillsCfg);

        final PreferencesFacade preferencesFacade = new JavaPreferences(Preferences.userNodeForPackage(Samples.class));

        final Application app = new Application("org.mapsforge.samples.gtk", ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {

            final ApplicationWindow window = new ApplicationWindow(app);
            window.setTitle("Mapsforge Samples");
            window.setSizeRequest(1024, 768);

            // TODO onOpened?
            /*final Model model = mapView.getModel();
             model.init(preferencesFacade);
                    if (model.mapViewPosition.getZoomLevel() == 0 || !boundingBox.contains(model.mapViewPosition.getCenter())) {
                        byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(), boundingBox, model.displayModel.getTileSize());
                        model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
                    }*/
            window.showAll();
        });
        app.run(0, args);
    }

    private static BoundingBox addLayers(MapView mapView, List<File> mapFiles, HillsRenderConfig hillsRenderConfig) {
        Layers layers = mapView.getLayerManager().getLayers();

        int tileSize = SHOW_RASTER_MAP ? 256 : 512;

        // Tile cache
        // TODO TileCache tileCache = AwtUtil.createTileCache(
        // TODO         tileSize,
// TODO                 mapView.getModel().frameBufferModel.getOverdrawFactor(),
// TODO                 1024,
// TODO                 new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        final BoundingBox boundingBox;
        if (SHOW_RASTER_MAP) {
            // Raster
            mapView.getModel().displayModel.setFixedTileSize(tileSize);
            OpenStreetMapMapnik tileSource = OpenStreetMapMapnik.INSTANCE;
            tileSource.setUserAgent("mapsforge-samples-awt");
// TODO             TileDownloadLayer tileDownloadLayer = createTileDownloadLayer(tileCache, mapView.getModel().mapViewPosition, tileSource);
// TODO             layers.add(tileDownloadLayer);
// TODO             tileDownloadLayer.start();
            mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
            mapView.setZoomLevelMax(tileSource.getZoomLevelMax());
            boundingBox = new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN, LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX);
        } else {
            // Vector
            mapView.getModel().displayModel.setFixedTileSize(tileSize);
            MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
            for (File file : mapFiles) {
                mapDataStore.addMapDataStore(new MapFile(file), false, false);
            }
// TODO             TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, hillsRenderConfig);
// TODO             layers.add(tileRendererLayer);
            boundingBox = mapDataStore.boundingBox();
        }

        // Debug
        if (SHOW_DEBUG_LAYERS) {
// TODO             layers.add(new TileGridLayer(GRAPHIC_FACTORY, mapView.getModel().displayModel));
// TODO             layers.add(new TileCoordinatesLayer(GRAPHIC_FACTORY, mapView.getModel().displayModel));
        }

        return boundingBox;
    }

    private static MapView createMapView() {
// TODO         MapView mapView = new MapView();
// TODO         mapView.getMapScaleBar().setVisible(true);
// TODO         if (SHOW_DEBUG_LAYERS) {
// TODO             mapView.getFpsCounter().setVisible(true);
// TODO         }

// TODO         return mapView;
        return null;
    }

    @SuppressWarnings("unused")
    private static TileDownloadLayer createTileDownloadLayer(TileCache tileCache, IMapViewPosition mapViewPosition, TileSource tileSource) {
// TODO         return new TileDownloadLayer(tileCache, mapViewPosition, tileSource, GRAPHIC_FACTORY) {
// TODO             @Override
// TODO             public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
// TODO                 System.out.println("Tap on: " + tapLatLong);
// TODO                 return true;
// TODO             }
// TODO         };
        return null;
    }

    private static TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore, IMapViewPosition mapViewPosition, HillsRenderConfig hillsRenderConfig) {
// TODO         TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false, true, false, GRAPHIC_FACTORY, hillsRenderConfig) {
// TODO             @Override
// TODO             public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
// TODO                 System.out.println("Tap on: " + tapLatLong);
// TODO                 return true;
// TODO             }
// TODO         };
// TODO         tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
// TODO         return tileRendererLayer;
        return null;
    }

    private static File getDemFolder(String[] args) {
        if (args.length == 0) {
            if (SHOW_RASTER_MAP) {
                return null;
            } else {
                throw new IllegalArgumentException("missing argument: <mapFile>");
            }
        }

        File demFolder = new File(args[0]);
        if (demFolder.exists() && demFolder.isDirectory() && demFolder.canRead()) {
            return demFolder;
        }
        return null;
    }

    private static List<File> getMapFiles(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("missing argument: <mapFile>");
        }

        List<File> result = new ArrayList<>();
        for (String arg : args) {
            File mapFile = new File(arg);
            if (!mapFile.exists()) {
                throw new IllegalArgumentException("file does not exist: " + mapFile);
            } else if (!mapFile.isFile()) {
                throw new IllegalArgumentException("not a file: " + mapFile);
            } else if (!mapFile.canRead()) {
                throw new IllegalArgumentException("cannot read file: " + mapFile);
            }
            result.add(mapFile);
        }
        return result;
    }

    private Samples() {
        throw new IllegalStateException();
    }
}

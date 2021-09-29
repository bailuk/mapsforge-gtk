package org.mapsforge.samples.gtk;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.gtk.util.GtkUtil;
import org.mapsforge.map.gtk.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.UUID;

import ch.bailu.gtk.GTK;

public class LayerConfig {

    private final MapView mapView;


    private final RenderHillsConfig renderHillsConfig;
    private final RenderMapConfig renderMapConfig;

    public LayerConfig(String[] args, MapView mapView) {
        this.mapView = mapView;
        renderMapConfig = new RenderMapConfig(args);
        renderHillsConfig = new RenderHillsConfig(args);
    }

    private void initMapScaleBar(MapView mapView) {
        mapView.getMapScaleBar().setVisible(true);
        mapView.getFpsCounter().setVisible(true);
    }


    private BoundingBox initRenderMap(MapView mapView) {
        Layers layers = mapView.getLayerManager().getLayers();

        TileCache tileCache = initTileCache(mapView, 512);
        mapView.getModel().displayModel.setFixedTileSize(512);
        MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);

        renderMapConfig.addMapDataStore(mapDataStore);
        TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, renderHillsConfig.getConfig());
        layers.add(tileRendererLayer);
        return mapDataStore.boundingBox();

    }
    private BoundingBox initRasterMap(MapView mapView) {
        Layers layers = mapView.getLayerManager().getLayers();

        TileCache tileCache = initTileCache(mapView, 256);
        mapView.getModel().displayModel.setFixedTileSize(256);
        OpenStreetMapMapnik tileSource = OpenStreetMapMapnik.INSTANCE;
        tileSource.setUserAgent("mapsforge-samples-awt");
        TileDownloadLayer tileDownloadLayer = createTileDownloadLayer(tileCache, mapView.getModel().mapViewPosition, tileSource);
        layers.add(tileDownloadLayer);
        tileDownloadLayer.start();
        mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
        mapView.setZoomLevelMax(tileSource.getZoomLevelMax());
        return new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN, LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX);
    }

    public BoundingBox initLayers() {

        //if (showRasterMap || !renderMapConfig.hasMapFiles()) {
            return initRasterMap(mapView);
        //} else {
        //    return initRenderMap(mapView);
        //}
    }

    private TileCache initTileCache(MapView mapView, int tileSize) {
        return GtkUtil.createTileCache(
                tileSize,
                mapView.getModel().frameBufferModel.getOverdrawFactor(),
                512,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));
    }

    private static TileDownloadLayer createTileDownloadLayer(TileCache tileCache, IMapViewPosition mapViewPosition, TileSource tileSource) {
        return new TileDownloadLayer(tileCache, mapViewPosition, tileSource, GtkGraphicFactory.INSTANCE) {
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                return true;
            }
        };
    }

    private static TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore, IMapViewPosition mapViewPosition, HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false, true, false, GtkGraphicFactory.INSTANCE, hillsRenderConfig) {
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        return tileRendererLayer;
    }

    public void setCoordsLayer(boolean on) {
        if (on) {
            if (!findLayer(TileCoordinatesLayer.class)) {
                mapView.addLayer(new TileCoordinatesLayer(GtkGraphicFactory.INSTANCE, mapView.getModel().displayModel));
            }
        } else {
            removeLayer(TileCoordinatesLayer.class);
        }
    }

    private boolean findLayer(Class clazz) {
        for (Layer layer : mapView.getLayerManager().getLayers()) {
            if (clazz.isInstance(layer)) {
                return true;
            }
        }
        return false;
    }


    private boolean removeLayer(Class clazz) {
        var layers = mapView.getLayerManager().getLayers();
        for (Layer layer : layers) {
            if (clazz.isInstance(layer)) {
                layers.remove(layer);
                return true;
            }
        }
        return false;
    }

    public void setGridLayer(boolean on) {
        if (on) {
            if (!findLayer(TileGridLayer.class)) {
                mapView.addLayer(new TileGridLayer(GtkGraphicFactory.INSTANCE, mapView.getModel().displayModel));
            }
        } else {
            removeLayer(TileGridLayer.class);
        }
    }

     public void setScaleBar(boolean on) {
        mapView.getMapScaleBar().setVisible(on);
        mapView.repaint();
    }

    public void setFpsLayer(boolean on) {
        mapView.getFpsCounter().setVisible(on);
        mapView.repaint();
    }
}

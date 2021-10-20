/*
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

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.gtk.util.TileCacheUtil;
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

public class LayerConfig {

    private final MapView mapView;


    private final HillsConfig hillsConfig;
    private final VectorMapConfig renderMapConfig;

    private BoundingBox boundingBox = null;

    public LayerConfig(String[] args, MapView mapView) {
        this.mapView = mapView;
        renderMapConfig = new VectorMapConfig(args);
        hillsConfig = new HillsConfig(args);
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

    public boolean haveVectorMap() {
        return renderMapConfig.hasMapFiles();
    }

    public void setRasterMap(boolean on) {
        if (on) {
            if (!findLayer(TileDownloadLayer.class)) {
                boundingBox = initRasterMap(mapView);
            }
        } else {
            removeLayer(TileDownloadLayer.class);
        }
    }


    private BoundingBox initRasterMap(MapView mapView) {
        Layers layers = mapView.getLayerManager().getLayers();

        mapView.getModel().displayModel.setFixedTileSize(256);
        TileCache tileCache = TileCacheUtil.createTileCache(mapView.getModel());

        OpenStreetMapMapnik tileSource = OpenStreetMapMapnik.INSTANCE;
        tileSource.setUserAgent("mapsforge-samples-awt");
        TileDownloadLayer tileDownloadLayer = createTileDownloadLayer(tileCache, mapView.getModel().mapViewPosition, tileSource);
        layers.add(0,tileDownloadLayer);
        tileDownloadLayer.start();
        mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
        mapView.setZoomLevelMax(tileSource.getZoomLevelMax());
        return new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN, LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX);
    }

    public void setVectorMap(boolean on) {
        if (on) {
            if (!findLayer(TileRendererLayer.class)) {
                boundingBox = initRenderMap(mapView);
            }
        } else {
            removeLayer(TileRendererLayer.class);
        }

    }

    private BoundingBox initRenderMap(MapView mapView) {
        Layers layers = mapView.getLayerManager().getLayers();

        mapView.getModel().displayModel.setFixedTileSize(512);
        TileCache tileCache = TileCacheUtil.createTileCache(mapView.getModel());
        MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);

        renderMapConfig.addMapDataStore(mapDataStore);
        TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, hillsConfig.getConfig());
        layers.add(0,tileRendererLayer);
        return mapDataStore.boundingBox();
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

    public BoundingBox getInitialBounding() {
        return boundingBox;
    }
}

package org.mapsforge.map.gtk.view;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.scalebar.MapScaleBar;
import org.mapsforge.map.util.MapViewProjection;
import org.mapsforge.map.view.FpsCounter;
import org.mapsforge.map.view.FrameBuffer;

public class MapView implements org.mapsforge.map.view.MapView{
    @Override
    public void addLayer(Layer layer) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void destroyAll() {

    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public Dimension getDimension() {
        return null;
    }

    @Override
    public FpsCounter getFpsCounter() {
        return null;
    }

    @Override
    public FrameBuffer getFrameBuffer() {
        return null;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public LayerManager getLayerManager() {
        return null;
    }

    @Override
    public MapScaleBar getMapScaleBar() {
        return null;
    }

    @Override
    public MapViewProjection getMapViewProjection() {
        return null;
    }

    @Override
    public Model getModel() {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public void repaint() {

    }

    @Override
    public void setCenter(LatLong center) {

    }

    @Override
    public void setMapScaleBar(MapScaleBar mapScaleBar) {

    }

    @Override
    public void setZoomLevel(byte zoomLevel) {

    }

    @Override
    public void setZoomLevelMax(byte zoomLevelMax) {

    }

    @Override
    public void setZoomLevelMin(byte zoomLevelMin) {

    }
}

package org.mapsforge.map.gtk.view;

import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.controller.LayerManagerController;
import org.mapsforge.map.controller.MapViewController;
import org.mapsforge.map.gtk.graphics.GtkGraphicContext;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.labels.LabelStore;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.scalebar.DefaultMapScaleBar;
import org.mapsforge.map.scalebar.MapScaleBar;
import org.mapsforge.map.util.MapPositionUtil;
import org.mapsforge.map.util.MapViewProjection;
import org.mapsforge.map.view.FpsCounter;
import org.mapsforge.map.view.FrameBuffer;
import org.mapsforge.map.view.FrameBufferHA3;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.Pointer;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.gtk.DrawingArea;

public class MapView implements org.mapsforge.map.view.MapView{

    private static final GraphicFactory GRAPHIC_FACTORY = GtkGraphicFactory.INSTANCE;

    private final FpsCounter fpsCounter;
    private final FrameBuffer frameBuffer;
    private final FrameBufferController frameBufferController;
    private final LayerManager layerManager;
    private final MapViewProjection mapViewProjection;
    private final Model model;
    private MapScaleBar mapScaleBar;

    private final DrawingArea drawingArea = new DrawingArea();
    private Dimension dimension = new Dimension(0,0);

    public MapView() {
        this.model = new Model();
        this.fpsCounter = new FpsCounter(GRAPHIC_FACTORY, this.model.displayModel);
        this.frameBuffer = new FrameBufferHA3(this.model.frameBufferModel, this.model.displayModel, GRAPHIC_FACTORY);
        this.frameBufferController = FrameBufferController.create(this.frameBuffer, this.model);
        this.layerManager = new LayerManager(this, this.model.mapViewPosition, GRAPHIC_FACTORY);
        this.layerManager.start();
        LayerManagerController.create(this.layerManager, this.model);
        MapViewController.create(this, this.model);

        this.mapScaleBar = new DefaultMapScaleBar(this.model.mapViewPosition, this.model.mapViewDimension, GRAPHIC_FACTORY,
                this.model.displayModel);

        this.mapViewProjection = new MapViewProjection(this);

        drawingArea.onDraw(context -> {
            final GraphicContext graphicContext = new GtkGraphicContext(context, dimension);

            this.frameBuffer.draw(graphicContext);
            if (this.mapScaleBar != null) {
                this.mapScaleBar.draw(graphicContext);
            }
            this.fpsCounter.draw(graphicContext);

            return GTK.TRUE;
        });


        drawingArea.onSizeAllocate(allocation -> {
            System.out.println(allocation.getFieldX() + ":" + allocation.getFieldY() + ":" + allocation.getFieldWidth() + ":" + allocation.getFieldHeight());
            dimension = new Dimension(allocation.getFieldWidth(), allocation.getFieldHeight());
            if (dimension.width > 0 && dimension.height > 0) {
                model.mapViewDimension.setDimension(dimension);
            }
        });

        new MouseEvents(this, drawingArea);



    }

    @Override
    public void addLayer(Layer layer) {
        this.layerManager.getLayers().add(layer);
    }

    @Override
    public void destroy() {
        this.layerManager.finish();
        this.frameBufferController.destroy();
        this.frameBuffer.destroy();
        if (this.mapScaleBar != null) {
            this.mapScaleBar.destroy();
        }
        this.getModel().mapViewPosition.destroy();
    }

    @Override
    public void destroyAll() {
        for (Layer layer : this.layerManager.getLayers()) {
            this.layerManager.getLayers().remove(layer);
            layer.onDestroy();
            if (layer instanceof TileLayer) {
                ((TileLayer<?>) layer).getTileCache().destroy();
            }
            if (layer instanceof TileRendererLayer) {
                LabelStore labelStore = ((TileRendererLayer) layer).getLabelStore();
                if (labelStore != null) {
                    labelStore.clear();
                }
            }
        }
        destroy();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return MapPositionUtil.getBoundingBox(this.model.mapViewPosition.getMapPosition(),
                getDimension(), this.model.displayModel.getTileSize());
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public FpsCounter getFpsCounter() {
        return this.fpsCounter;
    }

    @Override
    public FrameBuffer getFrameBuffer() {
        return this.frameBuffer;
    }

    @Override
    public int getHeight() {
        return dimension.height;
    }

    @Override
    public LayerManager getLayerManager() {
        return this.layerManager;
    }

    @Override
    public MapScaleBar getMapScaleBar() {
        return this.mapScaleBar;
    }

    @Override
    public MapViewProjection getMapViewProjection() {
        return this.mapViewProjection;
    }

    @Override
    public Model getModel() {
        return this.model;
    }

    @Override
    public int getWidth() {
        return dimension.width;
    }

    private boolean redrawNeeded = false;

    @Override
    public void repaint() {
        redrawNeeded = true;
        Glib.idleAdd(l -> {

            if (redrawNeeded) {
                redrawNeeded = false;
                drawingArea.queueDraw();
            }
            return GTK.FALSE;
        }, 0);
    }

    @Override
    public void setCenter(LatLong center) {
        this.model.mapViewPosition.setCenter(center);
    }

    @Override
    public void setMapScaleBar(MapScaleBar mapScaleBar) {
        if (this.mapScaleBar != null) {
            this.mapScaleBar.destroy();
        }
        this.mapScaleBar = mapScaleBar;
    }


    @Override
    public void setZoomLevel(byte zoomLevel) {
        this.model.mapViewPosition.setZoomLevel(zoomLevel);
    }

    @Override
    public void setZoomLevelMax(byte zoomLevelMax) {
        this.model.mapViewPosition.setZoomLevelMax(zoomLevelMax);
    }

    @Override
    public void setZoomLevelMin(byte zoomLevelMin) {
        this.model.mapViewPosition.setZoomLevelMin(zoomLevelMin);
    }

    public DrawingArea getDrawingArea() {
        return drawingArea;
    }
}

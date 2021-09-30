package org.mapsforge.samples.gtk;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.layer.hills.DiffuseLightShadingAlgorithm;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.hills.MemoryCachingHgtReaderTileSource;

import java.io.File;

public class RenderHillsConfig {

    //private final HillsRenderConfig config;

    private final File folder;


    public RenderHillsConfig(String[] args) {
        folder = getDemFolder(args);
        //config = createHillsCfg(getDemFolder(args), GtkGraphicFactory.INSTANCE);
    }

    private File getDemFolder(String[] args) {
        if (args.length > 0) {
            File demFolder = new File(args[0]);
            if (demFolder.exists() && demFolder.isDirectory() && demFolder.canRead()) {
                return demFolder;
            }
        }
        return null;
    }

    private HillsRenderConfig createHillsCfg(File folder, GraphicFactory factory) {
        HillsRenderConfig result = null;

        if (folder != null) {
            MemoryCachingHgtReaderTileSource tileSource = new MemoryCachingHgtReaderTileSource(folder, new DiffuseLightShadingAlgorithm(), factory);
            tileSource.setEnableInterpolationOverlap(true);
            result = new org.mapsforge.map.layer.hills.HillsRenderConfig(tileSource);
            result.indexOnThread();
        }
        return result;
    }

    /*public HillsRenderConfig getConfig() {
        return config;
    }*/
}

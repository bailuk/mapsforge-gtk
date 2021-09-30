package org.mapsforge.samples.gtk;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.layer.hills.DiffuseLightShadingAlgorithm;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.hills.MemoryCachingHgtReaderTileSource;

import java.io.File;

public class HillsConfig {

    private final File folder;


    public HillsConfig(String[] args) {
        folder = getDemFolder(args);
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

    public HillsRenderConfig getConfig() {
        if (folder != null) {
            return getHillsCfg(folder, GtkGraphicFactory.INSTANCE);
        }
        return null;
    }

    private HillsRenderConfig getHillsCfg(File folder, GraphicFactory factory) {
        MemoryCachingHgtReaderTileSource tileSource = new MemoryCachingHgtReaderTileSource(folder, new DiffuseLightShadingAlgorithm(), factory);
        tileSource.setEnableInterpolationOverlap(true);
        HillsRenderConfig result = new HillsRenderConfig(tileSource);
        result.indexOnThread();
        return result;
    }

}

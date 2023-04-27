/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2014 Christian Pesch
 * Copyright 2014 Ludwig M Brinckmann
 * Copyright 2014-2020 devemux86
 * Copyright 2017 usrusr
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
package org.mapsforge.samples.gtk.config;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.layer.hills.DemFolder;
import org.mapsforge.map.layer.hills.DemFolderFS;
import org.mapsforge.map.layer.hills.DiffuseLightShadingAlgorithm;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.hills.MemoryCachingHgtReaderTileSource;

import java.io.File;

public class HillsConfig {

    private final DemFolder folder;


    public HillsConfig(String[] args) {
        folder = getDemFolder(args);
    }

    private DemFolder getDemFolder(String[] args) {
        if (args.length > 0) {
            File folder = new File(args[0]);
            if (folder.exists() && folder.isDirectory() && folder.canRead()) {
                return new DemFolderFS(folder);
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

    private HillsRenderConfig getHillsCfg(DemFolder folder, GraphicFactory factory) {
        MemoryCachingHgtReaderTileSource tileSource = new MemoryCachingHgtReaderTileSource(folder, new DiffuseLightShadingAlgorithm(), factory);
        tileSource.setEnableInterpolationOverlap(true);
        HillsRenderConfig result = new HillsRenderConfig(tileSource);
        result.indexOnThread();
        return result;
    }

}

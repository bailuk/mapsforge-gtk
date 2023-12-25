/*
 * Copyright 2016-2017 devemux86
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
package org.mapsforge.map.gtk.util;

import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory;
import org.mapsforge.map.layer.cache.FileSystemTileCache;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.model.Model;

import java.io.File;
import java.util.UUID;

import ch.bailu.gtk.gdk.Display;
import ch.bailu.gtk.gdk.Monitor;
import ch.bailu.gtk.gdk.Rectangle;

public class TileCacheUtil {
    private static final int DEFAULT_CAPACITY = 1024;

    /**
     * Utility function to create a two-level tile cache with the provided dimensions.
     * <p>
     * Combine with <code>FrameBufferController.setUseSquareFrameBuffer(false);</code>
     * If GtkGraphicFactory.DRAW_DEBUG is true only a in memory cache will be created.
     *
     * @param tileSize       the tile size
     * @param overdrawFactor the overdraw factor applied to the map view
     * @param capacity       the maximum number of entries in file cache
     * @param cacheDirectory the directory where cached tiles will be stored
     * @return a new cache created on the file system
     */
    public static TileCache createTileCache(int tileSize, double overdrawFactor, int capacity, File cacheDirectory) {
        int cacheSize = getMinimumCacheSize(tileSize, overdrawFactor);

        if (GtkGraphicFactory.DRAW_DEBUG) {
            return new InMemoryTileCache(cacheSize);

        } else {
            TileCache firstLevelTileCache = new InMemoryTileCache(cacheSize);
            TileCache secondLevelTileCache = new FileSystemTileCache(capacity, cacheDirectory, GtkGraphicFactory.INSTANCE);
            return new TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache);
        }
    }

    /**
     * Creating a map cache. Gets the values for the
     * cache dimension from the provided Model. A temporary directory will
     * be used for the second level cache.
     * If GtkGraphicFactory.DRAW_DEBUG is true only a in memory cache will be created.
     * @param model the cache gets created for this models dimensions.
     * @return a tile cache
     */
    public static TileCache createTileCache(Model model) {
        final int tileSize = model.displayModel.getTileSize();
        return createTileCache(
                tileSize,
                model.frameBufferModel.getOverdrawFactor(),
                DEFAULT_CAPACITY,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));
    }

    /**
     * Compute the minimum cache size for a view, using the size of the screen.
     * <p>
     * Combine with <code>FrameBufferController.setUseSquareFrameBuffer(false);</code>
     *
     * @param tileSize       the tile size
     * @param overdrawFactor the overdraw factor applied to the map view
     * @return the minimum cache size for the view
     */
    public static int getMinimumCacheSize(int tileSize, double overdrawFactor) {
        var dimension = getMonitorDimension();
        return (int) Math.max(4, Math.round((2 + dimension.width * overdrawFactor / tileSize)
                * (2 + dimension.height * overdrawFactor / tileSize)));
    }

    private static Dimension getMonitorDimension() {
        final var monitors = Display.getDefault().getMonitors();
        final var rectangle = new Rectangle();
        int width = 800;
        int height = 600;

        for (int i=0; i< monitors.getNItems(); i++) {
            var monitor = new Monitor(monitors.getItem(i).cast());
            monitor.getGeometry(rectangle);
            width = Math.max(width, rectangle.getFieldWidth());
            height = Math.max(height, rectangle.getFieldHeight());
        }
        rectangle.destroy();
        return new Dimension(width, height);
    }

    private TileCacheUtil() {}
}

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
package org.mapsforge.samples.gtk.config;

import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.reader.MapFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VectorMapConfig {
    private final List<File> mapFiles = new ArrayList<>();
    private File mapFile = null;

    public VectorMapConfig(String[] args) {
        for (String arg : args) {
            final File mapFile = new File(arg);
            if (mapFile.exists() && mapFile.isFile() && mapFile.canRead()) {
                System.out.println("Add map file: " + mapFile);
                mapFiles.add(mapFile);
            }
        }
    }

    public boolean setMapFile(File mapFile) {
        if (mapFile.exists() && mapFile.canRead()) {
            this.mapFile = mapFile;
            return true;
        }
        else this.mapFile = null;
        return false;
    }

    public boolean hasMapFiles() {
        return !mapFiles.isEmpty() || mapFile != null;
    }

    public void addMapDataStore(MultiMapDataStore mapDataStore) {
        for (File file : mapFiles) {
            mapDataStore.addMapDataStore(new MapFile(file), false, false);
        }
        if (mapFile != null) {
            mapDataStore.addMapDataStore(new MapFile(mapFile), false, false);
        }
    }

    public String getTitleExtra() {
        if (mapFile != null) {
            return " [" + mapFile.getName() + "]";
        } else if (!mapFiles.isEmpty()) {
            return " [" + mapFiles.get(0).getName() + "]";
        }
        return "";
    }
}

package org.mapsforge.samples.gtk;

import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.reader.MapFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RenderMapConfig {
    private final List<File> mapFiles = new ArrayList<>();

    public RenderMapConfig(String[] args) {
        for (String arg : args) {
            final File mapFile = new File(arg);
            if (mapFile.exists() && mapFile.isFile() && mapFile.canRead()) {
                System.out.println("Add map file: " + mapFile);
                mapFiles.add(mapFile);
            }
        }
    }


    public boolean hasMapFiles() {
        return !mapFiles.isEmpty();
    }

    public void addMapDataStore(MultiMapDataStore mapDataStore) {
        for (File file : mapFiles) {
            mapDataStore.addMapDataStore(new MapFile(file), false, false);
        }
    }
}

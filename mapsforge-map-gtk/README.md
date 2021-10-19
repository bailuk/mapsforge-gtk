# Mapsforge Map GTK
GTK3 based implementation of the Mapsforge MapView. It is made with [java-gtk](https://github.com/bailuk/java-gtk)

## Compile and install
```bash
# build
./gradlew mapsforge-map-gtk:build

# install to local maven repository 
./gradlew mapsforge-map-gtk:install
```

## Integrate using Gradle (build.gradle.kts)
```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("org.mapsforge:mapsforge-map-gtk:master-SNAPSHOT")
}
```

## Hello world
```kotlin
package com.example.gtk.app

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import org.mapsforge.map.gtk.view.MapView
import org.mapsforge.map.layer.Layers
import org.mapsforge.map.layer.cache.FileSystemTileCache
import org.mapsforge.map.layer.cache.InMemoryTileCache
import org.mapsforge.map.layer.cache.TwoLevelTileCache
import org.mapsforge.map.layer.download.TileDownloadLayer
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    GTK.init();

    val app = Application(Str("com.example.gtk.app"), ApplicationFlags.FLAGS_NONE)

    app.onActivate {
        val window = ApplicationWindow(app)
        val mapView = MapView()
        window.add(mapView.drawingArea)
        window.title = Str("MapView")
        window.setSizeRequest(500,500)

        window.onShow {
            val layers: Layers = mapView.layerManager.layers

            val tmpDir = File(System.getProperty("java.io.tmpdir"))
            val firstLevelTileCache = InMemoryTileCache(100)
            val secondLevelTileCache =  FileSystemTileCache(1024, tmpDir, GtkGraphicFactory.INSTANCE)
            val tileCache =  TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache)

            val tileSource = OpenStreetMapMapnik.INSTANCE
            tileSource.userAgent = "com-example-gtk-app"
            val tileDownloadLayer = TileDownloadLayer(tileCache, mapView.model.mapViewPosition, tileSource, GtkGraphicFactory.INSTANCE)

            layers.add(tileDownloadLayer)
            tileDownloadLayer.start()

            mapView.setZoomLevelMin(tileSource.zoomLevelMin)
            mapView.setZoomLevelMax(tileSource.zoomLevelMax)

            mapView.setZoomLevel(14)
            mapView.model.mapViewPosition.center = LatLong(47.35,7.9)
        }

        window.onDestroy {
            exitProcess(0)
        }

        window.showAll()
    }

    app.run(args.size, Strs(args))
}
```

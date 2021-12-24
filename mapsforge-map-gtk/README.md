# Mapsforge Map GTK
GTK4 based implementation of the Mapsforge MapView. It is made with [java-gtk](https://github.com/bailuk/java-gtk)

## Prerequisite
Clone [java-gtk](https://github.com/bailuk/java-gtk) and run `./gradlew publishToMavenLocal` in project root.

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
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import org.mapsforge.map.gtk.util.TileCacheUtil
import org.mapsforge.map.gtk.view.MapView
import org.mapsforge.map.layer.download.TileDownloadLayer
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    GTK.init()

    val app = Application(Str("com.example.gtk.app"), ApplicationFlags.FLAGS_NONE)

    app.onActivate {
        val window = ApplicationWindow(app)
        val mapView = MapView()
        window.setChild(mapView.drawingArea)
        window.title = Str("Map")
        window.setSizeRequest(500,500)

        window.onShow {
            OpenStreetMapMapnik.INSTANCE.userAgent = "mapsforge-samples-gtk"

            val tileCache = TileCacheUtil.createTileCache(mapView.model)
            val tileDownloadLayer = TileDownloadLayer(tileCache, mapView.model.mapViewPosition, OpenStreetMapMapnik.INSTANCE, GtkGraphicFactory.INSTANCE)

            mapView.layerManager.layers.add(tileDownloadLayer)
            tileDownloadLayer.start()

            mapView.setZoomLevelMin(OpenStreetMapMapnik.INSTANCE.zoomLevelMin)
            mapView.setZoomLevelMax(OpenStreetMapMapnik.INSTANCE.zoomLevelMax)

            mapView.setZoomLevel(14)
            mapView.model.mapViewPosition.center = LatLong(47.35,7.9)
        }

        window.onDestroy {
            exitProcess(0)
        }

        window.show()
    }

    app.run(args.size, Strs(args))
}
```

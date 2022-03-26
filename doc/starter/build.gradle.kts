plugins {
    application
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    val mapsForgeVersion: String by project

    // TODO add jitpack url
    implementation("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-themes:$mapsForgeVersion")
}

val appMainClass = "org.mapsforge.samples.gtk.App"

application {
    mainClass.set(appMainClass)
}


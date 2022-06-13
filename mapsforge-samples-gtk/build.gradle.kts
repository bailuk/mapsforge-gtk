plugins {
    application
 }

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

dependencies {
    val mapsForgeVersion: String by project

    implementation(project(":mapsforge-map-gtk"))
    implementation("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-themes:$mapsForgeVersion")

    val junitVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

repositories {
    mavenLocal()
    mavenCentral()
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

val appMainClass = "org.mapsforge.samples.gtk.SampleApp"

application {
    mainClass.set(appMainClass)
}

tasks.run {
    if (this is JavaExec && project.hasProperty("args")) {
        val property = project.properties["args"]
        if (property is String) {
            args(property.split(","))
        }
    }
}


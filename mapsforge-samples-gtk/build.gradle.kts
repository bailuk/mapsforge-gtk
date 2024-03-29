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

    // https://mvnrepository.com/artifact/net.sf.kxml/kxml2
    // xml parser implementation for render theme parsing (used to be part of mapsforge dependency prior 0.19)
    implementation("net.sf.kxml:kxml2:2.3.0")

    val junitVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
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

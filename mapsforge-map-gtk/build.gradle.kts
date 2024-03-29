plugins {
    `java-library`
    `maven-publish`
}


java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

fun getProperty(property: String, default: String) : String {
    var result = default

    if (project.hasProperty(property)) {
        val r = project.property(property)
        if (r is String && r != "unspecified") {
            result = r
        }
    }
    return result
}
project.version = getProperty("version", "SNAPSHOT")
project.group = "org.mapsforge"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {

    val mapsForgeVersion: String by project
    val javaGtkVersion: String by project

    implementation("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")

    // api("ch.bailu.java-gtk:java-gtk:${javaGtkVersion}-SNAPSHOT")
    api("com.github.bailuk:java-gtk:$javaGtkVersion")

    val junitVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")

}

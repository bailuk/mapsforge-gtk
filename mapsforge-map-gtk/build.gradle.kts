plugins {
    `java-library`
    `maven-publish`
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
    implementation("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")

    //api 'ch.bailu.java-gtk:java-gtk:0.1-SNAPSHOT'
    api("com.github.bailuk:java-gtk:0.1")

    val junitVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")

}


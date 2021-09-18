package org.mapsforge.map.gtk.util.color;

public class Conv255 {

    static public int toInt(float c) {
        return (int) (c * 255d);
    }

    static public int toInt(double c) {
        return (int) (c * 255d);
    }

    static public float toFloat(int c) {
        return ( ((float)c) / 255f );
    }

    static public double toDouble(int c) {
        return ( ((double)c) / 255f );
    }
}

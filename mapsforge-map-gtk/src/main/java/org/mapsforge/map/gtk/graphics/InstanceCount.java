package org.mapsforge.map.gtk.graphics;

public class InstanceCount {
    private int count = 0;
    private int rapportCount = 0;

    public void increment() {
        count++;
        rapport();
    }

    public void decrement() {
        count--;
        rapport();
    }


    public void rapport() {
        if (rapportCount < 0) {
            System.out.println("GtkBitmap::instances: " + count);
            rapportCount = 50;
        }
        rapportCount--;

    }

}

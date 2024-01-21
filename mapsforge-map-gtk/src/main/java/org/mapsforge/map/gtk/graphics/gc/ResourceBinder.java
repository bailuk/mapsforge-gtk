package org.mapsforge.map.gtk.graphics.gc;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.Set;

import ch.bailu.gtk.lib.util.SizeLog;

public class ResourceBinder implements Runnable{
    private final SizeLog sizeLog;

    private final Set<BoundResourceReference<Object>> references = new HashSet<>();
    private final ReferenceQueue<Object> reverencesQueue = new ReferenceQueue<>();

    public ResourceBinder(String label) {
        sizeLog = new SizeLog(label);
        final Thread thread = new Thread(this, ResourceBinder.class.getSimpleName());
        thread.setDaemon(true); // Will terminate when application exits
        thread.start();
    }

    public synchronized void bindResource(Object object, BoundResourceHandler recyclerResource) {
        references.add(new BoundResourceReference<>(object, reverencesQueue, recyclerResource));
        sizeLog.log(references.size());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Reference<?> recyclerReference = reverencesQueue.remove();
                freeResource(recyclerReference);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

    private synchronized void freeResource(Reference<?> recyclerReference) throws InterruptedException {
        if (recyclerReference instanceof BoundResourceReference) {
            if (references.remove(recyclerReference)) {
                ((BoundResourceReference<?>) recyclerReference).freeResource();
                sizeLog.log(references.size());
            } else {
                System.err.println("Reference was not in set");
            }
        } else {
            System.err.println("Reference is of invalid type");
        }
    }
}

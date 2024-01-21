package org.mapsforge.map.gtk.graphics.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class BoundResourceReference<T> extends PhantomReference<T> implements BoundResourceHandler {
    private final BoundResourceHandler resource;

    public BoundResourceReference(T referent, ReferenceQueue<T> referenceQueue, BoundResourceHandler resource) {
        super(referent, referenceQueue);
        this.resource = resource;
    }


    @Override
    public void freeResource() {
        resource.freeResource();
    }
}

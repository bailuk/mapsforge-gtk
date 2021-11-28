package org.mapsforge.map.gtk.view;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gdk.EventMask;
import ch.bailu.gtk.gdk.EventType;
import ch.bailu.gtk.gtk.Widget;

/**
 * Register touch events
 * TODO implement zoom and fling
 * https://stackoverflow.com/questions/12520540/multitouch-gtk3-example
 */
public class TouchGestureHandler {
    public TouchGestureHandler(MapView mapView) {
        final Widget da = mapView.getDrawingArea();

        da.addEvents(EventMask.TOUCH_MASK);
        da.onTouchEvent(it -> {
            var event = it.getFieldTouch();

            switch(event.getFieldType()) {
                case EventType.TOUCH_BEGIN:
                case EventType.TOUCH_UPDATE:
                case EventType.TOUCH_END:
                case EventType.TOUCH_CANCEL:

            }
            return GTK.FALSE;
        });

    }

    public void destory() {

    }
}

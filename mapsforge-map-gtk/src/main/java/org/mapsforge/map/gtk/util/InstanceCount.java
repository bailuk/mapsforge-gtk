/*
 * Copyright 2021 Lukas Bai
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.gtk.util;

import org.mapsforge.map.gtk.graphics.GtkBitmap;

import ch.bailu.gtk.lib.util.SizeLog;

/**
 * Count instances of allocated resources. Util class for debugging.
 * This is used to spot memory leaks
 */
public class InstanceCount {
    private final SizeLog sizeLog = new SizeLog(GtkBitmap.class.getSimpleName());
    private int count = 0;

    public synchronized void increment() {
        count++;
        rapport();
    }

    public synchronized void decrement() {
        count--;
        rapport();
    }

    public void rapport() {
        sizeLog.log(count);
    }
}

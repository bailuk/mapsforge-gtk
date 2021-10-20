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
package org.mapsforge.map.gtk.util.color;

public class ARGB implements ColorInterface {

    private int a,r,g,b;

    public ARGB(int color) {
        a = alpha(color);
        r = red(color);
        g = green(color);
        b = blue(color);
    }

    public ARGB(int alpha, int color) {
        a = alpha;
        r = red(color);
        g = green(color);
        b = blue(color);
    }

    public ARGB(int r, int g, int b) {
        this(255,r,g,b);
    }

    public ARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public static int alpha(int color) {
        return (color >> 24) & 0xff;
    }

    public static int red(int color) {
        return (color >> 16) & 0xff;
    }

    public static int green(int color) {
        return (color >> 8) & 0xff;
    }

    public static int blue(int color) {
        return (color) & 0xff;
    }

    @Override
    public int red() {
        return r;
    }

    @Override
    public int green() {
        return g;
    }

    @Override
    public int blue() {
        return b;
    }

    @Override
    public int alpha() {
        return a;
    }

    public int toInt() {
        int result = (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
        return result;
    }

}
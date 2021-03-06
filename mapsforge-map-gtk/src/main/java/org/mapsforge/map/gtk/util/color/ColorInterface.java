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

/**
 * Representation of a color
 */
public interface ColorInterface {

    int GRAY = 0xFF7F7F7F;
    int BLACK = 0xFF000000;
    int MAGENTA = 0xFFFF00FF;
    int WHITE = 0xFFFFFFFF;
    int LTGRAY = 0xFFD3D3D3;
    int DKGRAY = 0xFFD3D3D3;

    int red();
    int green();
    int blue();
    int alpha();

    int toInt();
}

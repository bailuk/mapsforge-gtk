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

import org.mapsforge.core.model.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Split Text into multiple lines and calculate textbox size.
 * This is used to render vector maps
 */
public class MultiLineText {
    private final List<String> lines = new ArrayList<>();

    public MultiLineText(String text, int maxLine) {
        String[] words = text.replace('\n', ' ').replace("  ", " ").split(" ");
        String del = "";
        StringBuilder line = new StringBuilder();
        int length = 0;

        for (String word : words) {
            length += word.length();
            if (length > maxLine && line.length() > 0) {
                length = word.length();
                lines.add(line.toString());
                line.setLength(0);
                del = "";
            }
            line.append(del).append(word);
            del = " ";
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
    }

    public List<String> getLines() {
        return lines;
    }


    public Rectangle getBoundary(int charWidth, int charHeight) {
        return new Rectangle(0,0, charWidth * getMaxLineLength(), charHeight * size());
    }


    private int getMaxLineLength() {
        int result = 0;
        for (String line: lines) {
            result = Math.max(result, line.length());
        }
        return result;
    }

    public int size() {
        return lines.size();
    }
}

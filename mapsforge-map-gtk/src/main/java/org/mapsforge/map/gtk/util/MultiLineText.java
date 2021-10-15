package org.mapsforge.map.gtk.util;

import org.mapsforge.core.model.Rectangle;

import java.util.ArrayList;
import java.util.List;

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
                line.setLength(0);
                lines.add(line.toString());
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
        Rectangle result = new Rectangle(0,0, charWidth * getMaxLineWidth(), charHeight * lines.size());
        return result;
    }


    private int getMaxLineWidth() {
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

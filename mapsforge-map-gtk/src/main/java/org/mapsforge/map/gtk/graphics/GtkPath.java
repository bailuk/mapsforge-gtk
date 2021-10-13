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
package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.FillRule;
import org.mapsforge.core.graphics.Path;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.cairo.Context;

public class GtkPath implements Path {
    private int fillRule = ch.bailu.gtk.cairo.FillRule.WINDING;
    private final List<Command> commands = new ArrayList<>();

    private float xStart, yStart, xEnd, yEnd;
    private abstract static class Command {
        final float x, y;

        public Command(float x, float y) {
            this.x = x;
            this.y = y;
        }
        public abstract void exec(Context ctx);
    }
    private static class LineTo extends Command {
        public LineTo(float x, float y) {
            super(x,y);
        }
        @Override
        public void exec(Context c) {
            c.lineTo(x,y);
        }
    }
    private static class MoveTo extends Command {
        public MoveTo(float x, float y) {
            super(x,y);
        }
        @Override
        public void exec(Context c) {
            c.moveTo(x,y);
        }
    }


    public void exec(Context c) {
        for (Command cmd : commands) {
            cmd.exec(c);
        }
    }

    public int getFillRule() {
        return fillRule;
    }

    @Override
    public void clear() {
        commands.clear();
    }

    @Override
    public void close() {
        System.out.println("GtkPath::close()");
    }

    @Override
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    @Override
    public void lineTo(float x, float y) {
        commands.add(new LineTo(x, y));
        xEnd = x;
        yEnd =y;
    }

    @Override
    public void moveTo(float x, float y) {
        this.xStart = x;
        this.yStart = y;
        commands.add(new MoveTo(x,y));
    }

    public float getXStart() {
        return xStart;
    }

    public float getYStart() {
        return yStart;
    }

    public float getXEnd() {
        return xEnd;
    }

    public float getYEnd() {
        return yEnd;
    }

    @Override
    public void setFillRule(FillRule fillRule) {
        if (fillRule == FillRule.EVEN_ODD) {
            this.fillRule = ch.bailu.gtk.cairo.FillRule.EVEN_ODD;

        } else {
            this.fillRule = ch.bailu.gtk.cairo.FillRule.WINDING;
        }
    }
}

package org.mapsforge.map.gtk.graphics;

import org.mapsforge.core.graphics.FillRule;
import org.mapsforge.core.graphics.Path;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.cairo.Context;

public class GtkPath implements Path {
    private int fillRule = ch.bailu.gtk.cairo.FillRule.WINDING;

    private final List<Command> commands = new ArrayList<>(20);

    private abstract static class Command {
        public abstract void exec(Context ctx);
    }

    private static class LineTo extends Command {
        private final float x, y;
        public LineTo(float x, float y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public void exec(Context c) {
            c.lineTo(x,y);
        }
    }

    private static class MoveTo extends Command {
        private final float x, y;
        public MoveTo(float x, float y) {
            this.x = x;
            this.y = y;
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


    @Override
    public void clear() {
        commands.clear();
        //System.out.println("GtkPath::clear()");
    }

    @Override
    public void close() {
        System.out.println("GtkPath::close()");
    }

    @Override
    public boolean isEmpty() {
        return commands.isEmpty();
        //System.out.println("GtkPath::isEmpty()");
        //return false;
    }

    @Override
    public void lineTo(float x, float y) {
        commands.add(new LineTo(x, y));
        //System.out.println("GtkPath::lineTo()");
    }

    @Override
    public void moveTo(float x, float y) {
        commands.add(new MoveTo(x,y));
        //System.out.println("GtkPath::moveTo()");
    }

    @Override
    public void setFillRule(FillRule fillRule) {
        if (fillRule == FillRule.EVEN_ODD) {
            this.fillRule = ch.bailu.gtk.cairo.FillRule.EVEN_ODD;

        } else if (fillRule == FillRule.NON_ZERO) {
            this.fillRule = ch.bailu.gtk.cairo.FillRule.WINDING;
        }
        //System.out.println("GtkPath::setFillRule()");
    }
}

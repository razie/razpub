package com.razie.pub.draw.widgets;

import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Drawable.DrawWidget;
import com.razie.pub.draw.Renderer.Technology;

/**
 * just a wrapper to turn any object into a drawable string, via toString(). One use is to stream
 * exceptions for instance.
 * 
 * this is useful also to avoid default formatting of strings - this will not replace a \n with <br>
 * for instance
 * 
 * @author razvanc99
 * 
 */
public class ToString extends DrawWidget {
    Object o;

    public ToString(Object o) {
        this.o = o;
    }

    public Renderer getRenderer(Technology technology) {
        return MyRenderer.singleton;
    }

    /** my renderer, MT-safe */
    private static class MyRenderer implements Renderer<ToString> {
        // no state, MT-safe
        static ToString.MyRenderer singleton = new MyRenderer();

        public boolean canRender(ToString o, Technology technology) {
            return true;
        }

        public Object render(ToString o, Technology technology, DrawStream stream) {
            return o.o == null ? "" : o.o.toString();
        }
    }

    public String toString() {
        return o == null ? "" : o.toString();
    }
}
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
 * for instance. IF you actually want that, just use HtmlRenderUtils.textToHtml(s)
 * 
 * @author razvanc99
 * 
 */
public class DrawToString extends DrawWidget {
    Object o;

    public DrawToString(Object o) {
        this.o = o;
    }

    public Renderer<DrawToString> getRenderer(Technology technology) {
        return MyRenderer.singleton;
    }

    /** my renderer, MT-safe */
    private static class MyRenderer implements Renderer<DrawToString> {
        // no state, MT-safe
        static DrawToString.MyRenderer singleton = new MyRenderer();

        public Object render(DrawToString o, Technology technology, DrawStream stream) {
            return o.o == null ? "" : o.o.toString();
        }
    }

    public String toString() {
        return o == null ? "" : o.toString();
    }
}
package com.razie.pub.draw.widgets;

import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.base.log.Exceptions;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Drawable.DrawWidget;
import com.razie.pub.draw.Renderer.Technology;

/**
 * dedicated error object (popups, info bars etc)
 * 
 * @author razvanc99
 * 
 */
public class DrawError extends DrawWidget {
    String    msg;
    Throwable t;

    public DrawError(String msg, Throwable t) {
        this.msg = msg;
        this.t = t;
    }

    public DrawError(String msg) {
        this.msg = msg;
    }

    public DrawError(Throwable t) {
        this.t = t;
    }

    public Renderer getRenderer(Technology technology) {
        return MyRenderer.singleton;
    }

    /** my renderer, MT-safe */
    private static class MyRenderer implements Renderer<DrawError> {
        static DrawError.MyRenderer singleton = new MyRenderer();

        public boolean canRender(DrawError o, Technology technology) {
            return true;
        }

        public Object render(DrawError o, Technology technology, DrawStream stream) {
            return o.toString();
        }
    }

    public String toString() {
        String reply = HtmlRenderUtils.textToHtml(Exceptions.getStackTraceAsString(t));
        return (msg == null ? "<no msg>" : msg) + (t == null ? "" : "\n" + reply);
    }
}
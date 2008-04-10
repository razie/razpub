/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.base.data.HtmlRenderUtils;
import com.razie.pub.hframe.base.log.Exceptions;
import com.razie.pub.hframe.draw.Renderer.Technology;

/**
 * basic drawable objects. are rendered by Renderers for different technologies
 * 
 * @author razvanc99
 * 
 */
public interface Drawable {
    public Renderer getRenderer(Renderer.Technology technology);

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology t, DrawStream stream);

    public static abstract class Impl implements Drawable {
        public Drawable makeDrawable() {
            return this;
        }

        /** shortcut to render self - don't like controllers that much */
        public Object render(Technology t, DrawStream stream) {
            return Renderer.Helper.draw(this, t, stream);
        }
    }

    /**
     * just a wrapper to turn any object into a drawable string, via toString(). One use is to
     * stream exceptions for instance
     * 
     * @author razvanc99
     * 
     */
    public static class ToString extends Impl implements Drawable {
        Object o;

        public ToString(Object o) {
            this.o = o;
        }

        public Renderer getRenderer(Technology technology) {
            return MyRenderer.singleton;
        }

        /** my renderer, MT-safe */
        private static class MyRenderer implements Renderer {
            // no state, MT-safe
            static MyRenderer singleton = new MyRenderer();

            public boolean canRender(Object o, Technology technology) {
                return o instanceof ToString;
            }

            public Object render(Object o, Technology technology, DrawStream stream) {
                ToString list = (ToString) o;

                return list.o == null ? "" : list.o.toString();
            }
        }

        public String toString() {
            return o == null ? "" : o.toString();
        }
    }

    /**
     * dedicated error object (popups, info bars etc)
     * 
     * @author razvanc99
     * 
     */
    public static class DrawError extends Impl implements Drawable {
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
        private static class MyRenderer implements Renderer {
            static MyRenderer singleton = new MyRenderer();

            public boolean canRender(Object o, Technology technology) {
                return o instanceof DrawError;
            }

            public Object render(Object o, Technology technology, DrawStream stream) {
                DrawError list = (DrawError) o;

                return list.toString();
            }
        }

        public String toString() {
            String reply = HtmlRenderUtils.textToHtml(Exceptions.getStackTraceAsString(t));
            return (msg == null ? "<no msg>" : msg) + (t == null ? "" : "\n" + reply);
        }
    }
}

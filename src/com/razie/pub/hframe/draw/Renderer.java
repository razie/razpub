/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.base.data.HtmlRenderUtils;
import com.razie.pub.hframe.base.data.IndexedMemDb;

/**
 * a renderer can render an object on a display...
 * 
 * @author razvanc99
 * 
 */
public interface Renderer<T> {

    /**
     * @label draws
     */

    /* #com.razie.sdk.draw.Drawable Dependency_Link */

    /**
     * the return object is technology specific
     * 
     * @param o
     * @param technology
     * @return
     */
    public Object render(T o, Technology technology, DrawStream stream);

    /**
     * can i render the given object on the given technology?
     * 
     * @param o
     * @param technology
     * @return true if this renderer can render on the given technology
     */
    public boolean canRender(T o, Technology technology);

    /**
     * about technologies...you see a pile of unrelated stuff...
     * 
     * <p>
     * TEXT - simple toString, whatever that does...
     * 
     * <p>
     * HTML - format the objects for writing to html
     * 
     * <p>
     * XML - whatever native format you have for XML...could as well as be HTML if you don't have
     * anything else
     * 
     * <p>
     * JSON - guess what that is
     * 
     * <p>
     * UPNP - works only for UPNP knowledgeable containers and items, otherwise it's XML...a UPNP
     * method knows what it returns...they would also know the difference between META and
     * DATA...For instance, an AssetBrief knows to turn into an UPNP item and an AssetFolder knows
     * to turn into an UPNP container...that's about it :)
     * 
     * <p>
     * ECLIPSE as opposed to SWING and SVG
     * 
     * @author razvanc99
     * 
     */
    public static enum Technology {
        TEXT, HTML, SWING, SVG, ECLIPSE, XML, JSON, UPNP, ANY
    }

    /** simple renderer utils */
    public static class Helper {
        private static IndexedMemDb<Class, Technology, Renderer> renderers = new IndexedMemDb<Class, Technology, Renderer>();

        /** overwrite or define a new renderer for a drawable class and technology combination */
        public void register(Class c, Technology t, Renderer r) {
            renderers.put(c, t, r);
        }

        private static Renderer findRenderer(Drawable d, Technology technology) {
            Renderer r = renderers.get(d.getClass(), technology);
            return r != null ? r : d.getRenderer(technology);
        }

        /** draw an object */
        public static Object draw(Object o, Technology t, DrawStream stream) {
            if (o == null) {
                return "";
            }

            if (o instanceof Drawable) {
                return findRenderer((Drawable) o, t).render(o, t, stream);
            } else if (o instanceof DrawableSource) {
                Drawable d = ((DrawableSource) o).makeDrawable();
                return findRenderer(d, t).render(d, t, stream);
            } else {
                if (Technology.HTML.equals(t)) {
                    return HtmlRenderUtils.textToHtml(o.toString());
                }
            }

            return o.toString();
        }
    }

    public static interface ContainerRenderer extends Renderer {
        /**
         * the return object is technology specific
         * 
         * @param o the container providing the footer
         * @param technology
         * @return
         */
        public Object renderHeader(Object o, Technology technology, DrawStream stream);

        /**
         * the return object is technology specific
         * 
         * @param o the container
         * @param technology
         * @return
         */
        public Object renderElement(Object container, Object element, Technology technology, DrawStream stream);

        /**
         * the return object is technology specific
         * 
         * @param o the container providing the footer
         * @param technology
         * @return
         */
        public Object renderFooter(Object o, Technology technology, DrawStream stream);
    }
}

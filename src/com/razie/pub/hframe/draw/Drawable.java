/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.draw.Renderer.Technology;

/**
 * basic drawable objects. are rendered by Renderers for different technologies
 * 
 * Convention: each drawable MUST support at least HTML and TEXT. It SHOULD also support XML and
 * JSON. For other rendering technologies, you should create basic drawable objects in this package,
 * which will render themselves onto those respective technologies, depending on their availability
 * in a given environment.
 * 
 * The rendering as TEXT is for logging.
 * 
 * The rendering as HTML is for display in an old HTML page (form or something)
 * 
 * The rendering as XML/JSON is for marshalling/unmarshalling.
 * 
 * The other rendering formats are for display in other technologies (SWING, SVG etc).
 * 
 * There is a NATIVE rendering which means you use only native drawing elements (all defined in this
 * package). These can then render to any other technologies.
 * 
 * The preferred approach for supporting another technology is: make sure the technology is
 * XML-based. Render yourself in NATIVE and implement the xslt transformation from native to the
 * other technology.
 * 
 * Note that rendering of an item includes actionables for that item (user interaction instructions
 * and actions/commands back to the model element). Controllers are optional for managing
 * interaction (there is a defualt controller).
 * 
 * TODO Also NOTE that most of the above is TODO (sic!)
 * 
 * @author razvanc99
 * 
 */
public interface Drawable {
    public Renderer getRenderer(Renderer.Technology technology);

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology t, DrawStream stream);

    public static abstract class DrawWidget implements Drawable {
        public Drawable makeDrawable() {
            return this;
        }

        /** shortcut to render self - don't like controllers that much */
        public Object render(Technology t, DrawStream stream) {
            return Renderer.Helper.draw(this, t, stream);
        }
    }
    
    /** Use this for model drawables that just use widgets, see SampleDrawable */
    public static class DefaultRenderer implements Renderer<Drawable> {
        // no state, MT-safe
        public static DefaultRenderer singleton = new DefaultRenderer();

        public boolean canRender(Drawable o, Technology technology) {
            return true;
        }

        public Object render(Drawable o, Technology technology, DrawStream stream) {
            return o.render(technology, stream);
        }
    }
}

/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw;

import com.razie.pub.draw.Renderer.ContainerRenderer;

/**
 * a drawable sequence of objects. Objects must implement Drawable or else we'll use toString()
 * 
 * this is different from DrawList in that there's no special formatting across elements...and it's
 * a stream not a container
 * 
 * @author razvanc99
 */
public class DrawSequence extends DrawStream implements Drawable {

    public DrawSequence() {
        super(Technology.ANY);
    }

    public DrawSequence(Object... objects) {
        super(Technology.ANY);
        if (objects != null) {
            for (Object o : objects) {
                if (o != null) this.write(o);
            }
        }
    }

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology t, DrawStream stream) {
        return Renderer.Helper.draw(this, t, stream);
    }

    /** I'm also a Drawable */
    public Renderer<DrawSequence> getRenderer(Technology technology) {
        return new MyRenderer();
    }

    /** I'm also a Drawable */
    public static class MyRenderer implements Renderer<DrawSequence> {

        public Object render(DrawSequence o, Technology technology, DrawStream stream) {
            DrawSequence list = (DrawSequence) o;
            String reply = "";
            for (Element element : list.elements) {
                reply += (String) Renderer.Helper.draw(element.o, technology, stream);
            }

            return reply;
        }
    }

    @Override
    public void close() {
    }

    @Override
    protected void renderObjectToStream(Object d) {
    }

    public Drawable makeDrawable() {
        return this;
    }

    @Override
    public void renderElement(StreamableContainer container, Object element) {
        ((ContainerRenderer) ((Drawable) container).getRenderer(technology)).renderElement(container,
                element, technology, this);
    }

    @Override
    protected void renderFooter(StreamableContainer container) {
        ((ContainerRenderer) ((Drawable) container).getRenderer(technology)).renderFooter(container,
                technology, this);
    }

    @Override
    protected void renderHeader(StreamableContainer container) {
        ((ContainerRenderer) ((Drawable) container).getRenderer(technology)).renderHeader(container,
                technology, this);
    }

}

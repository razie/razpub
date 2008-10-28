/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw.widgets;

import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Drawable;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Renderer.Technology;

/**
 * this will insert a paint area which is drawn later with the specified url (will be invoked during
 * paint in the client)
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class DrawLater extends Drawable.DrawWidget {

    private ActionToInvoke ai;

    public DrawLater(ActionToInvoke ai) {
        this.ai = ai;
    }

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology technology, DrawStream stream) {
        return "<iframe frameborder=0 src=\"" + ai.makeActionUrl() + "\"/>";
    }

    public Renderer<Drawable> getRenderer(Technology technology) {
        return DefaultRenderer.singleton;
    }

}

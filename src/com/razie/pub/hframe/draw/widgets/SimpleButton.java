/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw.widgets;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.Renderer;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.resources.RazIconRes;

/**
 * a button with an action and image that just does it's action and no change on the screen (POST in
 * html)
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class SimpleButton extends NavButton {

    public SimpleButton(ActionItem a, String link) {
        super(a, link);
    }
    
    /** command is for both display and execution */
    public SimpleButton(ActionToInvoke a) {
        super(a);
    }

    public Renderer getRenderer(Technology technology) {
        // TODO too stupid - register renderers...
        return new MyRender();
    }

    public class MyRender extends NavButton.MyRender {
        @Override
        public Object render(Object o, Technology technology, DrawStream stream) {
            return irender("<a type=\"POST\" ", o, technology, stream);
        }
    }
}

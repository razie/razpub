/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw.widgets;

import com.razie.pub.base.ActionItem;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Renderer.Technology;

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

    public Renderer<NavLink> getRenderer(Technology technology) {
        // TODO too stupid - register renderers...
        return MyRender.singleton;
    }

    public static class MyRender extends NavButton.MyRender {
        static MyRender singleton=new MyRender();
        
        @Override
        public Object render(NavLink o, Technology technology, DrawStream stream) {
            return irender("<a type=\"POST\" ", o, technology, stream);
        }
    }
}

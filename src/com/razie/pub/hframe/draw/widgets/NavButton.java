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

/**
 * a button with an action and image that navigates elsewhere (i.e. open a new window or do GET in
 * http
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class NavButton extends NavLink {

    /** display a, execute link */
    public NavButton(ActionItem a, String link) {
        super(a, link);
        this.style = Style.TEXT_UNDER;
        this.size = Size.NORMAL;
    }

    /** command is for both display and execution */
    public NavButton(ActionToInvoke a) {
        super(a);
        this.style = Style.TEXT_UNDER;
        this.size = Size.NORMAL;
    }

    /** display item, execute a */
    public NavButton(ActionItem item, ActionToInvoke a) {
        super(item, a);
        this.style = Style.TEXT_UNDER;
        this.size = Size.NORMAL;
    }

    public Renderer getRenderer(Technology technology) {
        // TODO too stupid - register renderers...
        return new MyRender();
    }

    public class MyRender extends NavLink.MyRender {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof NavButton;
        }

        public Object render(Object o, Technology technology, DrawStream stream) {
            return irender("<a", o, technology, stream);
        }
    }
}

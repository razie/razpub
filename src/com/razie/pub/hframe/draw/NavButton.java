/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.resources.RazIconRes;

/**
 * a button with an action and image that navigates elsewhere (i.e. open a new window or do GET in
 * http
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class NavButton extends NavLink implements Drawable {

    private boolean tiny = false;

    /** display a, execute link */
    public NavButton(ActionItem a, String link) {
        super(a,link);
    }

    /** command is for both display and execution */
    public NavButton(ActionToInvoke a) {
        super(a);
    }

    /** display item, execute a */
    public NavButton(ActionItem item, ActionToInvoke a) {
        super(item,a);
    }

    public Renderer getRenderer(Technology technology) {
        // TODO too stupid - register renderers...
        return new MyRender();
    }

    public class MyRender implements Renderer {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof NavButton;
        }

        public Object render(Object o, Technology technology, DrawStream stream) {
            NavButton b = (NavButton) o;
            String icon = RazIconRes.getIconFile(b.action.iconProp);

            // if not std property, then it's full url
            if (icon == null || icon.length() <= 0) {
                icon = b.action.iconProp;
            }

            if (Technology.HTML.equals(technology)) {
                String s = b.link != null && b.link.length() > 0 ? "<a href=\"" + b.link + "\">" : "";
                if (tiny) {
                    s += "<img border=0 width=30 height=30 src=\"" + icon + "\" alt=\"" + b.action.label
                            + "\"/>";
                } else {
                    s += "<img border=0 width=80 height=80 src=\"" + icon + "\" alt=\"" + b.action.label
                            + "\"/>" + "<br>" + b.action.label;
                }
                s += b.link != null && b.link.length() > 0 ? "</a>" : "";
                return s;
            }

            // default rendering
            return b.action.label;
        }
    }

    public void setTiny(boolean tiny) {
        this.tiny = tiny;
    }
}

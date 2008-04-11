/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.resources.RazIconRes;

/**
 * a link with an action that navigates elsewhere (i.e. open a new window or do GET in http
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class NavLink extends Drawable.Impl implements Drawable {

    ActionItem      action;
    String          link;

    /** display a, execute link */
    public NavLink(ActionItem a, String link) {
        this.action = a;
        this.link = link;
    }

    /** command is for both display and execution */
    public NavLink(ActionToInvoke a) {
        this.action = a.actionItem;
        this.link = a.makeActionUrl();
    }

    /** display item, execute a */
    public NavLink(ActionItem item, ActionToInvoke a) {
        this.action = item;
        this.link = a.makeActionUrl();
    }

    public Renderer getRenderer(Technology technology) {
        // TODO too stupid - register renderers...
        return new MyRender();
    }

    public class MyRender implements Renderer {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof NavLink;
        }

        public Object render(Object o, Technology technology, DrawStream stream) {
            NavLink b = (NavLink) o;

            if (Technology.HTML.equals(technology)) {
                String s = b.link != null && b.link.length() > 0 ? "<a href=\"" + b.link + "\">" : "";
                s += b.action.label;
                s += b.link != null && b.link.length() > 0 ? "</a>" : "";
                return s;
            }

            // default rendering
            return b.action.label;
        }
    }
}

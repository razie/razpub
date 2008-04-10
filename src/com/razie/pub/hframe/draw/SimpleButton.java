/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw;

import com.razie.pub.hframe.base.ActionItem;
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

    public Renderer getRenderer(Technology technology) {
        // TODO too stupid - register renderers...
        return new MyRender();
    }

    public class MyRender extends NavButton.MyRender {

        public Object render(Object o, Technology technology) {
            SimpleButton b = (SimpleButton) o;
            if (Technology.HTML.equals(technology)) {
                String s = b.link != null && b.link.length() > 0 ? "<a type=\"POST\" href=\"" + b.link + "\">" : "";
                s += "<img width=60 height=60 src=\"" + RazIconRes.getIconFile(b.action.iconProp) + "\"/><br>"
                        + b.action.name;
                s += b.link != null && b.link.length() > 0 ? "</a>" : "";
                return s;
            }

            // default rendering
            return b.action.name;
        }
    }
}

/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw.widgets;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.Renderer;
import com.razie.pub.hframe.draw.Drawable.DrawWidget;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.resources.RazIconRes;

/**
 * a link with an action that navigates elsewhere (i.e. open a new window or do GET in http
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class NavLink extends DrawWidget {

    ActionItem action;
    String     link;

    public enum Style {
        ONELINE, JUST_LABEL, JUST_ICON, TEXT_UNDER
    };

    public enum Size {
        TINY, SMALL, NORMAL, LARGE
    };

    Style style = Style.ONELINE;
    Size  size  = Size.TINY;

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

    public NavLink style(Style st, Size... sz) {
        this.style = st;
        if (sz.length > 0)
            this.size = sz[0];
        return this;
    }

    public void setTiny(boolean tiny) {
        this.style = tiny ? Style.JUST_ICON : Style.TEXT_UNDER;
        this.size = tiny ? Size.SMALL : Size.NORMAL;
    }

    public class MyRender implements Renderer {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof NavLink;
        }

        protected Object irender(String atype, Object o, Technology technology, DrawStream stream) {
            NavLink b = (NavLink) o;
            String icon = RazIconRes.getIconFile(b.action.iconProp);

            // if not std property, then it's full url
            if (icon == null || icon.length() <= 0) {
                icon = b.action.iconProp;
            }

            if (Technology.HTML.equals(technology)) {
                String s = b.link != null && b.link.length() > 0 ? atype + " href=\"" + b.link + "\">" : "";
                if (!style.equals(Style.JUST_LABEL) && icon != null && !icon.equals(b.action.name)) {
                    if (Size.TINY.equals(size)) {
                        s += "<img border=0 width=21 height=21 src=\"" + icon + "\" alt=\"" + b.action.label
                                + "\"/>";
                    } else if (Size.SMALL.equals(size)) {
                        s += "<img border=0 width=30 height=30 src=\"" + icon + "\" alt=\"" + b.action.label
                                + "\"/>";
                    } else if (Size.NORMAL.equals(size)) {
                        s += "<img border=0 width=80 height=80 src=\"" + icon + "\" alt=\"" + b.action.label
                                + "\"/>";
                    } else if (Size.LARGE.equals(size)) {
                        s += "<img border=0 width=180 height=180 src=\"" + icon + "\" alt=\""
                                + b.action.label + "\"/>";
                    }

                    if (Style.ONELINE.equals(style)) {
                        s += b.action.label;
                    } else if (Style.JUST_ICON.equals(style)) {
                    } else if (Style.TEXT_UNDER.equals(style)) {
                        s += "<br>" + b.action.label;
                    }
                } else {// Style.JUST_LABEL
                    s += b.action.label;
                }
                s += b.link != null && b.link.length() > 0 ? "</a>" : "";
                return s;
            }

            // default rendering
            return b.action.label;
        }

        public Object render(Object o, Technology technology, DrawStream stream) {
            return irender("<a", o, technology, stream);
        }
    }
}

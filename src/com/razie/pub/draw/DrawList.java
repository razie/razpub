/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.draw;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.razie.pub.draw.DrawStream.Element.State;
import com.razie.pub.draw.Renderer.ContainerRenderer;
import com.razie.pub.draw.Renderer.Technology;

/**
 * a drawable list of objects. objects must implement Drawable or else we'll use toString()
 * 
 * @author razvanc99
 * 
 */
public class DrawList extends StreamableContainer.Impl implements Drawable, StreamableContainer {

    private List   list       = new ArrayList();
    public boolean isVertical = false;
    public String  valign     = null;
    public String  rowColor   = null;

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology t, DrawStream stream) {
        return Renderer.Helper.draw(this, t, stream);
    }

    public Renderer getRenderer(Technology technology) {
        return new MyRenderer();
    }

    /**
     * @param list the list to set
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * @return the list
     */
    public List getList() {
        return list;
    }

    public void write(Object o) {
        this.getList().add(o);
        if (this.state.equals(State.OPEN)) {
            this.ownerStream.renderElement(this, o);
        }
    }

    public static class MyRenderer implements ContainerRenderer {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof DrawList;
        }

        public Object render(Object o, Technology technology, DrawStream stream) {
            DrawList list = (DrawList) o;
            String res = "LIST???";

            if (Technology.HTML.equals(technology)) {
                res = (String) renderHeader(o, technology, stream);
                for (Object e : list.list) {
                    res += (String) renderElement(o, e, technology, stream);
                }
                res += (String) renderFooter(o, technology, stream);
            } else if (Technology.JSON.equals(technology)) {
                JSONObject jso = new JSONObject(list);
                res = jso.toString();
            }
            list.wroteHeader = list.wroteFooter = list.wroteElements = true;
            return res;
        }

        private String makeTR(DrawList list) {
            return (list.rowColor == null ? "<tr " : "<tr bgcolor=\"" + list.rowColor + "\" ")
                    + (list.valign == null ? "" : "valign=\"" + list.valign + "\"") + ">";
        }

        public Object renderElement(Object container, Object element, Technology technology, DrawStream stream) {
            DrawList list = (DrawList) container;
            String s = "";
            if (Technology.HTML.equals(technology)) {
                s += list.isVertical ? makeTR(list) + "<td>" : "<td>";
                s += Renderer.Helper.draw(element, technology, stream);
                s += list.isVertical ? "</td></tr>" : "</td>";
            } else if (Technology.JSON.equals(technology)) {
                JSONObject o = new JSONObject(element);
                s = o.toString();
            }
            return s;
        }

        public Object renderFooter(Object o, Technology technology, DrawStream stream) {
            DrawList list = (DrawList) o;
            String res = "LIST???";

            if (Technology.HTML.equals(technology)) {
                res = list.isVertical ? "" : "</tr>";
                res += "</table>";
            } else if (Technology.JSON.equals(technology)) {
                res = "]";
            }
            list.wroteFooter = true;
            return res;
        }

        public Object renderHeader(Object o, Technology technology, DrawStream stream) {
            DrawList list = (DrawList) o;
            String res = "LIST???";

            if (Technology.HTML.equals(technology)) {
                res = "<table>" + (list.isVertical ? "" : makeTR(list));
            } else if (Technology.JSON.equals(technology)) {
                res = "[";
            }

            list.wroteHeader = true;
            return res;
        }
    }
}

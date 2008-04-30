/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw.widgets;

import java.util.HashMap;
import java.util.Map;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.base.AttrAccess;
import com.razie.pub.hframe.base.AttrAccess.AttrType;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.Drawable;
import com.razie.pub.hframe.draw.Renderer;
import com.razie.pub.hframe.draw.Renderer.Technology;

/**
 * this will insert a paint area which is drawn later with the specified url (will be invoked during
 * paint in the client)
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class DrawForm extends Drawable.DrawWidget {

    private ActionToInvoke ai;
    AttrAccess             parms;
    ActionItem             name;

    public DrawForm(ActionItem name, ActionToInvoke ai, AttrAccess parms) {
        this.ai = ai;
        this.name = name;
        this.parms = parms;
    }

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology technology, DrawStream stream) {
        String s = "\n<form name=\"" + name.label + "\" action=\"" + ai.makeActionUrl()
                + "\" method=\"get\">";

        s += "FORM: " + name.getLabel() + "<br>";
        s += "<table>";
        for (String a : parms.getPopulatedAttr()) {
            s += "\n<tr><td>" + a + ":";
            String type = types.get(AttrType.STRING);
            if (parms.getAttrType(a) != null) {
                AttrType t = parms.getAttrType(a);
                if (t != null) {
                    String ttype = types.get(t);
                    type = ttype != null ? ttype : type;
                }
            }
            Object val = parms.getAttr(a);
            val = val == null ? "" : val.toString();

            s += "</td><td>";
            s += type.replaceAll("RAZ.NAME", a).replaceAll("RAZ.VALUE", (String)val);
            s += "</td></tr>";
        }

        s += "</table>";
        s += "\n<input type=\"submit\" value=\"Submit\"/> ";
        s += "\n</form>\n";

        return s;
    }

    public Renderer getRenderer(Technology technology) {
        return DefaultRenderer.singleton;
    }

    private static Map<AttrAccess.AttrType, String> types = new HashMap<AttrAccess.AttrType, String>();
    static {
        types.put(AttrType.STRING, "<input type=\"text\" name=\"RAZ.NAME\" value=\"RAZ.VALUE\" />");
        types.put(AttrType.MEMO, "<TEXTAREA rows=20 cols=80 name=\"RAZ.NAME\">RAZ.VALUE</textarea>");
        types.put(AttrType.SCRIPT, "<TEXTAREA rows=20 cols=80 name=\"RAZ.NAME\">RAZ.VALUE</textarea>");
        types.put(AttrType.INT, "<input type=\"text\" name=\"RAZ.NAME\" value=\"RAZ.VALUE\" />");
        types.put(AttrType.FLOAT, "<input type=\"text\" name=\"RAZ.NAME\" value=\"RAZ.VALUE\" />");
        types.put(AttrType.DATE, "<input type=\"text\" name=\"RAZ.NAME\" value=\"RAZ.VALUE\" />");
    }
}

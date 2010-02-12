package com.razie.pub.webui;

import razie.base.ActionItem;
import razie.draw.DrawStream;
import razie.draw.Renderer;
import razie.draw.Technology;
import razie.draw.widgets.NavButton;
import razie.draw.widgets.NavLink;

import com.razie.pub.comms.SimpleActionToInvoke;

public class BrowseToPage extends SimpleActionToInvoke {

    String pageID;

    public BrowseToPage(ActionItem item, String pageID) {
        super(item);
        this.pageID = pageID;
    }

    public Renderer<BrowseToPage> getRenderer(Technology technology) {
        return new MyRenderer();
    }

    public static class MyRenderer implements Renderer<BrowseToPage> {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof BrowseToPage;
        }

        public Object render(BrowseToPage ea, Technology technology, DrawStream stream) {
            ActionItem cmd = ea.actionItem;

            String url;
            if (ea.pageID.startsWith("http:"))
                url = ea.pageID;
            else
                url = "/mutant/" + ea.pageID + ".html";

            NavLink b1 = new NavButton(cmd, url);
            b1.setTiny(ea.drawTiny);
            NavLink b = b1;
            return Renderer.Helper.draw(b, technology, stream);
        }
    }
}

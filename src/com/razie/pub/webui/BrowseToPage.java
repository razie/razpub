package com.razie.pub.webui;

import com.razie.pub.base.ActionItem;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Renderer.Technology;
import com.razie.pub.draw.widgets.NavButton;
import com.razie.pub.draw.widgets.NavLink;

public class BrowseToPage extends ActionToInvoke {

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

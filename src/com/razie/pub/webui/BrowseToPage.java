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

    @Override
        public Object render(Technology technology, DrawStream stream) {
            ActionItem cmd = actionItem;

            String url;
            if (pageID.startsWith("http:"))
                url = pageID;
            else
                url = "/mutant/" + pageID + ".html";

            NavLink b1 = new NavButton(cmd, url);
            b1.setTiny(drawTiny);
            NavLink b = b1;
            return Renderer.Helper.draw(b, technology, stream);
        }
}

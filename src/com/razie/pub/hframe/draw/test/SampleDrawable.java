/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.draw.test;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.draw.DrawSequence;
import com.razie.pub.hframe.draw.DrawStream;
import com.razie.pub.hframe.draw.Drawable;
import com.razie.pub.hframe.draw.Renderer;
import com.razie.pub.hframe.draw.Renderer.Technology;
import com.razie.pub.hframe.draw.widgets.DrawError;
import com.razie.pub.hframe.draw.widgets.DrawLater;
import com.razie.pub.hframe.draw.widgets.DrawSelection;
import com.razie.pub.hframe.draw.widgets.NavButton;
import com.razie.pub.hframe.draw.widgets.NavLink;
import com.razie.pub.hframe.draw.widgets.SimpleButton;
import com.razie.pub.hframe.draw.widgets.ToString;
import com.razie.pub.hframe.draw.widgets.NavLink.Size;
import com.razie.pub.hframe.draw.widgets.NavLink.Style;
import com.razie.pub.hframe.resources.RazIcons;

/**
 * this is a sample model drawable - it has one label and two buttons
 * 
 * a model drawable, ideally will ignore the technology and the stream, just use widgets and ask
 * them to render
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class SampleDrawable extends Drawable.DrawWidget {

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology technology, DrawStream stream) {
        DrawSequence seq = new com.razie.pub.hframe.draw.DrawSequence();

        ActionItem AI = new ActionItem("lightsoa/service/echo", RazIcons.FILE);
        AI.label = "echo something...";
        ActionToInvoke ATI = new ActionToInvoke(AI, "msg", "echo...");

        seq.write("\nno action label vvv\n");
        seq.write(new NavLink(AI, (String) null).style(NavLink.Style.JUST_LABEL));
        seq.write("\nno action label ^^^\n");

        seq.write("\n action label vvv\n");
        seq.write(new NavLink(ATI).style(Style.JUST_LABEL));
        seq.write("\n action label ^^^\n");

        seq.write("\n just icon vvv\n");
        seq.write(new NavLink(ATI).style(NavLink.Style.JUST_ICON, Size.TINY));
        seq.write(new NavLink(ATI).style(NavLink.Style.JUST_ICON, Size.SMALL));
        seq.write(new NavLink(ATI).style(NavLink.Style.JUST_ICON, Size.NORMAL));
        seq.write(new NavLink(ATI).style(NavLink.Style.JUST_ICON, Size.LARGE));
        seq.write("\n just icon ^^^\n");

        seq.write("\n nice link vvv\n");
        seq.write(new NavLink(ATI));
        seq.write("\n nice link ^^^\n");

        seq.write("\nno action button vvv\n");
        seq.write(new SimpleButton(AI, (String) null));
        seq.write("\nno action button ^^^\n");

        seq.write("\n action button vvv\n");
        seq.write(new SimpleButton(ATI));
        seq.write("\n action button ^^^\n");

        seq.write("\n nav button vvv\n");
        seq.write(new NavButton(ATI));
        seq.write("\n nav button ^^^\n");

        seq.write("\n tiny nav button vvv\n");
        NavButton b = new NavButton(ATI);
        b.setTiny(true);
        seq.write(b);
        seq.write("\n tiny nav button ^^^\n");

        seq.write("\n selection vvv\n");
        DrawSelection sel = new DrawSelection("samplesel", ATI, ATI, ATI);
        seq.write(sel);
        seq.write("\n selection ^^^\n");

        seq.write("\nToString vvv\n");
        seq.write(new ToString("some text"));
        seq.write("\nToString ^^^\n");

        seq.write("\nDrawError vvv\n");
        try {
            justThrow();
        } catch (Throwable t) {
            seq.write(new DrawError(t));

        }
        seq.write("\nDrawError ^^^\n");

        seq.write("\nDrawLater vvv\n");
        seq.write(new DrawLater(ATI));
        seq.write("\nDrawLater ^^^\n");

        // for actual rendering, ask the widget
        return seq.render(technology, stream);
    }

    public Renderer getRenderer(Technology technology) {
        return DefaultRenderer.singleton;
    }

    private void justThrow() {
        throw new RuntimeException("exc");
    }

}

package com.razie.pub.draw.widgets;

import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Drawable.DrawWidget;
import com.razie.pub.draw.Renderer.Technology;

/**
 * Draw a text with proper toHTML formatting
 * 
 * @author razvanc99
 * 
 */
public class DrawText extends DrawWidget {
    String o;

    public DrawText(String o) {
        this.o = o;
    }

    public Renderer<DrawText> getRenderer(Technology technology) {
        return MyRenderer.singleton;
    }

    /** my renderer, MT-safe */
    private static class MyRenderer implements Renderer<DrawText> {
        // no state, MT-safe
        static DrawText.MyRenderer singleton = new MyRenderer();

        public Object render(DrawText o, Technology technology, DrawStream stream) {
            if (Technology.HTML.equals(technology)) {
                return HtmlRenderUtils.textToHtml(o.o);
            }
            
            return o;
        }
    }

    public String toString() {
        return o == null ? "" : o.toString();
    }
}
/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw.widgets;

import com.razie.pub.base.ScriptContext;
import com.razie.pub.base.ScriptJS;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Drawable;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.Drawable.DrawWidget;
import com.razie.pub.draw.Renderer.Technology;

/**
 * dynamic painting - will paint the TEXT results of running the script
 * 
 * TODO figure out how can the script change the format
 * 
 * @author razvanc99
 * 
 */
public class DrawScript extends DrawWidget {
    String        script;
    ScriptContext ctx;

    public DrawScript(String script) {
        this.script = script;
        this.ctx = ScriptContext.Impl.global();
    }

    public DrawScript(String script, ScriptContext ctx) {
        this.script = script;
        this.ctx = ctx;
    }

    public Renderer<DrawScript> getRenderer(Technology technology) {
        return MyRenderer.singleton;
    }

    /** my renderer, MT-safe */
    private static class MyRenderer implements Renderer<DrawScript> {
        // no state, MT-safe
        static DrawScript.MyRenderer singleton = new MyRenderer();

        public Object render(DrawScript o, Technology technology, DrawStream stream) {
            ScriptJS js = new ScriptJS(o.script);
            Object res = js.eval(o.ctx);
            if (res instanceof Drawable){
                return Renderer.Helper.draw(res, technology, stream);
            } else {
                return res.toString();
            }
        }
    }

    public String toString() {
        return script == null ? "" : script;
    }
}
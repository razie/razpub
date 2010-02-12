/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw.widgets;

import razie.base.ScriptContext;
import razie.draw.DrawStream;
import razie.draw.Drawable;
import razie.draw.Renderer;
import razie.draw.Technology;
import razie.draw.Drawable.DrawWidget;

import com.razie.pub.base.RazScript;
import com.razie.pub.base.ScriptFactory;

/**
 * dynamic painting - will paint the TEXT results of running the script
 * 
 * TODO figure out how can the script change the format
 * 
 * @author razvanc99
 * 
 */
public class DrawScript extends DrawWidget {
    String        lang;
    String        script;
    ScriptContext ctx;

    public DrawScript(String lang, String script) {
        this.lang = lang;
        this.script = script;
        this.ctx = ScriptContext.Impl.global();
    }

    public DrawScript(String lang, String script, ScriptContext ctx) {
        this.lang = lang;
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
            RazScript js = ScriptFactory.make(o.lang, o.script);
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
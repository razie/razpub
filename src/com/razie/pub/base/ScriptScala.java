/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.io.IOException;
import java.net.URL;

import org.mozilla.javascript.*;

import com.razie.pub.comms.Comms;

/**
 * running interpreted scala
 * 
 * @version $Revision: 1.13 $
 * @author $Author: razvanc $
 * @since $Date: 2007-05-03 13:31:49 $
 */
public class ScriptScala implements RazScript {

    private String script;

    /** use this for remote, files and classpath scripts */
    public ScriptScala(URL sfile) {
        try {
            this.setScript(Comms.readStream(sfile.openStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** build from script string */
    public ScriptScala(String s) {
        this.setScript(s);
    }

    public void setScript(String s) {
        this.script = s;
    }

    public String getScript() {
        return this.script;
    }

    /** @return the statement */
    public String toString() {
        return "js:\n" + this.getScript();
    }

    /**
     * execute the script with the given context
     * 
     * @param c the context for the script
     */
    public Object eval(ScriptContext c) {
        Object result = "";
        Context ctx = Context.enter();
        try {
            // Initialize the standard objects (Object, Function, etc.)
            // This must be done before scripts can be executed. Returns
            // a scope object that we use in later calls.
            Scriptable scope = ctx.initStandardObjects(null);

            for (String key : c.getPopulatedAttr()) {
                Object obj = c.getAttr(key);
                ScriptableObject.putProperty(scope, key, Context.toObject(obj, scope));
            }
            // Now evaluate the script
            result = ctx.evaluateString(scope, script, "<cmd>", 1, null);

            // add JS variables back into ScriptContext
            Object[] ids = scope.getIds();
            for (int i = 0; i < ids.length; i++) {
                String key = String.valueOf(ids[i]);
                if ((c.getAttr(key) != null)) {
                    continue;
                }
                Object obj = scope.get(String.valueOf(ids[i]), scope);
                if (obj != null) {
                    // first unwrap object from JS wrapper
                    if (obj instanceof Wrapper) {
                        obj = ((Wrapper) obj).unwrap();
                    }
                    // skip JS internal objects (like functions)
                    if (obj.getClass().getName().startsWith("org.mozilla")) {
                        continue;
                    }
                    // logger.trace(3, "add var to ctx: " + ids[i] + "(" + obj.getClass().getName()
                    // + ")");
                    c.setAttr(ids[i].toString(), obj);
                }
            }

            // convert to String
            if (result instanceof Wrapper) {
                result = ((Wrapper) result).unwrap();
            } else {
                result = Context.toString(result);
            }
        } catch (Exception e) {
            throw new RuntimeException("While processing script: " + this.script, e);
        } finally {
            // Exit from the context.
            Context.exit();
        }
        return result;
    }

    public static void main(String[] argv) {
        String script = "val y = 3; def f(x)={x+1}; f(7)";
        ScriptScala js = new ScriptScala(script);
        System.out.println(js.eval(new ScriptContext.Impl()));

        script = "TimeOfDay.value()";
        js = new ScriptScala(script);
        ScriptContext ctx = new ScriptContext.Impl();
        ctx.setAttr("TimeOfDay", new TimeOfDay());
        System.out.println(js.eval(ctx));

        // script = "com.razie.playground.things.TimeOfDay.calcvalue()";
        // js = new JavaScript(script);
        // System.out.println(js.run(new ScriptContext.Impl()));
    }
}

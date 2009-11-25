/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;


/**
 * running javascripts
 * 
 * TODO pre-parse the thing
 * 
 * TODO use JSR 264 or whatever the thing is and ditch custom code...
 * 
 * @version $Revision: 1.13 $
 * @author $Author: razvanc $
 * @since $Date: 2007-05-03 13:31:49 $
 */
public class ScriptJS implements RazScript {

    private String script;

    /** build from script string */
    public ScriptJS(String s) {
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
}

/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.util.HashMap;
import java.util.Map;

/**
 * actions execute in a context of objects available at that time in that environment.
 * 
 * a script context is more complex than just AttributeAccess, it may include a hierarchy of
 * contexts, hardcode mappings etc. This class may go away and be replaced with the jdk1.6
 * scriptables.
 * 
 * it's used to run activities and scripts $
 * 
 * @author razvanc99
 * 
 */
public interface ScriptContext extends AttrAccess {

    /** TODO document */
    public void define(String macro, String expr);

    /** TODO document */
    public void undef(String macro);

    /** TODO document */
    public void guard(String name, String condition, String expr);

    /** TODO document */
    public void unguard(String name, String condition, String expr);

    public class Impl extends AttrAccess.TreeImpl implements ScriptContext {
        private static ScriptContext    main   = new ScriptContext.Impl();

        protected Map<String, String>   macros = new HashMap<String, String>();
        protected Map<String, String[]> guards = new HashMap<String, String[]>();

        public static ScriptContext global() {
            return main;
        }

        public Impl() {
            this((ScriptContext) null);
        };

        public Impl(AttrAccess.Impl aa) {
            this(null, aa);
        };

        /** supports a map as well */
        public Impl(Object... pairs) {
            this(null, pairs);
        }

        public Impl(ScriptContext parent) {
            super(parent);
        };

        public Impl(ScriptContext parent, AttrAccess.Impl aa) {
            super(parent);
            this.parms = aa.parms;
        };

        /** supports a map as well */
        public Impl(ScriptContext parent, Object... pairs) {
            super(parent, pairs);
        }

        public Object getAttr(String name) {
            if (macros.containsKey(name)) {
                return ScriptFactory.make(macros.get(name)).eval(this);
            }
            return super.getAttr(name);
        }

        public boolean isPopulated(String name) {
            return macros.containsKey(name) ? true : super.isPopulated(name);
        }

        /**
         * redefine a parameter to mean something else
         * 
         * TODO use trust relationships
         */
        public void define(String macro, String expr) {
            macros.put(macro, expr);
        }

        /**
         * reset all overloads of a parm
         * 
         * TODO use trust relationships
         */
        public void undef(String macro) {
            macros.remove(macro);
        }

        /** TODO use the gurads - currently i'm not using them. I think i want them to be what, rules??? */
        public void guard(String name, String condition, String expr) {
            String[] g = new String[2];
            g[0] = condition;
            g[1] = expr;
            guards.put(name, g);
        }

        public void unguard(String name, String condition, String expr) {
            guards.remove(name);
        }
    }
}

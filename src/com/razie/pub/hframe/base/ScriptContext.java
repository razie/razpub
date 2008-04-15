/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.base;

/**
 * actions execute in a context of objects available at that time in that environment.
 * 
 * a script context is more complex than just AttributeAccess, it may include a hierarchy of
 * contexts, hardcode mappings etc. This class may go away and be replaced with the jdk1.6
 * scriptables.
 * 
 * it's used to run activities and scripts
 *  $
 * @author razvanc99
 * 
 */
public interface ScriptContext extends AttrAccess {

    public class Impl extends AttrAccess.TreeImpl implements ScriptContext {
        public static ScriptContext main = new ScriptContext.Impl();

        public Impl() {
            this((ScriptContext)null);
        };

        public Impl(AttrAccess.Impl aa) {
            this(null, aa);
        };

        /** supports a map as well */
        public Impl(Object... pairs) {
            this(null,pairs);
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
            super(parent,pairs);
        }
    }
}

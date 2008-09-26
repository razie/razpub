/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.util.HashMap;
import java.util.Map;

/**
 * thread local statics, to allow multiple instances of stuff/services/agents to co-exist (for
 * testing for instance) you need easy ways to avoid using statics
 * 
 * The way this works is: the root "process/agent" will register a "context" in each thread it owns
 * or reuses. This class will find that and get the related set of "statics" for that context.
 * 
 * Keeping instances: you either keep the instnace yourself and call enter(instance) or
 * register(name, instance) and then call enter(name).
 * 
 * NOTE: this is not terribly efficient, but simplifies multithreaded programming.
 * 
 * @author razvanc99
 */
public class NoStatics {
    private Map<Class, Object>            statics   = new HashMap<Class, Object>();
    private static Map<String, NoStatics> contexts  = new HashMap<String, NoStatics>();
    private static Map<Thread, NoStatics> instances = new HashMap<Thread, NoStatics>();
    private static NoStatics              root      = new NoStatics();

    public static NoStatics instance() {
        NoStatics s = instances.get(Thread.currentThread());
        return s == null ? root : s;
    }

    /** the current thread uses this instance - normally from a ThreadContext */
    public static void enter(NoStatics instance) {
        instances.put(Thread.currentThread(), instance);
    }

    /** the current thread uses this instance - normally from a ThreadContext */
    public static void enter(String name) {
        instances.put(Thread.currentThread(), contexts.get(name));
    }

    public static void register(String name, NoStatics instance) {
        contexts.put(name, instance);
    }

    /** the current thread done with this instance - normally from a ThreadContext */
    public static void exit() {
        instances.remove(Thread.currentThread());
    }

    public static Object put(Class c, Object o) {
        instance().statics.put(c, o);
        return o;
    }

    /** get the instance/static for this thread of the given class */
    public static Object get(Class c) {
        return instance().statics.get(c);
    }
}

/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * don't you hate not having thread context support? well, this is one way to do it. let's see if we
 * can find the optimal way.
 * 
 * this is a way to setup a context that's specific to each thread. similar to a session in a
 * servlet call or an EJB context during an EJB call, SOMEBODY is reponsible for setting the right
 * context when the thread is started / reused from a pool AND also erasing it at the end, to be
 * nice.
 * 
 * some other classes, most noticeably NoStatics, can use the context later to figure out who
 * they're working for...
 * 
 * NOTE: this is not terribly efficient, but simplifies multithreaded programming.
 * 
 * TODO check that it is mtsafe
 */
public class ThreadContext extends AttrAccess.Impl {
    // TODO actually use weak refrences - this keeps Threads from being collected
    private static Map<Thread, ThreadContext> instances = Collections
                                                                .synchronizedMap(new HashMap<Thread, ThreadContext>());
    static ThreadContext                      MAIN      = new ThreadContext(null);
    NoStatics                                 statics   = null;

    public ThreadContext(NoStatics myStatics) {
        this.statics = myStatics;
    }

    /** return the instance to use for the curent thread */
    public static ThreadContext instance() {
        ThreadContext s = instances.get(Thread.currentThread());
        return s == null ? MAIN : s;
    }

    /** current thread ENTERs this context and return the old one - you can use the old one on exit */
    public ThreadContext enter() {
        ThreadContext old = instances.get(Thread.currentThread());
        instances.put(Thread.currentThread(), this);
        if (statics != null)
            NoStatics.enter(statics);
        return old;
    }

    /** current thread EXITs this context */
    public static void exit(ThreadContext... old) {
        instances.remove(Thread.currentThread());
        if (old.length > 0)
            instances.put(Thread.currentThread(), old[0]);
    }
}

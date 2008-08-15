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
    static ThreadContext                      MAIN      = new ThreadContext();

    public static ThreadContext instance() {
        ThreadContext s = instances.get(Thread.currentThread());
        return s == null ? MAIN : s;
    }

    public static void enter(ThreadContext tc) {
        if (tc != null)
            instances.put(Thread.currentThread(), tc);
    }

    public void enter() {
        instances.put(Thread.currentThread(), this);
    }

    public static void exit() {
        instances.remove(Thread.currentThread());
    }
}

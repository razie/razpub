/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * don't you hate not having thread context support? well, this is one way to do
 * it. let's see if we can find the optimal way. Note that these contexts need
 * to be reset manually between threads.
 * 
 * <p>
 * this is a way to setup a context that's specific to each thread. similar to a
 * session in a servlet call or an EJB context during an EJB call, SOMEBODY is
 * reponsible for setting the right context with enter() when the thread is started / reused
 * from a pool AND also erasing it at the end, with exit(), to be nice.
 * 
 * <p>
 * NOTE: This is a logical context. While you can certainly create one for each
 * thread, you can also share one across multiple threads and keep it between
 * invocations, much like a servlet session.
 * 
 * <p>
 * In the mutantagent, this is used per instance of agent, such that mulitple
 * agents can run inside the same JVM.
 * 
 * <p>
 * some other classes, most noticeably NoStatics, can use the context later to
 * figure out who they're working for...
 * 
 * <p>
 * NOTE: this is not terribly efficient, but simplifies multithreaded
 * programming.
 * 
 * TODO check that it is mtsafe
 * 
 * TODO i should use ThreadLocal to implement this
 * 
 * @author razvanc
 */
public class ThreadContext extends AttrAccess.Impl {
   // TODO actually use weak refrences - this probably keeps Threads from being
   // collected if one forgets to exit()
   // TODO implement nice debugging: remember stack trace when enter() and print
   // when collected without exit()

   // all active instances, indexed by active thread
   private static Map<Thread, ThreadContext> instances = Collections
         .synchronizedMap(new HashMap<Thread, ThreadContext>());

   static ThreadContext DFLT_CTX = new ThreadContext(null);

   List<String> locals = new ArrayList<String>();// TODO lazy
   NoStatics statics = null; // my statics

   /**
    * thread contexts are intricately tied to the no-statics concept
    * 
    * @param myStatics
    */
   public ThreadContext(NoStatics myStatics) {
      this.statics = myStatics;
   }

   /** return the instance to use for the curent thread */
   public static ThreadContext instance() {
      ThreadContext s = instances.get(Thread.currentThread());
      return s == null ? DFLT_CTX : s;
   }

   /*
    * this attr is erased when exit() - it is only accessible on this
    * session/thread/call
    */
   public synchronized void setLocalAttr(String name, Object value) {
      this.setAttr(name, value);
      locals.add(name);
   }

   /**
    * current thread ENTERs this context and return the old one - you can use
    * the old one on exit
    */
   public synchronized ThreadContext enter() {
      // no further locking since the entry for this thread can't be modified
      // inbetween
      ThreadContext old = instances.get(Thread.currentThread());
      instances.put(Thread.currentThread(), this);

      if (statics != null)
         NoStatics.enter(statics);

      return old;
   }

   /** current thread EXITs this context */
   public static void exit(ThreadContext... old) {
      ThreadContext exited = instances.remove(Thread.currentThread());

      if (old.length > 0)
         instances.put(Thread.currentThread(), old[0]);

      if (exited != null)
         synchronized (exited) {
            if (exited.locals != null)
               for (String n : exited.locals)
                  exited.setAttr(n, null);
         }
   }
}

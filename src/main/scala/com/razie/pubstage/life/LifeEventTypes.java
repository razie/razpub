/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage.life;

import com.razie.pub.events.EvTypes;

/**
 * event types for life package
 * 
 * @version $Revision: 1.63 $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class LifeEventTypes extends EvTypes.Impl {
    /**
     * Notification that the thread is starting (called before 'run' function)
     * 
     * @param t Worker that is going to be started
     */
    public static String EV_THREAD_START = "threadStarting(Worker t)";

    /**
     * Notification that the thread has updated the progress value.
     * 
     * @param t Worker that has been updated
     */
    public static String EV_THREAD_UPD   = "threadUpdated(Worker t)";

    /**
     * Notification that the thread has stopped (called after 'run' function)
     * 
     * @param t Worker just has stopped its own task.
     */
    public static String EV_THREAD_END   = "threadFinished(Worker t)";
}

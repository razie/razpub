package com.razie.pubstage.life;

import com.razie.pub.hframe.events.EvTypes;

public class MTEventTypes extends EvTypes {
    /**
     * Notification that the thread is starting (called before 'run' function)
     * 
     * @param t MTWrkRq thread that is going to be started
     */
    public static String EV_THREAD_START = "threadStarting(MTWrkRq t)";

    /**
     * Notification that the thread is updated the progress value.
     * 
     * @param t MTWrkRq thread that is going to be updated
     */
    public static String EV_THREAD_UPD   = "threadUpdated(MTWrkRq t)";

    /**
     * Notification that the thread has stopped (called after 'run' function)
     * 
     * @param t MTWrkRq thread just has stopped its own task.
     */
    public static String EV_THREAD_END   = "threadFinished(MTWrkRq t)";
}

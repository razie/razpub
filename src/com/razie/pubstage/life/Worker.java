//============================================================================
//  FILE INFO
//    $Id: MTWrkRq.java,v 1.20 2007-10-01 17:00:58 razvanc Exp $
//
//    Service Broker 2.0
//    Copyright (c) 1993, 2001 Sigma Systems Group (Canada) Inc.
//    All rights reserved.
//
//  DESCRIPTION
//    Framewrk class for Thread Manager.
//
//  REVISION HISTORY
//  * Sorinel C    2000-12-27
//    Original version
//  * Sorinel C    2003-02-07
//  * Based on CVS log
//============================================================================

package com.razie.pubstage.life;

import java.util.HashMap;
import java.util.Map;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.log.Exceptions;
import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.events.PostOffice;

/**
 * A runnable work request.
 * 
 * <p>
 * Must be used with either MTLaunch or MTPool. If you want as many threads as work requests, use
 * MTLaunch. If you want a queue of work requests processed by a pool of threads, use MTPool.
 * 
 * $
 * @author razvanc99
 * 
 */
public abstract class Worker implements Runnable {


    private ActionItem progressCode;
    public Worker() {
    }

    /**
     * return a 0-100 progress indicator. Be sure to synchronize inside on whatever you deem
     * neccessary
     */
    public int getProgress() {
        return progress;
    }

    /**
     * this would also update the progress message, if any
     * 
     * @param p a progress indicator 0-nothing done, 100-all done
     * @param progressCode a string code for progress. Derived classes must overload the
     *        getProgressMsg to translate the code
     */
    public void setProgress(int p, ActionItem progressCd) {
        this.progress = p;
        this.progressCode = progressCd;
        this.notifyUpdated();
    }

    /**
     * this would also update the progress message, if any
     * 
     * @param p a progress indicator 0-nothing done, 100-all done
     * @param progressCode a string code for progress. Derived classes must overload the
     *        getProgressMsg to translate the code
     */
    public void setProgress(int p, String progressCd) {
        this.progress = p;
//        this.progressCode = progressCd;
        this.notifyUpdated();
    }

    /**
     * Notification that the thread is starting (called before 'process' function)
     * 
     * <p>
     * Don't forget to also check for the shutdownRequested() and if set, throw InterruptedException
     * 
     * @param p a progress indicator 0-nothing done, 100-all done
     * @param progressCode a string code for progress. Derived classes must overload the
     *        getProgressMsg to translate the code
     */
    public static void updateProgress(int newProgress, ActionItem newProgressCode) {
        Worker w = null;
        synchronized (allRq) {
            w = (Worker) allRq.get(Thread.currentThread().getName());
        }
        if (w != null) {
            synchronized (w) {
                w.setProgress(newProgress, newProgressCode);
            }
        }
    }

    /**
     * Notification that the thread is starting (called before 'process' function)
     * 
     * <p>
     * Don't forget to also check for the shutdownRequested() and if set, throw InterruptedException
     * 
     * @param p a progress indicator 0-nothing done, 100-all done
     * @param progressCode a string code for progress. Derived classes must overload the
     *        getProgressMsg to translate the code
     */
    public static void updateProgress(int newProgress, String newProgressCode) {
        Worker w = null;
        synchronized (allRq) {
            w = (Worker) allRq.get(Thread.currentThread().getName());
        }
        if (w != null) {
            synchronized (w) {
                w.setProgress(newProgress, newProgressCode);
            }
        }
    }

    /**
     * stops the current execution (*unsafe one). Use the shutdown(), sleep 5 seconds and only if
     * the thread is not done, then use cancel()...
     */
    @SuppressWarnings("deprecation")
    public void cancel() {
        if (this.intState != IntState.STOPPED) {
            Thread.currentThread().stop();
            // Notification that the thread has stopped (called after 'process' function)
            notifyStopped();
        }
    }

    /**
     * Abstract function that is called from <b>run</b> function of the thread. This should be
     * implemented by each child class.
     */
    public abstract void process();

    /**
     * internal run - do not overwrite. Overwrite process() instead.
     */
    final public void run() {
        // Notification that the thread is starting (called before 'process' function)
        this.setIntState(IntState.STARTED);

        synchronized (allRq) {
            allRq.put(Thread.currentThread().getName(), this);
        }
        // real thread (run) function
        try {
            process();
        } catch (Throwable t) {
            if (t instanceof ThreadDeath) {
                logger.log("Thread stopped with ThreaDeath, not graceful !");
                this.setIntState(IntState.STOPPED);

                // Notification that the thread has stopped (called after 'process' function)
                notifyStopped();

                // ThreadDeath must be propagated
                throw (ThreadDeath) t;
            } else if (Exceptions.getRootCause(t) instanceof BeingDyingRtException) {
                logger.log("Thread stopped at user request...");
            } else {
                logger.alarm(t.getMessage(), t);
            }
        }
        this.setIntState(IntState.STOPPED);

        // Notification that the thread has stopped (called after 'process' function)
        notifyStopped();
    }

    /**
     * Notification that the thread has stopped (called after 'process' function)
     */
    final private void notifyStopped() {
        PostOffice.shout(MTEventTypes.EV_THREAD_END, this);

        synchronized (allRq) {
            allRq.remove(Thread.currentThread().getName());
        }
    }

    /**
     * Notification that the thread has stopped (called after 'process' function)
     */
    final private void notifyUpdated() {
        PostOffice.shout(MTEventTypes.EV_THREAD_UPD, this);
    }

    /**
     * graceful thread stop - the framework will set this 5 seconds before thread is killed. Client
     * threads should check this every now and then and if true, NOT exit or return, but throw
     * GracefulShutdownRtException. This specific exception will not be reported...
     * 
     * <p>
     * Implementing work items should check this flag before doing anything of duration and just
     * stop if the flag is true.
     * 
     * @see com.sigma.hframe.jmt.BeingDyingRtException
     * @return true if the thread should stop...
     */
    public static boolean die() {
        Worker w = null;
        synchronized (allRq) {
            w = (Worker) allRq.get(Thread.currentThread().getName());
        }
        if (w != null) {
            synchronized (w) {
                return w.dying;
            }
        }

        // it's a thread not controlled by our framework - just keep doing whatever...
        return false;
    }

    /**
     * graceful thread stop - the framework will set this 5 seconds before thread is killed. Client
     * threads should check this every now and then and if true, NOT exit or return, but throw
     * GracefulShutdownRtException. This specific exception will not be reported...
     * 
     * @see com.sigma.hframe.jmt.BeingDyingRtException
     * @return true if the thread should stop...
     */
    public void shutdown() {
        synchronized (this) {
            this.dying = true;
        }
    }

    void setIntState(IntState intState) {
        this.intState = intState;
    }

    /** check if work is done */
    public boolean isDone() {
        return this.intState == IntState.STOPPED;
    }

    /**
     * the count of the thread
     */
    public long       _index            = 0;

    protected int     progress          = 0;

    /** this is set to true 5 sec before thread is killed. Client code should check this and stop. */
    protected boolean dying = false;

    /** started=1, stoped=0 */
    static enum IntState {
        STOPPED, STARTED
    }

    private IntState                    intState  = IntState.STOPPED;

    /*
     * Thread listeners list; listener that get the notifications when the thread starts/stops
     */
    /** Map<String taskname, MTWrkRq> all wrk rq in progress */
    private static Map<String, Worker> allRq     = new HashMap<String, Worker>();
    private static final Log logger = Log.Factory.create("", Worker.class.getName());
}

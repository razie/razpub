/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.events;

import java.util.List;

import com.razie.pub.hframe.base.data.MemDb;

/**
 * very simple event dispatcher
 * 
 * TODO detailed docs
 * 
 * TODO make singleton/factory to allow user-filtering in custom implementations
 * 
 * @author razvanc99
 * 
 */
public class PostOffice {
    static MemDb<String, EvListener> listeners = new MemDb<String, EvListener>();

    /**
     * main event notification thing
     * 
     * @param eventId the id of the event
     * @param args optional name-value pairs, i.e. "someparm", "somevalue", ...
     */
    public static void shout(String eventId, Object... args) {
        synchronized (listeners) {
            // TODO dont sync eating events
            List<EvListener> list = listeners.get(eventId);

            // TODO this shold be outside the sync but don't want to clone the list (poltergeist)
            // any ideas? Idea1: make a SyncMemDb with read/write lock in separate methods:
            // acquire(x) release(x)
            for (EvListener l : list) {
                l.eatThis(eventId, args);
            }
        }
    }

    /**
     * register an event listener
     * 
     * @param eventId the ID of the event to listen to
     * @param l the listener
     */
    public static void register(String eventId, EvListener l) {
        synchronized (listeners) {
            listeners.put(eventId, l);
        }
    }
}

/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.events;

import java.util.List;

import com.razie.pub.hframe.base.data.MemDb;


/**
 * very simple event dispatcher
 * 
 * TODO detailed docs
 * 
 * @author razvanc99
 * 
 */
public class PostOffice {
    static MemDb<String, EvListener> listeners = new MemDb<String, EvListener>();

    /** main event notification thing */
    public static void shout(String eventId, Object... args) {
        synchronized (listeners) {
            // TODO dont sync eating events
            List<EvListener> list = listeners.get(eventId);

            // TODO this shold be outside the sync but don't want to clone the list (poltergeist)
            // any ideas?
            for (EvListener l : list) {
                l.eatThis(eventId, args);
            }
        }
    }

    public static void listening(String eventId, EvListener l) {
        synchronized (listeners) {
            listeners.put(eventId, l);
        }
    }
}

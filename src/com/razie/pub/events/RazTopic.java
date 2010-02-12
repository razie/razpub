/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

import razie.base.AttrAccess;
import razie.base.AttrAccessImpl;


/**
 * a topic sends each message to all listeners
 * 
 * @author razvanc
 */
public class RazTopic extends RazDestination {
    public RazTopic(String name, boolean distributed, RazDestination.QOS qos) {
        super(name, distributed, qos);
    }

    /**
     * accept a message for this destination
     * 
     * @param srcId the id of the source of the event. normally of the form: "agentNm"
     * @param eventId the id of the event
     * @param args optional name-value pairs, i.e. "someparm", "somevalue", ... IF you only want to
     *        pass an object which is the subject of the event, the convention is that it should be
     *        named "subject".
     */
    public void send(String srcId, String eventId, Object... args) {
        AttrAccess info = null; // lazy
        synchronized (listeners) {
            // TODO 3 PERF dont sync eating events
            List<WeakReference<EvListener>> list = listeners.get(eventId);

            // TODO 3 PERF this shold be outside the sync but don't want to clone the list (poltergeist)
            // any ideas? Idea1: make a SyncMemDb with read/write lock in separate methods:
            // acquire(x) release(x)
            for (Iterator<WeakReference<EvListener>> i = list.iterator(); i.hasNext();) {
                EvListener l = i.next().get();
                if (l != null) {
                    if (info == null)
                        info = new AttrAccessImpl(args);
                    l.eatThis(srcId, eventId, info);
                } else
                    // garbage collection...
                    i.remove();
            }
            
            list = listeners.get("*");
            
            // TODO this shold be outside the sync but don't want to clone the list (poltergeist)
            // any ideas? Idea1: make a SyncMemDb with read/write lock in separate methods:
            // acquire(x) release(x)
            for (Iterator<WeakReference<EvListener>> i = list.iterator(); i.hasNext();) {
                EvListener l = i.next().get();
                if (l != null) {
                    if (info == null)
                        info = new AttrAccessImpl(args);
                    l.eatThis(srcId, eventId, info);
                } else
                    // garbage collection...
                    i.remove();
            }
            
        }
    }
}


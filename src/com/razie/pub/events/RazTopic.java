/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.lang.ref.WeakReference;
import java.util.Iterator;

import com.razie.pub.base.AttrAccess;

/** a topic sends each message to all listeners
 * 
 * @author razvanc
 *
 */
public class RazTopic extends RazDestination {
    public RazTopic(String name, boolean distributed, QOS qos) {
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
            // TODO dont sync eating events

            // TODO this shold be outside the sync but don't want to clone the list (poltergeist)
            // any ideas? Idea1: make a SyncMemDb with read/write lock in separate methods:
            // acquire(x) release(x)
            for (Iterator<WeakReference<EvListener>> i = listeners.iterator(); i.hasNext();) {
                EvListener l = i.next().get();
                if (l != null) {
                    if (info == null)
                        info = new AttrAccess.Impl(args);
                    l.eatThis(srcId, eventId, info);
                } else
                    // garbage collection...
                    i.remove();
            }
        }
    }
}

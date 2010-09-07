/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.lang.ref.WeakReference;
import java.util.List;

import razie.base.AttrAccess;
import razie.base.AttrAccessImpl;


/**
 * like a jms queue
 * 
 * TODO detailed docs
 * 
 * @author razvanc
 * 
 */
public class RazQueue extends RazDestination {
    int lastConsumer = -1; // so that the first is index 0

    public RazQueue(String name, boolean distributed, RazDestination.QOS qos) {
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

        // load-balancing the consumers
        boolean repeat = true;
        while (repeat) {
            List<WeakReference<EvListener>> list = RazDestination.listeners.get(eventId);
            // TODO protect against concurrent changes
            // TODO protect against no listeners, i.e. list==null
            
            if (++lastConsumer >= list.size()) {
                lastConsumer = 0;
            }

            WeakReference<EvListener> l = lastConsumer < list.size() ? list.get(lastConsumer) : null;

            if (l != null) {
                if (l.get() == null) {
                    list.remove(lastConsumer);
                } else {
                    EvListener evl = l.get();
                    if (info == null)
                        info = new AttrAccessImpl(args);
                    evl.eatThis(srcId, eventId, info);
                    repeat = false;
                }
            } else {
                repeat = false;
            }
        }
    }

}

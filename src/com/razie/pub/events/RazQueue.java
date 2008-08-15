/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.lang.ref.WeakReference;

import com.razie.pub.assets.AssetBrief;
import com.razie.pub.base.AttrAccess;

public class RazQueue extends RazDestination {
    int lastConsumer = -1; // so that the first is index 0

    public RazQueue(String name, boolean distributed, QOS qos) {
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
            if (++lastConsumer >= this.listeners.size()) {
                lastConsumer = 0;
            }

            WeakReference<EvListener> l = lastConsumer < this.listeners.size() ? this.listeners
                    .get(lastConsumer) : null;

            if (l != null) {
                if (l.get() == null) {
                    this.listeners.remove(lastConsumer);
                } else {
                    EvListener evl = l.get();
                    if (info == null)
                        info = new AttrAccess.Impl(args);
                    evl.eatThis(srcId, eventId, info);
                    repeat = false;
                }
            } else {
                repeat = false;
            }
        }
    }

    public AssetBrief getBrief() {
        // TODO Auto-generated method stub
        return null;
    }

}
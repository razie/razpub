/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.data.MemDb;
import com.razie.pub.events.RazDestination.QOS;

/**
 * very simple event dispatcher
 * 
 * NOTE that listeners are proxied with WeakReference and will be collected - if you want them to
 * live, keep them somewhere, they're yours, not mine
 * 
 * NOTE that there is also a distributed support offered by the Agent, for distributing
 * notifications to all agents
 * 
 * @author razvanc99
 * 
 */
public class PostOffice {
    static MemDb<String, WeakReference<EvListener>> listeners = new MemDb<String, WeakReference<EvListener>>();
    static PostOfficeNew                            theDefault;

    /** factory stub */
    public static PostOfficeNew getInstance() {
        PostOfficeNew singleton = (PostOfficeNew) NoStatics.get(PostOfficeNew.class);

        if (singleton == null) {
            singleton = (PostOfficeNew) NoStatics.put(PostOfficeNew.class, new PostOfficeNew());

            // these destinations are default...
            singleton.register(PostOfficeNew.DFLT_TOPIC, new RazTopic(PostOfficeNew.DFLT_TOPIC, true,
                    QOS.VOLANS));
            singleton.register(PostOfficeNew.DFLT_LOCAL_TOPIC, new RazTopic(PostOfficeNew.DFLT_LOCAL_TOPIC,
                    false, QOS.VOLANS));
        }

        return singleton;
    }

    /**
     * main event notification thing - local AND remote
     * 
     * @param srcId the id of the source of the event. normally of the form: "agentNm"
     * @param eventId the id of the event
     * @param args optional name-value pairs, i.e. "someparm", "somevalue", ... IF you only want to
     *        pass an object which is the subject of the event, the convention is that it should be
     *        named "subject".
     */
    public static void shout(String srcId, String eventId, Object... args) {
        getInstance().shoutImpl(srcId, eventId, args);
    }

    /**
     * @see shout()
     */
    protected void shoutImpl(String srcId, String eventId, Object... args) {
        shoutLocalImpl(srcId, eventId, args);
    }

    /**
     * main event notification thing - local only
     * 
     * @param srcId the id of the source of the event. normally of the form: "agentNm"
     * @param eventId the id of the event
     * @param args optional name-value pairs, i.e. "someparm", "somevalue", ... IF you only want to
     *        pass an object which is the subject of the event, the convention is that it should be
     *        named "subject".
     */
    public static void shoutLocal(String srcId, String eventId, Object... args) {
        getInstance().shoutLocalImpl(srcId, eventId, args);
    }

    /**
     * @see shoutLocal()
     */
    protected void shoutLocalImpl(String srcId, String eventId, Object... args) {
        AttrAccess info = null; // lazy
        synchronized (listeners) {
            // TODO dont sync eating events
            List<WeakReference<EvListener>> list = listeners.get(eventId);

            // TODO this shold be outside the sync but don't want to clone the list (poltergeist)
            // any ideas? Idea1: make a SyncMemDb with read/write lock in separate methods:
            // acquire(x) release(x)
            for (Iterator<WeakReference<EvListener>> i = list.iterator(); i.hasNext();) {
                EvListener l = i.next().get();
                if (l != null) {
                    if (info == null)
                        info = new AttrAccess.Impl(args);
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
                        info = new AttrAccess.Impl(args);
                    l.eatThis(srcId, eventId, info);
                } else
                    // garbage collection...
                    i.remove();
            }
        }

        PostOfficeNew.send(PostOfficeNew.DFLT_TOPIC, srcId, eventId, args);
    }

    /**
     * register an event listener for a specific event
     * 
     * @param eventId the ID of the event to listen to. Use "*" to listen to all.
     * @param l the listener
     */
    public static void register(String eventId, EvListener l) {
        getInstance().registerImpl(eventId, l);
    }

    /**
     * register a smart/reflective event listener
     * 
     * @param eventId the ID of the event to listen to
     * @param l the listener
     */
    public static void register(EvListener l) {
        for (String eventId : l.interestedIn())
            register(eventId, l);
    }

    /**
     * @see register()
     */
    protected void registerImpl(String eventId, EvListener l) {
        synchronized (listeners) {
            listeners.put(eventId, new WeakReference<EvListener>(l));
        }

        PostOfficeNew.register(PostOfficeNew.DFLT_TOPIC, l);
    }
}
